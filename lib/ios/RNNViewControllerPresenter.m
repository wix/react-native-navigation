#import "RNNViewControllerPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "UITabBarController+RNNOptions.h"
#import "RNNNavigationButtons.h"
#import "RCTConvert+Modal.h"

@interface RNNViewControllerPresenter()
@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;
@end

@implementation RNNViewControllerPresenter


- (void)applyOptions:(RNNNavigationOptions *)initialOptions {
	[super applyOptions:initialOptions];
	RNNNavigationOptions* options = [initialOptions withDefault:self.defaultOptions];
	
	UIViewController* viewController = self.bindedViewController;
	[viewController rnn_setBackgroundImage:[options.backgroundImage getWithDefaultValue:nil]];
	[viewController rnn_setDrawBehindTopBar:[options.topBar.drawBehind getWithDefaultValue:NO]];
	[viewController rnn_setNavigationItemTitle:[options.topBar.title.text getWithDefaultValue:nil]];
	[viewController rnn_setTopBarPrefersLargeTitle:[options.topBar.largeTitle.visible getWithDefaultValue:NO]];
	[viewController rnn_setDrawBehindTabBar:[options.bottomTabs.drawBehind getWithDefaultValue:NO] || ![options.bottomTabs.visible getWithDefaultValue:YES]];
	[viewController rnn_setTabBarItemBadgeColor:[options.bottomTab.badgeColor getWithDefaultValue:nil]];
	[viewController rnn_setStatusBarBlur:[options.statusBar.blur getWithDefaultValue:NO]];
	[viewController rnn_setStatusBarStyle:[options.statusBar.style getWithDefaultValue:@"default"] animated:[options.statusBar.animate getWithDefaultValue:YES]];
	[viewController rnn_setBackButtonVisible:[options.topBar.backButton.visible getWithDefaultValue:YES]];

	if (options.layout.backgroundColor.hasValue) {
		[viewController rnn_setBackgroundColor:options.layout.backgroundColor.get];
	}
	
	if (options.topBar.searchBar.hasValue) {
		[viewController rnn_setSearchBarWithPlaceholder:[options.topBar.searchBarPlaceholder getWithDefaultValue:@""]];
	}
	
	if ((options.topBar.leftButtons || options.topBar.rightButtons) && !_navigationButtons) {
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:options.topBar.leftButtons rightButtons:options.topBar.rightButtons defaultLeftButtonStyle:options.topBar.leftButtonStyle defaultRightButtonStyle:options.topBar.rightButtonStyle];
	}
	
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
	[super applyOptionsOnInit:options];
	
	UIViewController* viewController = self.bindedViewController;
	[viewController rnn_setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:[options.modalPresentationStyle getWithDefaultValue:@"fullScreen"]]];
	[viewController rnn_setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:[options.modalTransitionStyle getWithDefaultValue:@"coverVertical"]]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
	[super mergeOptions:options resolvedOptions:resolvedOptions];
	
	RNNNavigationOptions* withDefault = (RNNNavigationOptions *)[options withDefault:self.defaultOptions];
	
	UIViewController* viewController = self.bindedViewController;
	
	if (options.backgroundImage.hasValue) {
		[viewController rnn_setBackgroundImage:options.backgroundImage.get];
	}
	
	if (options.modalPresentationStyle.hasValue) {
		[viewController rnn_setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:options.modalPresentationStyle.get]];
	}
	
	if (options.modalTransitionStyle.hasValue) {
		[viewController rnn_setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:options.modalTransitionStyle.get]];
	}
	
	if (options.topBar.searchBar.hasValue) {
		[viewController rnn_setSearchBarWithPlaceholder:[withDefault.topBar.searchBarPlaceholder getWithDefaultValue:@""]];
	}
	
	if (options.topBar.drawBehind.hasValue) {
		[viewController rnn_setDrawBehindTopBar:options.topBar.drawBehind.get];
	}
	
	if (options.topBar.title.text.hasValue) {
		[viewController rnn_setNavigationItemTitle:options.topBar.title.text.get];
	}
	
	if (options.topBar.largeTitle.visible.hasValue) {
		[viewController rnn_setTopBarPrefersLargeTitle:options.topBar.largeTitle.visible.get];
	}
	
	if (options.bottomTabs.drawBehind.hasValue) {
		[viewController rnn_setDrawBehindTabBar:options.bottomTabs.drawBehind.get];
	}
	
	if (options.bottomTab.badgeColor.hasValue) {
		[viewController rnn_setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
	}
	
	if (options.layout.backgroundColor.hasValue) {
		[viewController rnn_setBackgroundColor:options.layout.backgroundColor.get];
	}
	
	if (options.bottomTab.visible.hasValue) {
		[viewController.tabBarController rnn_setCurrentTabIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	if (options.statusBar.blur.hasValue) {
		[viewController rnn_setStatusBarBlur:options.statusBar.blur.get];
	}
	
	if (options.statusBar.style.hasValue) {
		[viewController rnn_setStatusBarStyle:options.statusBar.style.get animated:[withDefault.statusBar.animate getWithDefaultValue:YES]];
	}
	
	if (options.topBar.backButton.visible.hasValue) {
		[viewController rnn_setBackButtonVisible:options.topBar.backButton.visible.get];
	}
	
	if (options.topBar.leftButtons || options.topBar.rightButtons) {
		RNNNavigationOptions* buttonsResolvedOptions = (RNNNavigationOptions *)[[resolvedOptions overrideOptions:options] withDefault:self.defaultOptions];
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:options.topBar.leftButtons rightButtons:options.topBar.rightButtons defaultLeftButtonStyle:buttonsResolvedOptions.topBar.leftButtonStyle defaultRightButtonStyle:buttonsResolvedOptions.topBar.rightButtonStyle];
	}
}

@end
