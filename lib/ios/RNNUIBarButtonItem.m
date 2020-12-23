#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"
#import "UIImage+insets.h"
#import "UIImage+tint.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNUIBarButtonItem ()

@property(nonatomic, strong) NSLayoutConstraint *widthConstraint;
@property(nonatomic, strong) NSLayoutConstraint *heightConstraint;

@end

@implementation RNNUIBarButtonItem

- (instancetype)init:(NSString *)buttonId icon:(UIImage *)iconImage tintColor:(UIColor *)tintColor {
    self = [super initWithImage:tintColor ? [iconImage withTintColor:tintColor] : iconImage
                          style:UIBarButtonItemStylePlain
                         target:nil
                         action:nil];
    self.buttonId = buttonId;
    return self;
}

- (instancetype)init:(NSString *)buttonId
                icon:(UIImage *)iconImage
                size:(CGSize)size
     backgroundColor:(UIColor *)backgroundColor
        cornerRadius:(CGFloat)cornerRadius
              insets:(UIEdgeInsets)edgeInsets
           tintColor:(UIColor *)tintColor {

    UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, size.width, size.height)];
    [button addTarget:self
                  action:@selector(onButtonPressed)
        forControlEvents:UIControlEventTouchUpInside];
    [button setImage:[(tintColor ? [iconImage withTintColor:tintColor]
                                 : iconImage) imageWithInsets:edgeInsets]
            forState:UIControlStateNormal];
    button.backgroundColor = backgroundColor;
    button.layer.cornerRadius = cornerRadius;
    button.clipsToBounds = !!cornerRadius;

    self = [super initWithCustomView:button];

    self.buttonId = buttonId;
    return self;
}

- (instancetype)init:(NSString *)buttonId withTitle:(NSString *)title {
    self = [super initWithTitle:title style:UIBarButtonItemStylePlain target:nil action:nil];
    self.buttonId = buttonId;
    return self;
}

- (instancetype)init:(NSString *)buttonId withCustomView:(RCTRootView *)reactView {
    self = [super initWithCustomView:reactView];

    reactView.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
    reactView.delegate = self;
    reactView.backgroundColor = [UIColor clearColor];
    reactView.hidden = CGRectEqualToRect(reactView.frame, CGRectZero);

    [NSLayoutConstraint deactivateConstraints:reactView.constraints];
    self.widthConstraint =
        [NSLayoutConstraint constraintWithItem:reactView
                                     attribute:NSLayoutAttributeWidth
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:nil
                                     attribute:NSLayoutAttributeNotAnAttribute
                                    multiplier:1.0
                                      constant:reactView.intrinsicContentSize.width];
    self.heightConstraint =
        [NSLayoutConstraint constraintWithItem:reactView
                                     attribute:NSLayoutAttributeHeight
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:nil
                                     attribute:NSLayoutAttributeNotAnAttribute
                                    multiplier:1.0
                                      constant:reactView.intrinsicContentSize.height];
    [NSLayoutConstraint activateConstraints:@[ self.widthConstraint, self.heightConstraint ]];
    self.buttonId = buttonId;
    return self;
}

- (instancetype)init:(NSString *)buttonId withSystemItem:(NSString *)systemItemName {
    UIBarButtonSystemItem systemItem = [RCTConvert UIBarButtonSystemItem:systemItemName];
    self = [super initWithBarButtonSystemItem:systemItem target:nil action:nil];
    self.buttonId = buttonId;
    return self;
}

- (void)notifyDidAppear {
    if ([self.customView isKindOfClass:[RNNReactView class]]) {
        [((RNNReactView *)self.customView) componentDidAppear];
    }
}

- (void)notifyDidDisappear {
    if ([self.customView isKindOfClass:[RNNReactView class]]) {
        [((RNNReactView *)self.customView) componentDidDisappear];
    }
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    self.widthConstraint.constant = rootView.intrinsicContentSize.width;
    self.heightConstraint.constant = rootView.intrinsicContentSize.height;
    [rootView setNeedsUpdateConstraints];
    [rootView updateConstraintsIfNeeded];
    rootView.hidden = NO;
}

- (void)onButtonPressed {
    [self.target performSelector:self.action withObject:self afterDelay:0];
}

@end
