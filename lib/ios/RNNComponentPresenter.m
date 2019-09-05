#import "RNNComponentPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "UITabBarController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNComponentPresenter() {
	RNNReactView* _customTitleView;
	RNNTitleViewHelper* _titleViewHelper;
	RNNReactComponentRegistry* _componentRegistry;
}

@end

@implementation RNNComponentPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry:(RNNNavigationOptions *)defaultOptions {
	self = [self initWithDefaultOptions:defaultOptions];
	_componentRegistry = componentRegistry;
	return self;
}

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)bindedViewController {
	[super bindViewController:bindedViewController];
	_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self.boundViewController componentRegistry:_componentRegistry];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	[super applyOptionsOnWillMoveToParentViewController:options];
	UIViewController* viewController = self.boundViewController;
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	[viewController setBackButtonIcon:[withDefault.topBar.backButton.icon getWithDefaultValue:nil] withColor:[withDefault.topBar.backButton.color getWithDefaultValue:nil] title:[withDefault.topBar.backButton.showTitle getWithDefaultValue:YES] ? [withDefault.topBar.backButton.title getWithDefaultValue:nil] : @""];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UIViewController* viewController = self.boundViewController;
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	[viewController setBackgroundImage:[withDefault.backgroundImage getWithDefaultValue:nil]];
	[viewController setNavigationItemTitle:[withDefault.topBar.title.text getWithDefaultValue:nil]];
	[viewController setTopBarPrefersLargeTitle:[withDefault.topBar.largeTitle.visible getWithDefaultValue:NO]];
	[viewController setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor getWithDefaultValue:nil]];
	[viewController setStatusBarBlur:[withDefault.statusBar.blur getWithDefaultValue:NO]];
	[viewController setStatusBarStyle:[withDefault.statusBar.style getWithDefaultValue:@"default"] animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
	[viewController setBackButtonVisible:[withDefault.topBar.backButton.visible getWithDefaultValue:YES]];
	[viewController setInterceptTouchOutside:[withDefault.overlay.interceptTouchOutside getWithDefaultValue:YES]];
	
	if (withDefault.layout.backgroundColor.hasValue) {
		[viewController setBackgroundColor:withDefault.layout.backgroundColor.get];
	}
	
	if (withDefault.topBar.searchBar.hasValue) {
		BOOL hideNavBarOnFocusSearchBar = YES;
		if (withDefault.topBar.hideNavBarOnFocusSearchBar.hasValue) {
			hideNavBarOnFocusSearchBar = withDefault.topBar.hideNavBarOnFocusSearchBar.get;
		}
		[viewController setSearchBarWithPlaceholder:[withDefault.topBar.searchBarPlaceholder getWithDefaultValue:@""] hideNavBarOnFocusSearchBar:hideNavBarOnFocusSearchBar];
	}
	
	[self setTitleViewWithSubtitle:withDefault];
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
	[super applyOptionsOnInit:options];
	
	UIViewController* viewController = self.boundViewController;
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	[viewController setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:[withDefault.modalPresentationStyle getWithDefaultValue:@"fullScreen"]]];
	[viewController setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:[withDefault.modalTransitionStyle getWithDefaultValue:@"coverVertical"]]];
	[viewController setDrawBehindTopBar:[withDefault.topBar.drawBehind getWithDefaultValue:NO]];
	[viewController setDrawBehindTabBar:[withDefault.bottomTabs.drawBehind getWithDefaultValue:NO] || ![withDefault.bottomTabs.visible getWithDefaultValue:YES]];
	
	if ((withDefault.topBar.leftButtons || withDefault.topBar.rightButtons)) {
		[_navigationButtons applyLeftButtons:withDefault.topBar.leftButtons rightButtons:withDefault.topBar.rightButtons defaultLeftButtonStyle:withDefault.topBar.leftButtonStyle defaultRightButtonStyle:withDefault.topBar.rightButtonStyle];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
	RNNNavigationOptions * withDefault	= (RNNNavigationOptions *) [[currentOptions overrideOptions:options] withDefault:[self defaultOptions]];

	UIViewController* viewController = self.boundViewController;
	
	if (options.backgroundImage.hasValue) {
		[viewController setBackgroundImage:options.backgroundImage.get];
	}
	
	if (options.modalPresentationStyle.hasValue) {
		[viewController setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:options.modalPresentationStyle.get]];
	}
	
	if (options.modalTransitionStyle.hasValue) {
		[viewController setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:options.modalTransitionStyle.get]];
	}
	
	if (options.topBar.searchBar.hasValue) {
		BOOL hideNavBarOnFocusSearchBar = YES;
		if (options.topBar.hideNavBarOnFocusSearchBar.hasValue) {
			hideNavBarOnFocusSearchBar = options.topBar.hideNavBarOnFocusSearchBar.get;
		}
		[viewController setSearchBarWithPlaceholder:[options.topBar.searchBarPlaceholder getWithDefaultValue:@""] hideNavBarOnFocusSearchBar:hideNavBarOnFocusSearchBar];
	}
	
	if (options.topBar.drawBehind.hasValue) {
		[viewController setDrawBehindTopBar:options.topBar.drawBehind.get];
	}
	
	if (options.topBar.title.text.hasValue) {
		[viewController setNavigationItemTitle:options.topBar.title.text.get];
	}
	
	if (options.topBar.largeTitle.visible.hasValue) {
		[viewController setTopBarPrefersLargeTitle:options.topBar.largeTitle.visible.get];
	}
	
	if (options.bottomTabs.drawBehind.hasValue) {
		[viewController setDrawBehindTabBar:options.bottomTabs.drawBehind.get];
	}
	
	if (options.bottomTab.badgeColor.hasValue) {
		[viewController setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
	}
	
	if (options.layout.backgroundColor.hasValue) {
		[viewController setBackgroundColor:options.layout.backgroundColor.get];
	}
	
	if (options.bottomTab.visible.hasValue) {
		[viewController.tabBarController setCurrentTabIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	if (options.statusBar.blur.hasValue) {
		[viewController setStatusBarBlur:options.statusBar.blur.get];
	}
	
	if (options.statusBar.style.hasValue) {
		[viewController setStatusBarStyle:options.statusBar.style.get animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
	}
	
	if (options.topBar.backButton.visible.hasValue) {
		[viewController setBackButtonVisible:options.topBar.backButton.visible.get];
	}
	
	if (options.topBar.leftButtons || options.topBar.rightButtons) {
		[_navigationButtons applyLeftButtons:options.topBar.leftButtons rightButtons:options.topBar.rightButtons defaultLeftButtonStyle:withDefault.topBar.leftButtonStyle defaultRightButtonStyle:withDefault.topBar.rightButtonStyle];
	}
	

	if (options.overlay.interceptTouchOutside.hasValue) {
		RCTRootView* rootView = (RCTRootView*)viewController.view;
		rootView.passThroughTouches = !options.overlay.interceptTouchOutside.get;
	}

	[self setTitleViewWithSubtitle:withDefault];

	if (options.topBar.title.component.name.hasValue) {
		[self setCustomNavigationTitleView:options perform:nil];
	} else {
		[_customTitleView removeFromSuperview];
		_customTitleView = nil;
	}

	if (options.topBar.backButton.hasValue) {
		UIViewController *lastViewControllerInStack = viewController.navigationController.viewControllers.count > 1 ? viewController.navigationController.viewControllers[viewController.navigationController.viewControllers.count - 2] : viewController.navigationController.topViewController;
	    RNNNavigationOptions * resolvedOptions	= (RNNNavigationOptions *) [[currentOptions overrideOptions:options] withDefault:[self defaultOptions]];
		[lastViewControllerInStack applyBackButton:resolvedOptions.topBar.backButton];
	}
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	[self setCustomNavigationTitleView:options perform:readyBlock];
}

- (void)setCustomNavigationTitleView:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	UIViewController<RNNLayoutProtocol>* viewController = self.boundViewController;
	if (![options.topBar.title.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
		readyBlock();
		readyBlock = nil;
	}
	
	if (options.topBar.title.component.name.hasValue) {
		_customTitleView = [_componentRegistry createComponentIfNotExists:options.topBar.title.component parentComponentId:viewController.layoutInfo.componentId reactViewReadyBlock:readyBlock];
		_customTitleView.backgroundColor = UIColor.clearColor;
		NSString* alignment = [options.topBar.title.component.alignment getWithDefaultValue:@""];
		[_customTitleView setAlignment:alignment inFrame:viewController.navigationController.navigationBar.frame];

		viewController.navigationItem.titleView = nil;
		viewController.navigationItem.titleView = _customTitleView;
	} else {
		[_customTitleView removeFromSuperview];
		if (readyBlock) {
			readyBlock();
		}
	}
}

- (void)setTitleViewWithSubtitle:(RNNNavigationOptions *)options {
	if (!_customTitleView) {
		_titleViewHelper = [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.topBar.title subTitleOptions:options.topBar.subtitle viewController:self.boundViewController];

		if (options.topBar.title.text.hasValue) {
			[_titleViewHelper setTitleOptions:options.topBar.title];
		}
		if (options.topBar.subtitle.text.hasValue) {
			[_titleViewHelper setSubtitleOptions:options.topBar.subtitle];
		}

		[_titleViewHelper setup];
	} else {
		_titleViewHelper = [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.topBar.title subTitleOptions:options.topBar.subtitle viewController:self.boundViewController];
		
		if (options.topBar.title.text.hasValue) {
			[_titleViewHelper setTitleOptions:options.topBar.title];
		}
		if (options.topBar.subtitle.text.hasValue) {
			[_titleViewHelper setSubtitleOptions:options.topBar.subtitle];
		}
		
		[_titleViewHelper setup];
	}
}

- (void)dealloc {
	[_componentRegistry clearComponentsForParentId:self.boundComponentId];
}
@end
