#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNNavigationButtons.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"

@implementation RNNNavigationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.topBar = [[RNNTopBarOptions alloc] initWithDict:dict[@"topBar"]];
	self.bottomTabs = [[RNNBottomTabsOptions alloc] initWithDict:dict[@"bottomTabs"]];
	self.bottomTab = [[RNNBottomTabOptions alloc] initWithDict:dict[@"bottomTab"]];
	self.topTabs = [[RNNTopTabsOptions alloc] initWithDict:dict[@"topTabs"]];
	self.topTab = [[RNNTopTabOptions alloc] initWithDict:dict[@"topTab"]];
	self.sideMenu = [[RNNSideMenuOptions alloc] initWithDict:dict[@"sideMenu"]];
	self.overlay = [[RNNOverlayOptions alloc] initWithDict:dict[@"overlay"]];
	self.customTransition = [[RNNAnimationOptions alloc] initWithDict:dict[@"customTransition"]];
	self.animations = [[RNNTransitionsOptions alloc] initWithDict:dict[@"animations"]];
	self.statusBar = [[RNNStatusBarOptions alloc] initWithDict:dict[@"statusBar"]];
	self.preview = [[RNNPreviewOptions alloc] initWithDict:dict[@"preview"]];
	self.layout = [[RNNLayoutOptions alloc] initWithDict:dict[@"layout"]];
	
	self.popGesture = [[Bool alloc] initWithValue:dict[@"popGesture"]];
	self.backgroundImage = [[Dictionary alloc] initWithValue:dict[@"backgroundImage"]];
	self.rootBackgroundImage = [[Dictionary alloc] initWithValue:dict[@"rootBackgroundImage"]];
	self.modalPresentationStyle = [[String alloc] initWithValue:dict[@"modalPresentationStyle"]];
	self.modalTransitionStyle = [[String alloc] initWithValue:dict[@"modalTransitionStyle"]];
	
	return self;
}

- (instancetype)initEmptyOptions {
	self = [self initWithDict:@{}];
	return self;
}

- (void)applyDefaultOptionsOn:(UIViewController *)viewController {
	UITabBarController* tabBarController = viewController.tabBarController;
	UINavigationController* navigationController = viewController.navigationController;
	
	[navigationController setNavigationBarHidden:NO animated:NO];
	navigationController.hidesBarsOnSwipe = NO;
	navigationController.navigationBar.barStyle = UIBarStyleDefault;
	navigationController.navigationBar.translucent = NO;
	navigationController.navigationBar.clipsToBounds = NO;
	
	tabBarController.tabBar.barTintColor = nil;
	tabBarController.tabBar.barStyle = UIBarStyleDefault;
	tabBarController.tabBar.translucent = NO;
}

//- (void)resetOptions {
//	[self.sideMenu resetOptions];
//	[self.bottomTabs resetOptions];
//	[self.bottomTab resetOptions];
//}

@end
