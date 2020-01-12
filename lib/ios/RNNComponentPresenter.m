#import "RNNComponentPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "UITabBarController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+LayoutProtocol.h"
#import "RNNReactTitleView.h"

@interface RNNComponentPresenter() {
	RNNReactTitleView* _customTitleView;
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

- (void)bindViewController:(id)boundViewController {
	[super bindViewController:boundViewController];
	_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self.boundViewController componentRegistry:_componentRegistry];
}

- (void)componentDidAppear {
    RNNReactView* component = (RNNReactView *)self.boundViewController.view;
    if ([component respondsToSelector:@selector(componentDidAppear)]) {
        [component componentDidAppear];
    }
    [_customTitleView componentDidAppear];
    [_navigationButtons componentDidAppear];
}

- (void)componentDidDisappear {
    RNNReactView* component = (RNNReactView *)self.boundViewController.view;
    if ([component respondsToSelector:@selector(componentDidDisappear)]) {
        [component componentDidDisappear];
    }
    
    [_customTitleView componentDidDisappear];
    [_navigationButtons componentDidDisappear];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	[super applyOptionsOnWillMoveToParentViewController:options];
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
	[self removeTitleComponentIfNeeded:options];

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

	if (options.topBar.title.component.name.hasValue) {
		[self setCustomNavigationTitleView:options perform:nil];
	}

	[self setTitleViewWithSubtitle:withDefault];
}

- (void)removeTitleComponentIfNeeded:(RNNNavigationOptions *)options {
	if (options.topBar.title.text.hasValue) {
        [_customTitleView componentDidDisappear];
		[_customTitleView removeFromSuperview];
		_customTitleView = nil;
	}
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	[self setCustomNavigationTitleView:withDefault perform:readyBlock];
}

- (void)setCustomNavigationTitleView:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	UIViewController<RNNLayoutProtocol>* viewController = self.boundViewController;
	if (![options.topBar.title.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
		readyBlock();
		readyBlock = nil;
	}
	
	if (options.topBar.title.component.name.hasValue) {
        _customTitleView = (RNNReactTitleView *)[_componentRegistry createComponentIfNotExists:options.topBar.title.component parentComponentId:viewController.layoutInfo.componentId componentType:RNNComponentTypeTopBarTitle reactViewReadyBlock:readyBlock];
		_customTitleView.backgroundColor = UIColor.clearColor;
		NSString* alignment = [options.topBar.title.component.alignment getWithDefaultValue:@""];
		[_customTitleView setAlignment:alignment inFrame:viewController.navigationController.navigationBar.frame];
		[_customTitleView layoutIfNeeded];
		
		viewController.navigationItem.titleView = nil;
		viewController.navigationItem.titleView = _customTitleView;
        [_customTitleView componentDidAppear];
	} else {
		[_customTitleView removeFromSuperview];
		if (readyBlock) {
			readyBlock();
		}
	}
}

- (void)setTitleViewWithSubtitle:(RNNNavigationOptions *)options {
	if (!_customTitleView && ![options.topBar.largeTitle.visible getWithDefaultValue:NO]) {
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
