#import "RNNComponentPresenter.h"
#import "RNNComponentViewController.h"
#import "TopBarTitlePresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNComponentPresenter {
    TopBarTitlePresenter *_topBarTitlePresenter;
    RNNButtonsPresenter *_buttonsPresenter;
}

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions
                         buttonsPresenter:(RNNButtonsPresenter *)buttonsPresenter {
    self = [super initWithComponentRegistry:componentRegistry defaultOptions:defaultOptions];
    _topBarTitlePresenter =
        [[TopBarTitlePresenter alloc] initWithComponentRegistry:componentRegistry
                                                 defaultOptions:defaultOptions];
    _buttonsPresenter = buttonsPresenter;
    return self;
}

- (void)bindViewController:(UIViewController *)viewController {
    [super bindViewController:viewController];
    [_topBarTitlePresenter bindViewController:viewController];
    [_buttonsPresenter bindViewController:viewController];
}

- (void)componentWillAppear {
    [_topBarTitlePresenter componentWillAppear];
    [_buttonsPresenter componentWillAppear];
}

- (void)componentDidAppear {
    [_topBarTitlePresenter componentDidAppear];
    [_buttonsPresenter componentDidAppear];
}

- (void)componentDidDisappear {
    [_topBarTitlePresenter componentDidDisappear];
    [_buttonsPresenter componentDidDisappear];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    [super applyOptions:options];

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [viewController setBackgroundImage:[withDefault.backgroundImage withDefault:nil]];
    [viewController setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor withDefault:nil]];
    [viewController setStatusBarBlur:[withDefault.statusBar.blur withDefault:NO]];
    [viewController setStatusBarStyle:[withDefault.statusBar.style withDefault:@"default"]
                             animated:[withDefault.statusBar.animate withDefault:YES]];
    [viewController setBackButtonVisible:[withDefault.topBar.backButton.visible withDefault:YES]];
    [viewController
        setInterceptTouchOutside:[withDefault.overlay.interceptTouchOutside withDefault:YES]];

    if (@available(iOS 13.0, *)) {
        [viewController setBackgroundColor:[withDefault.layout.componentBackgroundColor
                                               withDefault:UIColor.systemBackgroundColor]];
    } else {
        [viewController setBackgroundColor:[withDefault.layout.componentBackgroundColor
                                               withDefault:viewController.view.backgroundColor]];
    }

    if ([withDefault.topBar.searchBar.visible withDefault:NO]) {
        BOOL hideTopBarOnFocus = [withDefault.topBar.searchBar.hideTopBarOnFocus withDefault:YES];
        BOOL hideOnScroll = [withDefault.topBar.searchBar.hideOnScroll withDefault:NO];
        BOOL obscuresBackgroundDuringPresentation =
            [withDefault.topBar.searchBar.obscuresBackgroundDuringPresentation withDefault:NO];
        BOOL focus = [withDefault.topBar.searchBar.focus withDefault:NO];

        [viewController setSearchBarWithOptions:[withDefault.topBar.searchBar.placeholder
                                                    withDefault:@""]
                                           focus:focus
                               hideTopBarOnFocus:hideTopBarOnFocus
                                    hideOnScroll:hideOnScroll
            obscuresBackgroundDuringPresentation:obscuresBackgroundDuringPresentation
                                 backgroundColor:[options.topBar.searchBar.backgroundColor
                                                     withDefault:nil]
                                       tintColor:[options.topBar.searchBar.tintColor
                                                     withDefault:nil]
                                      cancelText:[withDefault.topBar.searchBar.cancelText
                                                     withDefault:nil]];
    }

    [_topBarTitlePresenter applyOptions:withDefault.topBar];

    if (withDefault.topBar.leftButtons) {
        [_buttonsPresenter applyLeftButtons:withDefault.topBar.leftButtons
                               defaultColor:withDefault.topBar.leftButtonColor
                       defaultDisabledColor:withDefault.topBar.leftButtonDisabledColor
                                   animated:[withDefault.topBar.animateLeftButtons withDefault:NO]];
    }

    if (withDefault.topBar.rightButtons) {
        [_buttonsPresenter
               applyRightButtons:withDefault.topBar.rightButtons
                    defaultColor:withDefault.topBar.rightButtonColor
            defaultDisabledColor:withDefault.topBar.rightButtonDisabledColor
                        animated:[withDefault.topBar.animateRightButtons withDefault:NO]];
    }
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    [super applyOptionsOnInit:options];

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [_topBarTitlePresenter applyOptionsOnInit:withDefault.topBar];

    [viewController
        setTopBarPrefersLargeTitle:[withDefault.topBar.largeTitle.visible withDefault:NO]];
    [viewController setDrawBehindTopBar:[withDefault.topBar shouldDrawBehind]];
    [viewController setDrawBehindBottomTabs:[withDefault.bottomTabs shouldDrawBehind]];
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

    if ([withDefault.topBar.searchBar.visible withDefault:NO]) {
        BOOL hideTopBarOnFocus = [withDefault.topBar.searchBar.hideTopBarOnFocus withDefault:YES];
        BOOL hideOnScroll = [withDefault.topBar.searchBar.hideOnScroll withDefault:NO];
        BOOL obscuresBackgroundDuringPresentation =
            [withDefault.topBar.searchBar.obscuresBackgroundDuringPresentation withDefault:NO];
        BOOL focus = [withDefault.topBar.searchBar.focus withDefault:NO];

        [viewController setSearchBarWithOptions:[withDefault.topBar.searchBar.placeholder
                                                    withDefault:@""]
                                           focus:focus
                               hideTopBarOnFocus:hideTopBarOnFocus
                                    hideOnScroll:hideOnScroll
            obscuresBackgroundDuringPresentation:obscuresBackgroundDuringPresentation
                                 backgroundColor:[mergeOptions.topBar.searchBar.backgroundColor
                                                     withDefault:nil]
                                       tintColor:[mergeOptions.topBar.searchBar.tintColor
                                                     withDefault:nil]
                                      cancelText:[withDefault.topBar.searchBar.cancelText
                                                     withDefault:nil]];
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
                                 animated:[withDefault.statusBar.animate withDefault:YES]];
    }

    if (mergeOptions.topBar.backButton.visible.hasValue) {
        [viewController setBackButtonVisible:mergeOptions.topBar.backButton.visible.get];
    }

    if (mergeOptions.topBar.leftButtons) {
        [_buttonsPresenter applyLeftButtons:mergeOptions.topBar.leftButtons
                               defaultColor:withDefault.topBar.leftButtonColor
                       defaultDisabledColor:withDefault.topBar.leftButtonDisabledColor
                                   animated:[withDefault.topBar.animateLeftButtons withDefault:NO]];
    }

    if (mergeOptions.topBar.rightButtons) {
        [_buttonsPresenter
               applyRightButtons:mergeOptions.topBar.rightButtons
                    defaultColor:withDefault.topBar.rightButtonColor
            defaultDisabledColor:withDefault.topBar.rightButtonDisabledColor
                        animated:[withDefault.topBar.animateRightButtons withDefault:NO]];
    }

    if (mergeOptions.topBar.leftButtonColor.hasValue) {
        [_buttonsPresenter applyLeftButtonsColor:mergeOptions.topBar.leftButtonColor.get];
    }

    if (mergeOptions.topBar.rightButtonColor.hasValue) {
        [_buttonsPresenter applyRightButtonsColor:mergeOptions.topBar.rightButtonColor.get];
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
