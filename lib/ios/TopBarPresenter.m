#import "TopBarPresenter.h"
#import "RNNFontAttributesCreator.h"
#import "RNNUIBarBackButtonItem.h"
#import "UIColor+RNNUtils.h"
#import "UIImage+utils.h"
#import "UINavigationController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"

@implementation TopBarPresenter

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController {
    self = [super init];
    self.boundViewController = boundNavigationController;
    return self;
}

- (void)applyOptions:(RNNTopBarOptions *)options {
    [self setTranslucent:[options.background.translucent withDefault:NO]];
    [self setBackgroundColor:[options.background.color withDefault:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
    [self showBorder:![options.noBorder withDefault:NO]];
    [self setBackButtonOptions:options.backButton];
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
    [self setBackgroundColor:[options.background.color withDefault:nil]];
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

    RNNLargeTitleOptions *largeTitleOptions = options.largeTitle;
    if (largeTitleOptions.color.hasValue || largeTitleOptions.fontSize.hasValue ||
        largeTitleOptions.fontFamily.hasValue) {
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

- (void)setScrollEdgeAppearanceColor:(UIColor *)backgroundColor {
    _scrollEdgeAppearanceColor = backgroundColor;
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
    [self.navigationController.navigationBar setBackgroundImage:[UIImage new]
                                                  forBarMetrics:UIBarMetricsDefault];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString *fontFamily = [titleOptions.fontFamily withDefault:nil];
    NSString *fontWeight = [titleOptions.fontWeight withDefault:nil];
    NSNumber *fontSize = [titleOptions.fontSize withDefault:nil];
    UIColor *fontColor = [titleOptions.color withDefault:nil];

    self.navigationController.navigationBar.titleTextAttributes = [RNNFontAttributesCreator
        createFromDictionary:self.navigationController.navigationBar.titleTextAttributes
                  fontFamily:fontFamily
                    fontSize:fontSize
                  fontWeight:fontWeight
                       color:fontColor];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString *fontFamily = [largeTitleOptions.fontFamily withDefault:nil];
    NSString *fontWeight = [largeTitleOptions.fontWeight withDefault:nil];
    NSNumber *fontSize = [largeTitleOptions.fontSize withDefault:nil];
    UIColor *fontColor = [largeTitleOptions.color withDefault:nil];

    self.navigationController.navigationBar.largeTitleTextAttributes = [RNNFontAttributesCreator
        createFromDictionary:self.navigationController.navigationBar.largeTitleTextAttributes
                  fontFamily:fontFamily
                    fontSize:fontSize
                  fontWeight:fontWeight
                       color:fontColor];
}

- (void)componentDidAppear {
    NSString *backButtonTestID =
        [self.navigationController.topViewController.resolveOptionsWithDefault.topBar.backButton
                .testID withDefault:nil];
    [self.navigationController setBackButtonTestID:backButtonTestID];
}

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 140000
- (UINavigationItemBackButtonDisplayMode)getBackButtonDisplayMode:(NSString *)displayMode {
    if ([displayMode isEqualToString:@"generic"]) {
        return UINavigationItemBackButtonDisplayModeGeneric;
    } else if ([displayMode isEqualToString:@"minimal"]) {
        return UINavigationItemBackButtonDisplayModeMinimal;
    } else {
        return UINavigationItemBackButtonDisplayModeDefault;
    }
}
#endif

- (void)setBackButtonOptions:(RNNBackButtonOptions *)backButtonOptions {
    UIImage *icon = [backButtonOptions.icon withDefault:nil];
    UIColor *color = [backButtonOptions.color withDefault:nil];
    NSString *title = [backButtonOptions.title withDefault:nil];
    BOOL showTitle = [backButtonOptions.showTitle withDefault:YES];
    NSString *fontFamily = [backButtonOptions.fontFamily withDefault:nil];
    NSNumber *fontSize = [backButtonOptions.fontSize withDefault:nil];

    UIViewController *previousViewControllerInStack = self.previousViewControllerInStack;
    UIBarButtonItem *backItem = [[RNNUIBarBackButtonItem alloc] initWithOptions:backButtonOptions];
    UINavigationItem *previousNavigationItem = previousViewControllerInStack.navigationItem;

    if (@available(iOS 13.0, *)) {
        UIImage *sfSymbol = [UIImage systemImageNamed:[backButtonOptions.sfSymbol withDefault:nil]];
        if (backButtonOptions.sfSymbol.hasValue) {
            icon = color ? [sfSymbol imageWithTintColor:color
                                          renderingMode:UIImageRenderingModeAlwaysOriginal]
                         : [sfSymbol imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        } else {
            icon = color ? [[icon withTintColor:color]
                               imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
                         : icon;
        }
    } else {
        icon = color ? [[icon withTintColor:color]
                           imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
                     : icon;
    }

    [self setBackIndicatorImage:icon withColor:color];

    title = title ? title : (previousNavigationItem.title ? previousNavigationItem.title : @"");

    if (showTitle) {
        backItem.title = title;
    } else {
        backItem.title = @"";
    }

    backItem.tintColor = color;

    if (fontFamily) {
        CGFloat resolvedFontSize = fontSize ? fontSize.floatValue : 17.0;
        [backItem setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                                           [UIFont fontWithName:fontFamily
                                                                           size:resolvedFontSize],
                                                           NSFontAttributeName, nil]
                                forState:UIControlStateNormal];
        [backItem setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                                           [UIFont fontWithName:fontFamily
                                                                           size:resolvedFontSize],
                                                           NSFontAttributeName, nil]
                                forState:UIControlStateHighlighted];
    }

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 140000
    if (@available(iOS 14.0, *)) {
        if (backButtonOptions.displayMode.hasValue) {
            previousNavigationItem.backButtonTitle = title;
            previousNavigationItem.backButtonDisplayMode =
                [self getBackButtonDisplayMode:backButtonOptions.displayMode.get];
        } else {
            previousNavigationItem.backBarButtonItem = backItem;
        }
    } else
        previousNavigationItem.backBarButtonItem = backItem;
#else
    previousNavigationItem.backBarButtonItem = backItem;
#endif
}

- (UIViewController *)previousViewControllerInStack {
    NSArray *stackChildren = self.navigationController.viewControllers;
    UIViewController *previousViewControllerInStack =
        stackChildren.count > 1 ? stackChildren[stackChildren.count - 2]
                                : self.navigationController.topViewController;
    return previousViewControllerInStack;
}

- (BOOL)transparent {
    return self.backgroundColor.isTransparent;
}

- (BOOL)scrollEdgeTransparent {
    return self.scrollEdgeAppearanceColor.isTransparent;
}

@end
