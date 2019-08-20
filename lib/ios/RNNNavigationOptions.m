#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNNavigationButtons.h"
#import "RNNSplitViewOptions.h"
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
	self.splitView = [[RNNSplitViewOptions alloc] initWithDict:dict[@"splitView"]];
	self.overlay = [[RNNOverlayOptions alloc] initWithDict:dict[@"overlay"]];
	self.customTransition = [[RNNSharedElementAnimationOptions alloc] initWithDict:dict[@"customTransition"]];
	self.animations = [[RNNAnimationsOptions alloc] initWithDict:dict[@"animations"]];
	self.statusBar = [[RNNStatusBarOptions alloc] initWithDict:dict[@"statusBar"]];
	self.preview = [[RNNPreviewOptions alloc] initWithDict:dict[@"preview"]];
	self.layout = [[RNNLayoutOptions alloc] initWithDict:dict[@"layout"]];
	
	self.popGesture = [[Bool alloc] initWithValue:dict[@"popGesture"]];
	
	self.backgroundImage = [ImageParser parse:dict key:@"backgroundImage"];
	self.rootBackgroundImage = [ImageParser parse:dict key:@"rootBackgroundImage"];
	self.modalPresentationStyle = [[Text alloc] initWithValue:dict[@"modalPresentationStyle"]];
	self.modalTransitionStyle = [[Text alloc] initWithValue:dict[@"modalTransitionStyle"]];
	
	return self;
}

- (instancetype)initEmptyOptions {
	self = [self initWithDict:@{}];
	return self;
}

- (RNNNavigationOptions *)copy {
	RNNNavigationOptions* newOptions = [[RNNNavigationOptions alloc] initWithDict:@{}];
	[newOptions overrideOptions:self];
	
	return newOptions;
}

- (RNNNavigationOptions *)withDefault:(RNNNavigationOptions *)defaultOptions {
	return (RNNNavigationOptions *)[super withDefault:defaultOptions];
}

@end
