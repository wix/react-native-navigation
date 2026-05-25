#import "RNNComponentPresenter.h"
#import "RNNComponentViewController.h"
#import "RNNButtonOptions.h"
#import "RNNScrollEdgeEffectOptions.h"
#import "RNNTopBarOptions.h"
#import "TopBarAppearancePresenter.h"
#import "TopBarTitlePresenter.h"
#import "UIColor+RNNUtils.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"

static NSNumber *RNNScrollEdgeEffectStyleFromString(NSString *value) {
    if ([value isEqualToString:@"hard"])
        return @(2);
    if ([value isEqualToString:@"soft"])
        return @(1);
    return @(0); // automatic
}

static RNNScrollEdgeOptions *RNNScrollEdgeOptionsForKey(RNNScrollEdgeEffectOptions *options,
                                                       NSString *edgeKey) {
    if ([edgeKey isEqualToString:@"topEdgeEffect"])
        return options.top;
    if ([edgeKey isEqualToString:@"bottomEdgeEffect"])
        return options.bottom;
    if ([edgeKey isEqualToString:@"leftEdgeEffect"])
        return options.left;
    if ([edgeKey isEqualToString:@"rightEdgeEffect"])
        return options.right;
    return nil;
}

static BOOL RNNScrollEdgeEffectHasAnyValue(RNNScrollEdgeEffectOptions *options) {
    if (!options)
        return NO;
    if (options.hidden.hasValue || options.style.hasValue)
        return YES;
    NSArray<RNNScrollEdgeOptions *> *edges =
        @[ options.top ?: [RNNScrollEdgeOptions new], options.bottom ?: [RNNScrollEdgeOptions new],
           options.left ?: [RNNScrollEdgeOptions new], options.right ?: [RNNScrollEdgeOptions new] ];
    for (RNNScrollEdgeOptions *edge in edges) {
        if (edge.hidden.hasValue || edge.style.hasValue)
            return YES;
    }
    return NO;
}

static void RNNApplyScrollEdgeEffectToScrollView(UIScrollView *scrollView,
                                                 RNNScrollEdgeEffectOptions *options) {
    if (![scrollView respondsToSelector:NSSelectorFromString(@"topEdgeEffect")])
        return;
    NSArray<NSString *> *edgeKeys =
        @[ @"topEdgeEffect", @"bottomEdgeEffect", @"leftEdgeEffect", @"rightEdgeEffect" ];
    for (NSString *key in edgeKeys) {
        id effect = [scrollView valueForKey:key];
        if (!effect)
            continue;
        RNNScrollEdgeOptions *perEdge = RNNScrollEdgeOptionsForKey(options, key);

        Bool *hidden = (perEdge.hidden.hasValue) ? perEdge.hidden : options.hidden;
        Text *style = (perEdge.style.hasValue) ? perEdge.style : options.style;

        if (hidden.hasValue &&
            [effect respondsToSelector:NSSelectorFromString(@"setHidden:")]) {
            [effect setValue:@([hidden withDefault:NO]) forKey:@"hidden"];
        }
        if (style.hasValue &&
            [effect respondsToSelector:NSSelectorFromString(@"setStyle:")]) {
            [effect setValue:RNNScrollEdgeEffectStyleFromString(style.get) forKey:@"style"];
        }
    }
}

static void RNNApplyScrollEdgeEffectToView(UIView *view, RNNScrollEdgeEffectOptions *options) {
    if (!RNNScrollEdgeEffectHasAnyValue(options))
        return;
    if ([view isKindOfClass:[UIScrollView class]]) {
        RNNApplyScrollEdgeEffectToScrollView((UIScrollView *)view, options);
    }
    for (UIView *subview in view.subviews) {
        RNNApplyScrollEdgeEffectToView(subview, options);
    }
}

@implementation RNNComponentPresenter {
    TopBarTitlePresenter *_topBarTitlePresenter;
    RNNButtonsPresenter *_buttonsPresenter;
    BOOL _topBarButtonsAppliedBeforeShowing;
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

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault =
        [viewController.options withDefault:[self defaultOptions]];
    RNNApplyScrollEdgeEffectToView(viewController.view, withDefault.scrollEdgeEffect);
}

