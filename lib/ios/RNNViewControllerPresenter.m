#import "RNNViewControllerPresenter.h"
#import "RNNSideMenuController.h"
#import "UIViewController+SideMenuController.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"
#import "UITabBarController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"
#import "RNNNavigationController.h"
#import "RNNNavigationButtons.h"

@implementation RNNViewControllerPresenter

- (void)bindViewController:(UIViewController *)bindedViewController {
	_bindedViewController = bindedViewController;
}

- (void)presentOnWillMoveToParent:(RNNNavigationOptions *)options {
	[options applyDefaultOptionsOn:self.bindedViewController];
}

- (void)presentOnViewWillAppear:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;
	UITabBarController* tabBarController = viewController.tabBarController;
	RNNNavigationController* navigationController = (RNNNavigationController *)viewController.navigationController;
	RNNSideMenuController* sideMenuController = viewController.sideMenuController;
	
	if (viewController) {
		[self present:options onViewController:viewController];
	}
	
	if (tabBarController) {
		[self present:options onTabBarController:tabBarController];
	}
	
	if (navigationController) {
		[self present:options onNavigationController:navigationController];
	}
	
	if (sideMenuController) {
		[self present:options onSideMenuController:sideMenuController];
	}
}

- (void)present:(RNNNavigationOptions *)options onViewController:(UIViewController *)viewController {
	if (options.backgroundImage.hasValue) {
		[viewController rnn_setBackgroundImage:[RCTConvert UIImage:options.backgroundImage.get]];
	}
	
	if (options.modalPresentationStyle.hasValue) {
		[viewController rnn_setModalPresentationStyle:[RCTConvert UIModalPresentationStyle:options.modalPresentationStyle.get]];
	}
	
	if (options.modalTransitionStyle.hasValue) {
		[viewController rnn_setModalTransitionStyle:[RCTConvert UIModalTransitionStyle:options.modalTransitionStyle.get]];
	}
	
	if (options.topBar.searchBar.hasValue) {
		[viewController rnn_setSearchBarWithPlaceholder:[options.topBar.searchBarPlaceholder getWithDefaultValue:@""]];
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
	
	if (options.bottomTab.badge.hasValue) {
		[viewController rnn_setTabBarItemBadge:[RCTConvert NSString:options.bottomTab.badge.get]];
	}
	
	if (options.bottomTab.badgeColor.hasValue) {
		[viewController rnn_setTabBarItemBadgeColor:[RCTConvert UIColor:options.bottomTab.badgeColor.get]];
	}
	
	if (options.layout.backgroundColor.hasValue) {
		[viewController rnn_setBackgroundColor:[RCTConvert UIColor:options.layout.backgroundColor.get]];
	}
	
	if (options.bottomTab.visible.hasValue) {
		[viewController.tabBarController rnn_setCurrentTabIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	if (options.statusBar.blur.hasValue) {
		[viewController rnn_setStatusBarBlur:options.statusBar.blur.get];
	}
	
	if (options.statusBar.style.hasValue) {
		[viewController rnn_setStatusBarStyle:options.statusBar.style.get animated:[options.statusBar.animate getWithDefaultValue:@(1)]];
	}
	
	if (options.topBar.backButton.visible.hasValue) {
		[viewController rnn_setBackButtonVisible:options.topBar.backButton.visible.get];
	}
	
	UIViewController* topViewController = [self getTabControllerFirstChild:viewController];
	if (options.bottomTab.text.hasValue || options.bottomTab.icon.hasValue || options.bottomTab.selectedIcon.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		[topViewController setTabBarItem:tabItem];
		viewController.tabBarItem = tabItem;
	}
	
	[viewController.navigationController rnn_setNavigationBarFontFamily:[options.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.title.fontSize getWithDefaultValue:nil] color:[RCTConvert UIColor:[options.topBar.title.color getWithDefaultValue:nil]]];
	
	if (options.topBar.leftButtons || options.topBar.rightButtons) {
		RNNNavigationButtons* navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[navigationButtons applyLeftButtons:options.topBar.leftButtons rightButtons:options.topBar.rightButtons defaultLeftButtonStyle:options.topBar.leftButtonStyle defaultRightButtonStyle:options.topBar.rightButtonStyle];
	}
}

- (void)present:(RNNNavigationOptions *)options onNavigationController:(RNNNavigationController *)navigationController {
	if (options.popGesture.hasValue) {
		[navigationController rnn_setInteractivePopGestureEnabled:options.popGesture.get];
	}
	
	if (options.rootBackgroundImage.hasValue) {
		[navigationController rnn_setRootBackgroundImage:[RCTConvert UIImage:options.rootBackgroundImage.get]];
	}
	
	if (options.topBar.testID.hasValue) {
		[navigationController rnn_setNavigationBarTestID:options.topBar.testID.get];
	}
	
	if (options.topBar.visible.hasValue) {
		[navigationController rnn_setNavigationBarVisible:options.topBar.visible.get animated:[options.topBar.animate getWithDefaultValue:(@1)]];
		[options.topBar.visible consume];
	}
	
	if (options.topBar.hideOnScroll.hasValue) {
		[navigationController rnn_hideBarsOnScroll:[options.topBar.hideOnScroll get]];
	}
	
	if (options.topBar.noBorder.hasValue) {
		[navigationController rnn_setNavigationBarNoBorder:[options.topBar.noBorder get]];
	}
	
	if (options.topBar.barStyle.hasValue) {
		[navigationController rnn_setBarStyle:[RCTConvert UIBarStyle:options.topBar.barStyle.get]];
	}
	
	if (options.topBar.background.translucent.hasValue) {
		[navigationController rnn_setNavigationBarTranslucent:[options.topBar.background.translucent get]];
	}
	
	if (options.topBar.background.clipToBounds.hasValue) {
		[navigationController rnn_setNavigationBarClipsToBounds:[options.topBar.background.clipToBounds get]];
	}
	
	if (options.topBar.background.blur.hasValue) {
		[navigationController rnn_setNavigationBarBlur:[options.topBar.background.blur get]];
	}
	
	if (options.topBar.background.color.hasValue) {
		[navigationController setTopBarBackgroundColor:[RCTConvert UIColor:options.topBar.background.color.get]];
	}
	
	[navigationController rnn_setNavigationBarLargeTitleVisible:options.topBar.largeTitle.visible.get fontFamily:[options.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[RCTConvert UIColor:[options.topBar.largeTitle.color getWithDefaultValue:nil]]];

	
	[navigationController rnn_setBackButtonIcon:[RCTConvert UIImage:[options.topBar.backButton.icon getWithDefaultValue:nil]] withColor:[RCTConvert UIColor:[options.topBar.backButton.color getWithDefaultValue:nil]] title:[options.topBar.backButton.showTitle getWithDefaultValue:nil] ? options.topBar.backButton.title.get : @""];
}

- (void)present:(RNNNavigationOptions *)options onSideMenuController:(RNNSideMenuController *)sideMenuController {
	if (options.sideMenu.left.enabled) {
		[sideMenuController side:MMDrawerSideLeft enabled:options.sideMenu.left.enabled];
	}
	
	if (options.sideMenu.right.enabled) {
		[sideMenuController side:MMDrawerSideRight enabled:options.sideMenu.right.enabled];
	}
	
	if (options.sideMenu.left.visible) {
		[sideMenuController side:MMDrawerSideLeft visible:options.sideMenu.left.visible];
	}
	
	if (options.sideMenu.right.visible) {
		[sideMenuController side:MMDrawerSideRight visible:options.sideMenu.right.visible];
	}
	
	if (options.sideMenu.left.width) {
		[sideMenuController side:MMDrawerSideLeft visible:options.sideMenu.left.width];
	}
	
	if (options.sideMenu.right.width) {
		[sideMenuController side:MMDrawerSideRight visible:options.sideMenu.right.width];
	}
	
	if (options.sideMenu.right.shouldStretchDrawer) {
		sideMenuController.shouldStretchDrawer = options.sideMenu.right.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.right.animationVelocity) {
		sideMenuController.animationVelocity = [options.sideMenu.right.animationVelocity.get doubleValue];
	}
	
	if (options.sideMenu.left.shouldStretchDrawer) {
		sideMenuController.shouldStretchDrawer = options.sideMenu.left.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.left.animationVelocity) {
		sideMenuController.animationVelocity = [options.sideMenu.left.animationVelocity.get doubleValue];
	}
	
}

- (void)present:(RNNNavigationOptions *)options onTabBarController:(UITabBarController *)tabBarController {
	if (options.bottomTabs.currentTabIndex.hasValue) {
		[tabBarController rnn_setCurrentTabIndex:[options.bottomTabs.currentTabIndex.get unsignedIntegerValue]];
	}
	
	if (options.bottomTabs.currentTabId.hasValue) {
		[tabBarController rnn_setCurrentTabID:options.bottomTabs.currentTabId.get];
	}
	
	if (options.bottomTabs.testID.hasValue) {
		[tabBarController rnn_setTabBarTestID:options.bottomTabs.testID.get];
	}
	
	if (options.bottomTabs.backgroundColor.hasValue) {
		[tabBarController rnn_setTabBarBackgroundColor:[RCTConvert UIColor:options.bottomTabs.backgroundColor.get]];
	}
	
	if (options.bottomTabs.barStyle.hasValue) {
		[tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:options.bottomTabs.barStyle.get]];
	}
	
	if (options.bottomTabs.translucent.hasValue) {
		[tabBarController rnn_setTabBarTranslucent:options.bottomTabs.translucent.get];
	}
	
	if (options.bottomTabs.hideShadow.hasValue) {
		[tabBarController rnn_setTabBarHideShadow:options.bottomTabs.hideShadow.get];
	}
}

- (UIViewController *)getTabControllerFirstChild:(UIViewController *)viewController {
	while (viewController != nil) {
		if ([viewController.parentViewController isKindOfClass:[UITabBarController class]] || !viewController.parentViewController) {
			return viewController;
		}
		
		viewController = viewController.parentViewController;
	}
	
	return nil;
}

@end
