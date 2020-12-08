#import "RNNComponentPresenter.h"
#import "RNNComponentViewController.h"
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
        BOOL focus = [withDefault.topBar.searchBar.focus getWithDefaultValue:NO];

        [viewController setSearchBarWithOptions:[withDefault.topBar.searchBar.placeholder
                                                    getWithDefaultValue:@""]
                                           focus:focus
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

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:mergeOptions resolvedOptions:currentOptions];
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[currentOptions
        mergeOptions:mergeOptions] withDefault:self.defaultOptions];
    RNNComponentViewController *viewController = self.boundViewController;

    if (mergeOptions.backgroundImage.hasValue) {
        [viewController setBackgroundImage:mergeOptions.backgroundImage.get];
    }

    if ([withDefault.topBar.searchBar.visible getWithDefaultValue:NO]) {
        BOOL hideTopBarOnFocus =
            [withDefault.topBar.searchBar.hideTopBarOnFocus getWithDefaultValue:YES];
        BOOL hideOnScroll = [withDefault.topBar.searchBar.hideOnScroll getWithDefaultValue:NO];
        BOOL obscuresBackgroundDuringPresentation =
            [withDefault.topBar.searchBar.obscuresBackgroundDuringPresentation
                getWithDefaultValue:NO];
        BOOL focus = [withDefault.topBar.searchBar.focus getWithDefaultValue:NO];

        [viewController setSearchBarWithOptions:[withDefault.topBar.searchBar.placeholder
                                                    getWithDefaultValue:@""]
                                           focus:focus
                               hideTopBarOnFocus:hideTopBarOnFocus
                                    hideOnScroll:hideOnScroll
            obscuresBackgroundDuringPresentation:obscuresBackgroundDuringPresentation
                                 backgroundColor:[mergeOptions.topBar.searchBar.backgroundColor
                                                     getWithDefaultValue:nil]
                                       tintColor:[mergeOptions.topBar.searchBar.tintColor
                                                     getWithDefaultValue:nil]];
    } else {
        [viewController setSearchBarVisible:NO];
    }

    if (mergeOptions.topBar.drawBehind.hasValue) {
        [viewController setDrawBehindTopBar:mergeOptions.topBar.drawBehind.get];
    }

    if (mergeOptions.bottomTabs.drawBehind.hasValue) {
        [viewController setDrawBehindBottomTabs:mergeOptions.bottomTabs.drawBehind.get];
    }

    if (mergeOptions.topBar.title.text.hasValue) {
        [viewController setNavigationItemTitle:mergeOptions.topBar.title.text.get];
    }

    if (mergeOptions.topBar.largeTitle.visible.hasValue) {
        [viewController setTopBarPrefersLargeTitle:mergeOptions.topBar.largeTitle.visible.get];
    }

    if (mergeOptions.layout.componentBackgroundColor.hasValue) {
        [viewController setBackgroundColor:mergeOptions.layout.componentBackgroundColor.get];
    }

    if (mergeOptions.bottomTab.badgeColor.hasValue) {
        [viewController setTabBarItemBadgeColor:mergeOptions.bottomTab.badgeColor.get];
    }

    if (mergeOptions.bottomTab.visible.hasValue) {
        [viewController.tabBarController
            setCurrentTabIndex:[viewController.tabBarController.viewControllers
                                   indexOfObject:viewController]];
    }

    if (mergeOptions.statusBar.blur.hasValue) {
        [viewController setStatusBarBlur:mergeOptions.statusBar.blur.get];
    }

    if (mergeOptions.statusBar.style.hasValue) {
        [viewController setStatusBarStyle:mergeOptions.statusBar.style.get
                                 animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
    }

    if (mergeOptions.topBar.backButton.visible.hasValue) {
        [viewController setBackButtonVisible:mergeOptions.topBar.backButton.visible.get];
    }

    if (mergeOptions.topBar.leftButtons || mergeOptions.topBar.rightButtons) {
        [_navigationButtons applyLeftButtons:mergeOptions.topBar.leftButtons
                                rightButtons:mergeOptions.topBar.rightButtons
                      defaultLeftButtonStyle:withDefault.topBar.leftButtonStyle
                     defaultRightButtonStyle:withDefault.topBar.rightButtonStyle];
    }

    if (mergeOptions.overlay.interceptTouchOutside.hasValue) {
        viewController.reactView.passThroughTouches =
            !mergeOptions.overlay.interceptTouchOutside.get;
    }

    [_topBarTitlePresenter mergeOptions:mergeOptions.topBar resolvedOptions:withDefault.topBar];
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
