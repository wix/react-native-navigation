#import "RNNUIBarButtonItem.h"
#import "RCTConvert+UIBarButtonSystemItem.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+utils.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTSurface.h>
#endif

static const CGFloat kMinBarButtonSlotSize = 44.0;

@interface RNNUIBarButtonItem ()

@property(nonatomic, strong) NSLayoutConstraint *widthConstraint;
@property(nonatomic, strong) NSLayoutConstraint *heightConstraint;
@property(nonatomic, strong) RNNButtonPressCallback onPress;

@end

@implementation RNNUIBarButtonItem {
    RNNIconCreator *_iconCreator;
    RNNButtonOptions *_buttonOptions;
    BOOL _didApplyWidthResize;
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

- (BOOL)designRequiresCompatibility {
    static BOOL checked = NO;
    static BOOL result = NO;
    if (!checked) {
        checked = YES;
        result = [[[NSBundle mainBundle] objectForInfoDictionaryKey:@"UIDesignRequiresCompatibility"] boolValue];
    }
    return result;
}

- (void)applyCustomViewIntrinsicSize:(CGSize)intrinsicSize {
    if (!self.widthConstraint || !self.heightConstraint || intrinsicSize.width <= 0) {
        return;
    }

    CGFloat width = MAX(intrinsicSize.width, kMinBarButtonSlotSize);
    if (_didApplyWidthResize && width <= self.widthConstraint.constant) {
        return;
    }

    if (self.widthConstraint.constant == width) {
        _didApplyWidthResize = YES;
        return;
    }

    self.widthConstraint.constant = width;
    _didApplyWidthResize = YES;

    [self.customView setNeedsLayout];
    [self invalidateNavigationBarLayout];
}

- (void)invalidateNavigationBarLayout {
    UIView *view = self.customView;
    while (view) {
        if ([view isKindOfClass:[UINavigationBar class]]) {
            [view setNeedsLayout];
            return;
        }
        view = view.superview;
    }
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
        if (![self designRequiresCompatibility]) {
            // Reserve a stable 44pt slot for push-transition snapshots, then grow
            // width once when React reports intrinsic size (e.g. wide pickers).
            // Height stays 44; vertical alignment is handled in React.
            reactView.translatesAutoresizingMaskIntoConstraints = NO;
            self.widthConstraint = [reactView.widthAnchor constraintEqualToConstant:kMinBarButtonSlotSize];
            self.heightConstraint = [reactView.heightAnchor constraintEqualToConstant:kMinBarButtonSlotSize];
            self.widthConstraint.priority = UILayoutPriorityRequired;
            self.heightConstraint.priority = UILayoutPriorityRequired;
            self.widthConstraint.active = YES;
            self.heightConstraint.active = YES;
            if ([reactView isKindOfClass:[RNNReactButtonView class]]) {
                RNNReactButtonView *buttonView = (RNNReactButtonView *)reactView;
                __weak RNNUIBarButtonItem *weakSelf = self;
                buttonView.intrinsicSizeDidChangeHandler = ^(CGSize intrinsicSize) {
                    [weakSelf applyCustomViewIntrinsicSize:intrinsicSize];
                };
            }
        }
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


- (void)onButtonPressed:(RNNUIBarButtonItem *)barButtonItem {
    self.onPress(self.buttonId);
}

@end
