#import "TopBarPresenter.h"
#import "UIImage+tint.h"
#import "RNNFontAttributesCreator.h"

@implementation TopBarPresenter

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController {
    self = [super init];
    self.boundViewController = boundNavigationController;
    return self;
}

- (UINavigationController *)navigationController {
    return (UINavigationController *)self.boundViewController;
}

- (void)updateBackgroundAppearance {
    if (_transparent) {
        [self setBackgroundColorTransparent];
    } else if (_backgroundColor) {
        self.navigationController.navigationBar.barTintColor = _backgroundColor;
    } else if (_translucent) {
        self.navigationController.navigationBar.translucent = YES;
    } else {
        self.navigationController.navigationBar.translucent = NO;
        self.navigationController.navigationBar.barTintColor = nil;
    }
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

- (void)setBackgroundColorTransparent {
    self.navigationController.navigationBar.barTintColor = UIColor.clearColor;
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

@end
