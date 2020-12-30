#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+insets.h"
#import "UIImage+tint.h"

@interface RNNUIBarButtonItem ()

@property(nonatomic, strong) NSLayoutConstraint *widthConstraint;
@property(nonatomic, strong) NSLayoutConstraint *heightConstraint;
@property(nonatomic, strong) RNNButtonPressCallback onPress;

@end

@implementation RNNUIBarButtonItem

- (instancetype)init {
    self = [super init];
    self.target = self;
    self.action = @selector(onButtonPressed:);
    return self;
}

- (instancetype)initWithIcon:(RNNButtonOptions *)buttonOptions
                     onPress:(RNNButtonPressCallback)onPress {
    UIColor *tintColor = [buttonOptions.color getWithDefaultValue:nil];
    UIImage *iconImage = buttonOptions.icon.get;
    [self applyOptions:buttonOptions];
    self = [super initWithImage:tintColor ? [iconImage withTintColor:tintColor] : iconImage
                          style:UIBarButtonItemStylePlain
                         target:self
                         action:@selector(onButtonPressed:)];
    self.onPress = onPress;
    return self;
}

- (instancetype)initCustomIcon:(RNNButtonOptions *)buttonOptions
                       onPress:(RNNButtonPressCallback)onPress {
    UIImage *iconImage = buttonOptions.icon.get;
    UIColor *tintColor = [buttonOptions.color getWithDefaultValue:nil];
    CGFloat cornerRadius =
        [buttonOptions.iconBackground.cornerRadius getWithDefaultValue:@(0)].floatValue;

    UIButton *button =
        [[UIButton alloc] initWithFrame:CGRectMake(0, 0,
                                                   [buttonOptions.iconBackground.width
                                                       getWithDefaultValue:@(iconImage.size.width)]
                                                       .floatValue,
                                                   [buttonOptions.iconBackground.height
                                                       getWithDefaultValue:@(iconImage.size.width)]
                                                       .floatValue)];

    [button addTarget:self
                  action:@selector(onButtonPressed:)
        forControlEvents:UIControlEventTouchUpInside];
    [button setImage:[(tintColor ? [iconImage withTintColor:tintColor]
                                 : iconImage) imageWithInsets:buttonOptions.iconInsets.UIEdgeInsets]
            forState:UIControlStateNormal];
    button.backgroundColor = [buttonOptions.iconBackground.color getWithDefaultValue:nil];
    button.layer.cornerRadius = cornerRadius;
    button.clipsToBounds = !!cornerRadius;

    [self applyOptions:buttonOptions];
    self = [super initWithCustomView:button];
    self.onPress = onPress;
    return self;
}

- (instancetype)initWithTitle:(RNNButtonOptions *)buttonOptions
                      onPress:(RNNButtonPressCallback)onPress {
    self = [super initWithTitle:buttonOptions.text.get
                          style:UIBarButtonItemStylePlain
                         target:self
                         action:@selector(onButtonPressed:)];
    self.onPress = onPress;
    [self applyOptions:buttonOptions];
    return self;
}

- (instancetype)initWithCustomView:(RNNReactView *)reactView
                     buttonOptions:(RNNButtonOptions *)buttonOptions
                           onPress:(RNNButtonPressCallback)onPress {
    [self applyOptions:buttonOptions];
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
    self.onPress = onPress;
    return self;
}

- (instancetype)initWithSystemItem:(RNNButtonOptions *)buttonOptions
                           onPress:(RNNButtonPressCallback)onPress {
    UIBarButtonSystemItem systemItem =
        [RCTConvert UIBarButtonSystemItem:buttonOptions.systemItem.get];
    [self applyOptions:buttonOptions];
    self = [super initWithBarButtonSystemItem:systemItem
                                       target:self
                                       action:@selector(onButtonPressed:)];
    self.onPress = onPress;
    return self;
}

- (void)applyOptions:(RNNButtonOptions *)buttonOptions {
    self.buttonId = buttonOptions.identifier.get;
    self.accessibilityLabel = [buttonOptions.accessibilityLabel getWithDefaultValue:nil];
    self.enabled = [buttonOptions.enabled getWithDefaultValue:YES];
    self.accessibilityIdentifier = [buttonOptions.testID getWithDefaultValue:nil];
    [self applyTitleTextAttributes:buttonOptions];
    [self applyDisabledTitleTextAttributes:buttonOptions];
}

- (void)applyTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *textAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:button.color.get]];

    [self setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
    [self setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
}

- (void)applyDisabledTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *disabledTextAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:[button.disabledColor
                                                              getWithDefaultValue:nil]]];

    [self setTitleTextAttributes:disabledTextAttributes forState:UIControlStateDisabled];
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

- (void)onButtonPressed:(RNNUIBarButtonItem *)barButtonItem {
    self.onPress(self.buttonId);
}

@end
