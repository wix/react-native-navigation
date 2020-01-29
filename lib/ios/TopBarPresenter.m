#import "TopBarPresenter.h"
#import "UIImage+tint.h"
#import "RNNFontAttributesCreator.h"

@implementation TopBarPresenter

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController {
    self = [super init];
    self.boundViewController = boundNavigationController;
    return self;
}

- (void)applyOptions:(RNNTopBarOptions *)options {
    [self setTranslucent:[options.background.translucent getWithDefaultValue:NO]];
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
    [self showBorder:![options.noBorder getWithDefaultValue:NO]];
    [self setBackButtonIcon:[options.backButton.icon getWithDefaultValue:nil] withColor:[options.backButton.color getWithDefaultValue:nil] title:[options.backButton.title getWithDefaultValue:nil] showTitle:[options.backButton.showTitle getWithDefaultValue:YES]];
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
}

- (void)mergeOptions:(RNNTopBarOptions *)options defaultOptions:(RNNTopBarOptions *)defaultOptions {
    if (options.background.color.hasValue) {
        [self setBackgroundColor:options.background.color.get];
    }
    
    if (options.noBorder.hasValue) {
        [self showBorder:![options.noBorder get]];
    }
    
    if (options.background.translucent.hasValue) {
        [self setTranslucent:[options.background.translucent get]];
    }
    
    RNNLargeTitleOptions* largeTitleOptions = options.largeTitle;
    if (largeTitleOptions.color.hasValue || largeTitleOptions.fontSize.hasValue || largeTitleOptions.fontFamily.hasValue) {
        [self setLargeTitleAttributes:largeTitleOptions];
    }

    [self setTitleAttributes:options.title];
    
    if (options.backButton.hasValue) {
        [self setBackButtonIcon:[defaultOptions.backButton.icon getWithDefaultValue:nil] withColor:[defaultOptions.backButton.color getWithDefaultValue:nil] title:[defaultOptions.backButton.title getWithDefaultValue:nil] showTitle:[defaultOptions.backButton.showTitle getWithDefaultValue:YES]];
    }
}

- (UINavigationController *)navigationController {
    return (UINavigationController *)self.boundViewController;
}

- (void)showBorder:(BOOL)showBorder {
    [self.navigationController.navigationBar setShadowImage:showBorder ? nil : [UIImage new]];
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.navigationController.navigationBar setBackIndicatorImage:image];
    [self.navigationController.navigationBar setBackIndicatorTransitionMaskImage:image];
}

- (void)setBackgroundColor:(UIColor *)backgroundColor {
    _backgroundColor = backgroundColor;
    [self updateBackgroundAppearance];
}

- (void)updateBackgroundAppearance {
    if (self.transparent) {
        [self setBackgroundColorTransparent];
    } else if (_backgroundColor) {
        self.navigationController.navigationBar.translucent = NO;
        self.navigationController.navigationBar.barTintColor = _backgroundColor;
    } else if (_translucent) {
        self.navigationController.navigationBar.translucent = YES;
    } else {
        self.navigationController.navigationBar.translucent = NO;
        self.navigationController.navigationBar.barTintColor = nil;
    }
}

- (void)setBackgroundColorTransparent {
    self.navigationController.navigationBar.barTintColor = UIColor.clearColor;
    self.navigationController.navigationBar.translucent = YES;
    self.navigationController.navigationBar.shadowImage = [UIImage new];
    [self.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString* fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [titleOptions.color getWithDefaultValue:nil];
    
    self.navigationController.navigationBar.titleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.navigationController.navigationBar.titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString* fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    
    if (@available(iOS 11.0, *)) {
        self.navigationController.navigationBar.largeTitleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.navigationController.navigationBar.largeTitleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
    }
}

- (void)setBackButtonIcon:(UIImage *)icon withColor:(UIColor *)color title:(NSString *)title showTitle:(BOOL)showTitle {
    UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
    NSArray* stackChildren = self.navigationController.viewControllers;
    icon = color
    ? [[icon withTintColor:color] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
    : icon;
    [self setBackIndicatorImage:icon withColor:color];

    UIViewController *lastViewControllerInStack = stackChildren.count > 1 ? stackChildren[stackChildren.count - 2] : self.navigationController.topViewController;

    if (showTitle) {
        backItem.title = title ? title : lastViewControllerInStack.navigationItem.title;
    }
    backItem.tintColor = color;

    lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
}

- (BOOL)transparent {
    return (self.backgroundColor && CGColorGetAlpha(self.backgroundColor.CGColor) == 0.0);
}

@end
