#import "TopBarPresenter.h"
#import "UIImage+tint.h"
#import "RNNFontAttributesCreator.h"
#import "UIColor+RNNUtils.h"
#import "UIViewController+LayoutProtocol.h"
#import "UINavigationController+RNNOptions.h"

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
    [self setBackButtonOptions:options.backButton];
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
}

- (void)mergeOptions:(RNNTopBarOptions *)options withDefault:(RNNTopBarOptions *)withDefault {
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

    if (options.title.hasValue) {
        [self setTitleAttributes:withDefault.title];
    }
    
    if (options.backButton.hasValue) {
        [self setBackButtonOptions:withDefault.backButton];
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

- (void)componentDidAppear {
    NSString* backButtonTestID = [self.navigationController.topViewController.resolveOptionsWithDefault.topBar.backButton.testID getWithDefaultValue:nil];
    [self.navigationController setBackButtonTestID:backButtonTestID];
}

- (void)setBackButtonOptions:(RNNBackButtonOptions *)backButtonOptions {
    UIImage* icon = [backButtonOptions.icon getWithDefaultValue:nil];
    UIColor* color = [backButtonOptions.color getWithDefaultValue:nil];
    NSString* title = [backButtonOptions.title getWithDefaultValue:nil];
    BOOL showTitle = [backButtonOptions.showTitle getWithDefaultValue:YES];
    NSString* fontFamily = [backButtonOptions.fontFamily getWithDefaultValue:nil];
    NSNumber* fontSize = [backButtonOptions.fontSize getWithDefaultValue:nil];
    
    UIViewController *previousViewControllerInStack = self.previousViewControllerInStack;
    UIBarButtonItem *backItem = [UIBarButtonItem new];

    icon = color
    ? [[icon withTintColor:color] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
    : icon;
    [self setBackIndicatorImage:icon withColor:color];
    
    if (showTitle) {
        backItem.title = title ? title : previousViewControllerInStack.navigationItem.title;
    } else {
        backItem.title = @"";
    }
    
    backItem.tintColor = color;
	
    if (fontFamily) {
        CGFloat resolvedFontSize = fontSize ? fontSize.floatValue : 17.0;
        [backItem setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys: [UIFont fontWithName:fontFamily size:resolvedFontSize], NSFontAttributeName, nil] forState:UIControlStateNormal];
        [backItem setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys: [UIFont fontWithName:fontFamily size:resolvedFontSize], NSFontAttributeName, nil] forState:UIControlStateHighlighted];
    }
    
    previousViewControllerInStack.navigationItem.backBarButtonItem = backItem;
}

- (UIViewController *)previousViewControllerInStack {
    NSArray* stackChildren = self.navigationController.viewControllers;
    UIViewController *previousViewControllerInStack = stackChildren.count > 1 ? stackChildren[stackChildren.count - 2] : self.navigationController.topViewController;
    return previousViewControllerInStack;
}

- (BOOL)transparent {
    return self.backgroundColor.isTransparent;
}

@end