- (void)componentDidDisappear {
    [_topBarTitlePresenter componentDidDisappear];
    [_buttonsPresenter componentDidDisappear];
    _topBarButtonsAppliedBeforeShowing = NO;
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
                                                     withDefault:nil]
                                       placement:[withDefault.topBar.searchBar.placement
                                                     withDefault:SearchBarPlacementStacked]];
    }

    if (@available(iOS 26.0, *)) {
        if (!_topBarButtonsAppliedBeforeShowing) {
            if (withDefault.topBar.leftButtons) {
                [_buttonsPresenter applyLeftButtons:withDefault.topBar.leftButtons
                                       defaultColor:withDefault.topBar.leftButtonColor
                               defaultDisabledColor:withDefault.topBar.leftButtonDisabledColor
                                           animated:[withDefault.topBar.animateLeftButtons
                                                        withDefault:NO]];
            }
            if (withDefault.topBar.rightButtons) {
                [_buttonsPresenter
                       applyRightButtons:withDefault.topBar.rightButtons
                            defaultColor:withDefault.topBar.rightButtonColor
                    defaultDisabledColor:withDefault.topBar.rightButtonDisabledColor
                                animated:[withDefault.topBar.animateRightButtons withDefault:NO]];
            }
        }
        if (!_topBarButtonsAppliedBeforeShowing) {
            [_topBarTitlePresenter attachDeferredComponentTitleIfNeeded:withDefault.topBar
                                                            navigationBar:nil];
            [TopBarAppearancePresenter
                applySharedBackgroundSettingsForNavigationItem:viewController.navigationItem];
        }
    } else {
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

    [_topBarTitlePresenter applyOptions:withDefault.topBar];

    RNNApplyScrollEdgeEffectToView(viewController.view, withDefault.scrollEdgeEffect);
}

- (NSArray<RNNButtonOptions *> *)topBarComponentButtonsFromOptions:(RNNTopBarOptions *)topBar {
    NSMutableArray<RNNButtonOptions *> *componentButtons = [NSMutableArray new];
    for (RNNButtonOptions *button in topBar.leftButtons ?: @[]) {
        if (button.component.hasValue) {
            [componentButtons addObject:button];
        }
    }
    for (RNNButtonOptions *button in topBar.rightButtons ?: @[]) {
        if (button.component.hasValue) {
            [componentButtons addObject:button];
        }
    }
    return componentButtons;
}

- (void)preRenderTopBarComponentButtons:(RNNTopBarOptions *)topBar
                                perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    NSString *parentComponentId = self.boundViewController.layoutInfo.componentId;
    for (RNNButtonOptions *button in [self topBarComponentButtonsFromOptions:topBar]) {
        [self.componentRegistry createComponentIfNotExists:button.component
                                         parentComponentId:parentComponentId
                                             componentType:RNNComponentTypeTopBarButton
                                       reactViewReadyBlock:nil];
    }
    if (readyBlock) {
        readyBlock();
    }
}

- (void)applyTopBarButtonsBeforeShowing {
    if (_topBarButtonsAppliedBeforeShowing) {
        return;
    }

    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = viewController.resolveOptionsWithDefault;

    if (withDefault.topBar.leftButtons) {
        [_buttonsPresenter applyLeftButtons:withDefault.topBar.leftButtons
                               defaultColor:withDefault.topBar.leftButtonColor
                       defaultDisabledColor:withDefault.topBar.leftButtonDisabledColor
                                   animated:NO];
    }

    if (withDefault.topBar.rightButtons) {
        [_buttonsPresenter applyRightButtons:withDefault.topBar.rightButtons
                                defaultColor:withDefault.topBar.rightButtonColor
                        defaultDisabledColor:withDefault.topBar.rightButtonDisabledColor
                                    animated:NO];
    }

    [TopBarAppearancePresenter
        suppressSharedBackgroundForNavigationItem:viewController.navigationItem];
    _topBarButtonsAppliedBeforeShowing = YES;
}

