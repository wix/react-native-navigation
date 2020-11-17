#import "RNNComponentPresenter.h"
#import "RNNComponentViewController.h"
#import "RNNReactTitleView.h"
#import "RNNTitleViewHelper.h"
#import "TopBarTitlePresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNComponentPresenter {
    TopBarTitlePresenter *_topBarTitlePresenter;
}

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithComponentRegistry:componentRegistry defaultOptions:defaultOptions];
    _topBarTitlePresenter =
        [[TopBarTitlePresenter alloc] initWithComponentRegistry:componentRegistry
                                                 defaultOptions:defaultOptions];
    return self;
}

- (void)bindViewController:(id)boundViewController {
    [super bindViewController:boundViewController];
    [_topBarTitlePresenter bindViewController:boundViewController];
    _navigationButtons =
        [[RNNNavigationButtons alloc] initWithViewController:boundViewController
                                           componentRegistry:self.componentRegistry];
}

- (void)componentDidAppear {
    [_topBarTitlePresenter componentDidAppear];
    [_navigationButtons componentDidAppear];
}

- (void)componentDidDisappear {
    [_topBarTitlePresenter componentDidDisappear];
    [_navigationButtons componentDidDisappear];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
    [super applyOptionsOnWillMoveToParentViewController:options];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    [super applyOptions:options];

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [viewController setBackgroundImage:[withDefault.backgroundImage getWithDefaultValue:nil]];
    [viewController
        setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor getWithDefaultValue:nil]];
    [viewController setStatusBarBlur:[withDefault.statusBar.blur getWithDefaultValue:NO]];
    [viewController setStatusBarStyle:[withDefault.statusBar.style getWithDefaultValue:@"default"]
                             animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
    [viewController
        setBackButtonVisible:[withDefault.topBar.backButton.visible getWithDefaultValue:YES]];
    [viewController setInterceptTouchOutside:[withDefault.overlay.interceptTouchOutside
                                                 getWithDefaultValue:YES]];

    if (@available(iOS 13.0, *)) {
        [viewController setBackgroundColor:[withDefault.layout.componentBackgroundColor
                                               getWithDefaultValue:UIColor.systemBackgroundColor]];
    } else {
        [viewController
            setBackgroundColor:[withDefault.layout.componentBackgroundColor
                                   getWithDefaultValue:viewController.view.backgroundColor]];
    }

    if ([withDefault.topBar.searchBar.visible getWithDefaultValue:NO]) {
        BOOL hideTopBarOnFocus =
            [withDefault.topBar.searchBar.hideTopBarOnFocus getWithDefaultValue:YES];
        BOOL hideOnScroll = [withDefault.topBar.searchBar.hideOnScroll getWithDefaultValue:NO];
        BOOL obscuresBackgroundDuringPresentation =
            [withDefault.topBar.searchBar.obscuresBackgroundDuringPresentation
                getWithDefaultValue:NO];

        [viewController setSearchBarWithPlaceholder:[withDefault.topBar.searchBar.placeholder
                                                        getWithDefaultValue:@""]
                                  hideTopBarOnFocus:hideTopBarOnFocus
                                       hideOnScroll:hideOnScroll
               obscuresBackgroundDuringPresentation:obscuresBackgroundDuringPresentation
                                    backgroundColor:[options.topBar.searchBar.backgroundColor
                                                        getWithDefaultValue:nil]
                                          tintColor:[options.topBar.searchBar.tintColor
                                                        getWithDefaultValue:nil]];
    }

    [_topBarTitlePresenter applyOptions:withDefault.topBar];
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    [super applyOptionsOnInit:options];

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [_topBarTitlePresenter applyOptionsOnInit:withDefault.topBar];

    [viewController
        setTopBarPrefersLargeTitle:[withDefault.topBar.largeTitle.visible getWithDefaultValue:NO]];
    [viewController setDrawBehindTopBar:[withDefault.topBar shouldDrawBehind]];
    [viewController setDrawBehindBottomTabs:[withDefault.bottomTabs shouldDrawBehind]];

    if ((withDefault.topBar.leftButtons || withDefault.topBar.rightButtons)) {
        [_navigationButtons applyLeftButtons:withDefault.topBar.leftButtons
                                rightButtons:withDefault.topBar.rightButtons
                      defaultLeftButtonStyle:withDefault.topBar.leftButtonStyle
                     defaultRightButtonStyle:withDefault.topBar.rightButtonStyle];
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)options
     resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[currentOptions
        overrideOptions:options] withDefault:[self defaultOptions]];
    RNNComponentViewController *viewController = self.boundViewController;

    if (options.backgroundImage.hasValue) {
        [viewController setBackgroundImage:options.backgroundImage.get];
    }

    if ([withDefault.topBar.searchBar.visible getWithDefaultValue:NO]) {
        BOOL hideTopBarOnFocus =
            [withDefault.topBar.searchBar.hideTopBarOnFocus getWithDefaultValue:YES];
        BOOL hideOnScroll = [withDefault.topBar.searchBar.hideOnScroll getWithDefaultValue:NO];
        BOOL obscuresBackgroundDuringPresentation =
            [withDefault.topBar.searchBar.obscuresBackgroundDuringPresentation
                getWithDefaultValue:NO];

        [viewController setSearchBarWithPlaceholder:[withDefault.topBar.searchBar.placeholder
                                                        getWithDefaultValue:@""]
                                  hideTopBarOnFocus:hideTopBarOnFocus
                                       hideOnScroll:hideOnScroll
               obscuresBackgroundDuringPresentation:obscuresBackgroundDuringPresentation
                                    backgroundColor:[options.topBar.searchBar.backgroundColor
                                                        getWithDefaultValue:nil]
                                          tintColor:[options.topBar.searchBar.tintColor
                                                        getWithDefaultValue:nil]];
    }

    if (options.topBar.drawBehind.hasValue) {
        [viewController setDrawBehindTopBar:options.topBar.drawBehind.get];
    }

    if (options.bottomTabs.drawBehind.hasValue) {
        [viewController setDrawBehindBottomTabs:options.bottomTabs.drawBehind.get];
    }

    if (options.topBar.title.text.hasValue) {
        [viewController setNavigationItemTitle:options.topBar.title.text.get];
    }

    if (options.topBar.largeTitle.visible.hasValue) {
        [viewController setTopBarPrefersLargeTitle:options.topBar.largeTitle.visible.get];
    }

    if (options.layout.componentBackgroundColor.hasValue) {
        [viewController setBackgroundColor:options.layout.componentBackgroundColor.get];
    }

    if (options.bottomTab.badgeColor.hasValue) {
        [viewController setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    }

    if (options.bottomTab.visible.hasValue) {
        [viewController.tabBarController
            setCurrentTabIndex:[viewController.tabBarController.viewControllers
                                   indexOfObject:viewController]];
    }

    if (options.statusBar.blur.hasValue) {
        [viewController setStatusBarBlur:options.statusBar.blur.get];
    }

    if (options.statusBar.style.hasValue) {
        [viewController setStatusBarStyle:options.statusBar.style.get
                                 animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
    }

    if (options.topBar.backButton.visible.hasValue) {
        [viewController setBackButtonVisible:options.topBar.backButton.visible.get];
    }

    if (options.topBar.leftButtons || options.topBar.rightButtons) {
        [_navigationButtons applyLeftButtons:options.topBar.leftButtons
                                rightButtons:options.topBar.rightButtons
                      defaultLeftButtonStyle:withDefault.topBar.leftButtonStyle
                     defaultRightButtonStyle:withDefault.topBar.rightButtonStyle];
    }

    if (options.overlay.interceptTouchOutside.hasValue) {
        viewController.reactView.passThroughTouches = !options.overlay.interceptTouchOutside.get;
    }

    [_topBarTitlePresenter mergeOptions:options.topBar resolvedOptions:withDefault.topBar];
}

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [_topBarTitlePresenter renderComponents:withDefault.topBar perform:readyBlock];
}

- (void)dealloc {
    [self.componentRegistry clearComponentsForParentId:self.boundComponentId];
}
@end
