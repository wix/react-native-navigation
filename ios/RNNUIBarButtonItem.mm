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
    if (@available(iOS 26.0, *)) {
        // The UIButton already paints its own iconBackground chrome, so the
        // iOS 26 shared Platter would double-decorate it. Defaults to YES;
        // callers can opt back in via the hideSharedBackground option.
        self.hidesSharedBackground =
            [buttonOptions.hideSharedBackground withDefault:YES];
    }
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
    if (@available(iOS 26.0, *)) {
        // iOS 26 wraps every bar button item in a shared Platter/Liquid-Glass
        // background. React-rendered custom views supply their own chrome, and
        // the Platter both double-decorates and misaligns custom views taller
        // than the standard pill (~30pt). Defaults to YES; callers can opt
        // back in via the hideSharedBackground option.
        self.hidesSharedBackground =
            [buttonOptions.hideSharedBackground withDefault:YES];
        // Pin the custom view to a stable 44pt bar-item size so the navbar
        // reserves the final slot up-front (incl. during push-transition
        // snapshots) and doesn't relayout once React mounts. Without this,
        // React's post-mount sizeToFit changes bounds, the navbar relayouts,
        // and the customView visibly snaps from a 0x0 / wrong-position state
        // to its centered final frame. React content centers within via its
        // own flex layout.
        reactView.translatesAutoresizingMaskIntoConstraints = NO;
        [reactView.widthAnchor constraintEqualToConstant:44].active = YES;
        [reactView.heightAnchor constraintEqualToConstant:44].active = YES;
    }
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
