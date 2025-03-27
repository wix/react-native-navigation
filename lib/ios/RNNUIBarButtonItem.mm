#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+utils.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTSurface.h>
#endif

@interface RNNUIBarButtonItem ()

@property(nonatomic, strong) NSLayoutConstraint *widthConstraint;
@property(nonatomic, strong) NSLayoutConstraint *heightConstraint;
@property(nonatomic, strong) RNNButtonPressCallback onPress;

@end

@implementation RNNUIBarButtonItem {
    RNNIconCreator *_iconCreator;
    RNNButtonOptions *_buttonOptions;
}

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
    _iconCreator = iconCreator;
    UIImage *icon = [_iconCreator create:buttonOptions];
    UIButton *button =
        [[UIButton alloc] initWithFrame:CGRectMake(0, 0, icon.size.width, icon.size.height)];
    [button addTarget:self
                  action:@selector(onButtonPressed:)
        forControlEvents:UIControlEventTouchUpInside];
    [button setImage:icon
            forState:buttonOptions.isEnabled ? UIControlStateNormal : UIControlStateDisabled];
    [button.widthAnchor constraintEqualToConstant:icon.size.width].active = YES;
    [button.heightAnchor constraintEqualToConstant:icon.size.height].active = YES;
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
    _buttonOptions = buttonOptions;
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
        self.tintColor = color;
    } else
        self.image = [self.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
}

- (void)mergeBackgroundColor:(Color *)color {
    _buttonOptions.iconBackground.color = color;
    [self redrawIcon];
}

- (void)mergeColor:(Color *)color {
    _buttonOptions.color = color;
    [self applyColor:color.get];
    [self redrawIcon];
}

- (void)redrawIcon {
    if (_buttonOptions.icon.hasValue && [self.customView isKindOfClass:UIButton.class]) {
        UIImage *icon = [_iconCreator create:_buttonOptions];
        [(UIButton *)self.customView setImage:icon forState:_buttonOptions.state];
    }
}

- (void)applyTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *textAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily withDefault:nil]
                                                 fontSize:[button.fontSize withDefault:@(17)]
                                               fontWeight:[button.fontWeight withDefault:nil]
                                                    color:button.color.get
                                                 centered:NO]];

    [self setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
    [self setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
}

- (void)applyDisabledTitleTextAttributes:(RNNButtonOptions *)button {
    NSMutableDictionary *disabledTextAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily withDefault:nil]
                                                 fontSize:[button.fontSize withDefault:@(17)]
                                               fontWeight:[button.fontWeight withDefault:nil]
                                                    color:[button.disabledColor withDefault:nil]
                                                 centered:NO]];

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


#ifdef RCT_NEW_ARCH_ENABLED
// TODO: Verify
- (void)surface:(RCTSurface *)surface didChangeIntrinsicSize:(CGSize)intrinsicSize {
	self.widthConstraint.constant = intrinsicSize.width;
	self.heightConstraint.constant = intrinsicSize.height;
	[surface setSize:intrinsicSize];
	//[rootView setNeedsUpdateConstraints];
	//[rootView updateConstraintsIfNeeded];
	//surface.hidden = NO;
}
#else
- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    self.widthConstraint.constant = rootView.intrinsicContentSize.width;
    self.heightConstraint.constant = rootView.intrinsicContentSize.height;
    [rootView setNeedsUpdateConstraints];
    [rootView updateConstraintsIfNeeded];
    rootView.hidden = NO;
}
#endif

- (void)onButtonPressed:(RNNUIBarButtonItem *)barButtonItem {
    self.onPress(self.buttonId);
}

@end
