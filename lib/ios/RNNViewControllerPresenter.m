#import "RNNViewControllerPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "UITabBarController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNReactView.h"
#import "RNNCustomTitleView.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNViewControllerPresenter() {
	RNNReactView* _customTitleView;
	RNNTitleViewHelper* _titleViewHelper;
	RNNReactComponentRegistry* _componentRegistry;
}

@end

@implementation RNNViewControllerPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry {
	self = [self init];
	_componentRegistry = componentRegistry;
	return self;
}

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)bindedViewController {
	[super bindViewController:bindedViewController];
	_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self.bindedViewController componentRegistry:_componentRegistry];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UIViewController* viewController = self.bindedViewController;
	[viewController rnn_setBackgroundImage:[options.backgroundImage getWithDefaultValue:nil]];
	[viewController rnn_setNavigationItemTitle:[options.topBar.title.text getWithDefaultValue:nil]];
	[viewController rnn_setTopBarPrefersLargeTitle:[options.topBar.largeTitle.visible getWithDefaultValue:NO]];
	[viewController rnn_setTabBarItemBadgeColor:[options.bottomTab.badgeColor getWithDefaultValue:nil]];
	[viewController rnn_setStatusBarBlur:[options.statusBar.blur getWithDefaultValue:NO]];
	[viewController rnn_setStatusBarStyle:[options.statusBar.style getWithDefaultValue:@"default"] animated:[options.statusBar.animate getWithDefaultValue:YES]];
	[viewController rnn_setBackButtonVisible:[options.topBar.backButton.visible getWithDefaultValue:YES]];
	[viewController rnn_setInterceptTouchOutside:[options.overlay.interceptTouchOutside getWithDefaultValue:YES]];
	
	if (options.layout.backgroundColor.hasValue) {
		[viewController rnn_setBackgroundColor:options.layout.backgroundColor.get];
	}
	
	if (options.topBar.searchBar.hasValue) {
		BOOL hideNavBarOnFocusSearchBar = YES;
		if (options.topBar.hideNavBarOnFocusSearchBar.hasValue) {
			hideNavBarOnFocusSearchBar = options.topBar.hideNavBarOnFocusSearchBar.get;
		}
		[viewController rnn_setSearchBarWithPlaceholder:[options.topBar.searchBarPlaceholder getWithDefaultValue:@""] hideNavBarOnFocusSearchBar: hideNavBarOnFocusSearchBar];
	}
	
	[self setTitleViewWithSubtitle:options];
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
	[super applyOptionsOnInit:options];
	
	UIViewController* viewController = self.bindedViewController;
	[viewController rnn_setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:[options.modalPresentationStyle getWithDefaultValue:@"fullScreen"]]];
	[viewController rnn_setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:[options.modalTransitionStyle getWithDefaultValue:@"coverVertical"]]];
	[viewController rnn_setDrawBehindTopBar:[options.topBar.drawBehind getWithDefaultValue:NO]];
	[viewController rnn_setDrawBehindTabBar:[options.bottomTabs.drawBehind getWithDefaultValue:NO] || ![options.bottomTabs.visible getWithDefaultValue:YES]];
	
	if ((options.topBar.leftButtons || options.topBar.rightButtons)) {
		[_navigationButtons applyLeftButtons:options.topBar.leftButtons rightButtons:options.topBar.rightButtons defaultLeftButtonStyle:options.topBar.leftButtonStyle defaultRightButtonStyle:options.topBar.rightButtonStyle];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	UIViewController* viewController = self.bindedViewController;
	
	if (newOptions.backgroundImage.hasValue) {
		[viewController rnn_setBackgroundImage:newOptions.backgroundImage.get];
	}
	
	if (newOptions.modalPresentationStyle.hasValue) {
		[viewController rnn_setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:newOptions.modalPresentationStyle.get]];
	}
	
	if (newOptions.modalTransitionStyle.hasValue) {
		[viewController rnn_setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:newOptions.modalTransitionStyle.get]];
	}
	
	if (newOptions.topBar.searchBar.hasValue) {
		BOOL hideNavBarOnFocusSearchBar = YES;
		if (newOptions.topBar.hideNavBarOnFocusSearchBar.hasValue) {
			hideNavBarOnFocusSearchBar = newOptions.topBar.hideNavBarOnFocusSearchBar.get;
		}
		[viewController rnn_setSearchBarWithPlaceholder:[newOptions.topBar.searchBarPlaceholder getWithDefaultValue:@""] hideNavBarOnFocusSearchBar:hideNavBarOnFocusSearchBar];
	}
	
	if (newOptions.topBar.drawBehind.hasValue) {
		[viewController rnn_setDrawBehindTopBar:newOptions.topBar.drawBehind.get];
	}
	
	if (newOptions.topBar.title.text.hasValue) {
		[viewController rnn_setNavigationItemTitle:newOptions.topBar.title.text.get];
	}
	
	if (newOptions.topBar.largeTitle.visible.hasValue) {
		[viewController rnn_setTopBarPrefersLargeTitle:newOptions.topBar.largeTitle.visible.get];
	}
	
	if (newOptions.bottomTabs.drawBehind.hasValue) {
		[viewController rnn_setDrawBehindTabBar:newOptions.bottomTabs.drawBehind.get];
	}
	
	if (newOptions.bottomTab.badgeColor.hasValue) {
		[viewController rnn_setTabBarItemBadgeColor:newOptions.bottomTab.badgeColor.get];
	}
	
	if (newOptions.layout.backgroundColor.hasValue) {
		[viewController rnn_setBackgroundColor:newOptions.layout.backgroundColor.get];
	}
	
	if (newOptions.bottomTab.visible.hasValue) {
		[viewController.tabBarController rnn_setCurrentTabIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	if (newOptions.statusBar.blur.hasValue) {
		[viewController rnn_setStatusBarBlur:newOptions.statusBar.blur.get];
	}
	
	if (newOptions.statusBar.style.hasValue) {
		[viewController rnn_setStatusBarStyle:newOptions.statusBar.style.get animated:[newOptions.statusBar.animate getWithDefaultValue:YES]];
	}
	
	if (newOptions.topBar.backButton.visible.hasValue) {
		[viewController rnn_setBackButtonVisible:newOptions.topBar.backButton.visible.get];
	}
	
	if (newOptions.topBar.leftButtons || newOptions.topBar.rightButtons) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		[_navigationButtons applyLeftButtons:newOptions.topBar.leftButtons rightButtons:newOptions.topBar.rightButtons defaultLeftButtonStyle:buttonsResolvedOptions.topBar.leftButtonStyle defaultRightButtonStyle:buttonsResolvedOptions.topBar.rightButtonStyle];
	}
	
	if (newOptions.overlay.interceptTouchOutside.hasValue) {
		RCTRootView* rootView = (RCTRootView*)viewController.view;
		rootView.passThroughTouches = !newOptions.overlay.interceptTouchOutside.get;
	}
	
	[self setTitleViewWithSubtitle:(RNNNavigationOptions *)[[currentOptions overrideOptions:newOptions] mergeOptions:defaultOptions]];
	
	if (newOptions.topBar.title.component.name.hasValue) {
		[self setCustomNavigationTitleView:newOptions perform:nil];
	}
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	[self setCustomNavigationTitleView:options perform:readyBlock];
}

- (void)setCustomNavigationTitleView:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	UIViewController<RNNLayoutProtocol>* viewController = self.bindedViewController;
	if (![options.topBar.title.component.waitForRender getWithDefaultValue:NO] && readyBlock) {
		readyBlock();
		readyBlock = nil;
	}
	
	if (options.topBar.title.component.name.hasValue) {
		_customTitleView = (RNNReactView*)[_componentRegistry createComponentIfNotExists:options.topBar.title.component parentComponentId:viewController.layoutInfo.componentId reactViewReadyBlock:readyBlock];
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
	if (!_customTitleView && options.topBar.subtitle.text.hasValue) {
		_titleViewHelper = [[RNNTitleViewHelper alloc] initWithTitleViewOptions:options.topBar.title subTitleOptions:options.topBar.subtitle viewController:self.bindedViewController];
		[_titleViewHelper setup];
	} else if (_titleViewHelper) {
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
	[_componentRegistry clearComponentsForParentId:self.bindedComponentId];
}


@end
