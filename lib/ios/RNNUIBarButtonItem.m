#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+utils.h"

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

- (instancetype)initWithSFSymbol:(RNNButtonOptions *)buttonOptions
                         onPress:(RNNButtonPressCallback)onPress {
    UIImage *iconImage = [UIImage alloc];

    if (@available(iOS 13.0, *)) {
        iconImage = [UIImage systemImageNamed:[buttonOptions.sfSymbol withDefault:nil]];
    }

    self = [super initWithImage:iconImage
                          style:UIBarButtonItemStylePlain
                         target:self
                         action:@selector(onButtonPressed:)];
    [self applyOptions:buttonOptions];
    self.onPress = onPress;
    return self;
}

- (instancetype)initWithIcon:(RNNButtonOptions *)buttonOptions
                     onPress:(RNNButtonPressCallback)onPress {
    UIImage *iconImage = buttonOptions.icon.get;
    self = [super initWithImage:iconImage
                          style:UIBarButtonItemStylePlain
                         target:self
                         action:@selector(onButtonPressed:)];
    [self applyOptions:buttonOptions];
    self.onPress = onPress;
    return self;
}

- (instancetype)initCustomIcon:(RNNButtonOptions *)buttonOptions
                   iconCreator:(RNNIconCreator *)iconCreator
                       onPress:(RNNButtonPressCallback)onPress {
    UIImage *icon = [iconCreator create:buttonOptions];
    UIButton *button =
        [[UIButton alloc] initWithFrame:CGRectMake(0, 0, icon.size.width, icon.size.height)];

    [button addTarget:self
                  action:@selector(onButtonPressed:)
        forControlEvents:UIControlEventTouchUpInside];
    [button setImage:icon
            forState:buttonOptions.isEnabled ? UIControlStateNormal : UIControlStateDisabled];

    self = [super initWithCustomView:button];
    [self applyOptions:buttonOptions];
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
    self = [super initWithCustomView:reactView];
    [self applyOptions:buttonOptions];

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
    self = [super initWithBarButtonSystemItem:systemItem
                                       target:self
                                       action:@selector(onButtonPressed:)];
    [self applyOptions:buttonOptions];
    self.onPress = onPress;
    return self;
}

- (void)applyOptions:(RNNButtonOptions *)buttonOptions {
    self.buttonId = buttonOptions.identifier.get;
    self.accessibilityLabel = [buttonOptions.accessibilityLabel withDefault:nil];
    self.enabled = [buttonOptions.enabled withDefault:YES];
    self.accessibilityIdentifier = [buttonOptions.testID withDefault:nil];
    [self applyColor:[buttonOptions resolveColor]];
    [self applyTitleTextAttributes:buttonOptions];
    [self applyDisabledTitleTextAttributes:buttonOptions];
}

- (void)applyColor:(UIColor *)color {
    if (color) {
        NSMutableDictionary *titleTextAttributes = [NSMutableDictionary
            dictionaryWithDictionary:[self titleTextAttributesForState:UIControlStateNormal]];
        [titleTextAttributes setValue:color forKey:NSForegroundColorAttributeName];
        [self setTitleTextAttributes:titleTextAttributes forState:UIControlStateNormal];
        [self setTitleTextAttributes:titleTextAttributes forState:UIControlStateHighlighted];
    } else
        self.image = [self.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];

    self.tintColor = color;
}

- (void)applyTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *textAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily withDefault:nil]
                                                 fontSize:[button.fontSize withDefault:@(17)]
                                               fontWeight:[button.fontWeight withDefault:nil]
                                                    color:button.color.get]];

    [self setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
    [self setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
}

- (void)applyDisabledTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *disabledTextAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily withDefault:nil]
                                                 fontSize:[button.fontSize withDefault:@(17)]
                                               fontWeight:[button.fontWeight withDefault:nil]
                                                    color:[button.disabledColor withDefault:nil]]];

    [self setTitleTextAttributes:disabledTextAttributes forState:UIControlStateDisabled];
}

- (void)notifyWillAppear {
    if ([self.customView isKindOfClass:[RNNReactView class]]) {
        [((RNNReactView *)self.customView) componentWillAppear];
    }
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
