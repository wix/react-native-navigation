#import "RNNBottomTabOptions.h"
#import "UIImage+tint.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"

@implementation RNNBottomTabOptions

-(instancetype)initWithDict:(NSDictionary *)tabItemDict {
	self = [super init];
	
	[self mergeWith:tabItemDict];
	self.tag = [tabItemDict[@"tag"] integerValue];
	
	return self;
}

- (void)applyOn:(UIViewController *)viewController {
	UIViewController* topViewController = [self getTabControllerFirstChild:viewController];
	if (self.text || self.icon || self.selectedIcon) {
		
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:topViewController.tabBarItem text:self.text textColor:self.textColor selectedTextColor:self.selectedTextColor icon:self.icon selectedIcon:self.selectedIcon iconInsets:self.iconInsets iconColor:self.iconColor selectedIconColor:self.selectedIconColor fontFamily:self.fontFamily fontSize:self.fontSize];
		
		tabItem.tag = self.tag;
		tabItem.accessibilityIdentifier = self.testID;
		
		[topViewController setTabBarItem:tabItem];
	}
	
	if (self.badge) {
		[topViewController rnn_setTabBarItemBadge:[RCTConvert NSString:self.badge]];
	}
	
	if (self.badgeColor) {
		[topViewController rnn_setTabBarItemBadgeColor:[RCTConvert UIColor:self.badgeColor]];
	}
	
	if (self.visible) {
		[topViewController.tabBarController rnn_setCurrentTabIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	[self resetOptions];
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

-(void)resetOptions {
	self.text = nil;
	self.badge = nil;
	self.visible = nil;
	self.icon = nil;
	self.testID = nil;
	self.iconInsets = nil;
	self.selectedIcon = nil;
}

@end