- (void)prepareTopBarPlatterForPushTransition {
    if (!_topBarButtonsAppliedBeforeShowing) {
        [self applyTopBarButtonsBeforeShowing];
        return;
    }
    [TopBarAppearancePresenter
        suppressSharedBackgroundForNavigationItem:self.boundViewController.navigationItem];
}

- (void)attachTopBarTitleBeforePushUsingNavigationBar:(UINavigationBar *)navigationBar {
    if (!navigationBar || CGRectIsEmpty(navigationBar.bounds)) {
        return;
    }
    RNNNavigationOptions *withDefault = self.boundViewController.resolveOptionsWithDefault;
    [_topBarTitlePresenter attachDeferredComponentTitleIfNeeded:withDefault.topBar
                                                navigationBar:navigationBar];
}

- (void)finishTopBarAfterPushTransition {
    RNNComponentViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = viewController.resolveOptionsWithDefault;
    UINavigationItem *item = viewController.navigationItem;
    UINavigationBar *navigationBar = viewController.navigationController.navigationBar;

    [_topBarTitlePresenter attachDeferredComponentTitleIfNeeded:withDefault.topBar
                                                navigationBar:navigationBar];
    [TopBarAppearancePresenter applySharedBackgroundSettingsForNavigationItem:item];
    [TopBarAppearancePresenter flushDeferredBarButtonLayoutsForNavigationItem:item];
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

    if (@available(iOS 26.0, *)) {
        UIColor *barColor = [withDefault.topBar.background.color withDefault:nil];
        BOOL transparent = barColor.isTransparent;
        BOOL translucent = [withDefault.topBar.background.translucent withDefault:NO];
        [TopBarAppearancePresenter applyBackgroundToNavigationItem:viewController.navigationItem
                                                             color:barColor
                                                        transparent:transparent
                                                         translucent:translucent];
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
                                                     withDefault:nil]
                                       placement:[withDefault.topBar.searchBar.placement
                                                     withDefault:SearchBarPlacementStacked]];
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
        [_buttonsPresenter applyLeftButtonsColor:mergeOptions.topBar.leftButtonColor];
    }

    if (mergeOptions.topBar.rightButtonColor.hasValue) {
        [_buttonsPresenter applyRightButtonsColor:mergeOptions.topBar.rightButtonColor];
    }

    if (mergeOptions.topBar.rightButtonBackgroundColor.hasValue) {
        [_buttonsPresenter
            applyRightButtonsBackgroundColor:mergeOptions.topBar.rightButtonBackgroundColor];
    }

    if (mergeOptions.topBar.leftButtonBackgroundColor.hasValue) {
        [_buttonsPresenter
            applyLeftButtonsBackgroundColor:mergeOptions.topBar.leftButtonBackgroundColor];
    }

    if (mergeOptions.overlay.interceptTouchOutside.hasValue) {
        viewController.reactView.passThroughTouches =
            !mergeOptions.overlay.interceptTouchOutside.get;
    }

    [_topBarTitlePresenter mergeOptions:mergeOptions.topBar resolvedOptions:withDefault.topBar];

    if (RNNScrollEdgeEffectHasAnyValue(mergeOptions.scrollEdgeEffect)) {
        RNNApplyScrollEdgeEffectToView(viewController.view, mergeOptions.scrollEdgeEffect);
    }
}

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    if (@available(iOS 26.0, *)) {
        [self preRenderTopBarComponentButtons:withDefault.topBar
                                      perform:^{
                                        [self->_topBarTitlePresenter
                                            preCreateDeferredComponentTitleIfNeeded:withDefault.topBar];
                                        [self applyTopBarButtonsBeforeShowing];
                                        [self->_topBarTitlePresenter
                                            renderComponents:withDefault.topBar
                                                     perform:readyBlock];
                                      }];
        return;
    }
    [_topBarTitlePresenter renderComponents:withDefault.topBar perform:readyBlock];
}

- (void)dealloc {
    [self.componentRegistry clearComponentsForParentId:self.boundComponentId];
}
@end
