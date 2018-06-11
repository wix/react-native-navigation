#import "RNNBottomTabOptions.h"

@implementation RNNBottomTabOptions

-(instancetype)initWithDict:(NSDictionary *)tabItemDict {
	self = [super init];
	
	[self mergeWith:tabItemDict];
	self.tag = [tabItemDict[@"tag"] integerValue];
	
	return self;
}

- (void)applyOn:(UIViewController *)viewController {
	if (self.title || self.icon) {
		UIImage *iconImage = nil;
		if (self.disableIconTint) {
			iconImage = [[RCTConvert UIImage:self.icon] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			iconImage = [RCTConvert UIImage:self.icon];
		}
		UITabBarItem* tabItem = [[UITabBarItem alloc] initWithTitle:self.title image:iconImage tag:self.tag];
		tabItem.accessibilityIdentifier = self.testID;
		
		if (self.iconInsets && ![self.iconInsets isKindOfClass:[NSNull class]]) {
			id topInset = self.iconInsets[@"top"];
			id leftInset = self.iconInsets[@"left"];
			id bottomInset = self.iconInsets[@"bottom"];
			id rightInset = self.iconInsets[@"right"];
			
			CGFloat top = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:topInset] : 0;
			CGFloat left = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:leftInset] : 0;
			CGFloat bottom = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:bottomInset] : 0;
			CGFloat right = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:rightInset] : 0;
			
			tabItem.imageInsets = UIEdgeInsetsMake(top, left, bottom, right);
		}
		
		[viewController.navigationController setTabBarItem:tabItem];
	}
	if (self.selectedIcon) {
		UIImage *selectedIconImage = nil;
		if (self.disableSelectedIconTint) {
			selectedIconImage = [[RCTConvert UIImage:self.selectedIcon] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			selectedIconImage = [RCTConvert UIImage:self.selectedIcon];
		}
		if (viewController.navigationController) {
			viewController.navigationController.tabBarItem.selectedImage = selectedIconImage;
		} else {
			viewController.tabBarItem.selectedImage = selectedIconImage;
		}	
	}
	
	if (self.badge) {
		NSString *badge = nil;
		if (self.badge != nil && ![self.badge isEqual:[NSNull null]]) {
			badge = [RCTConvert NSString:self.badge];
		}
		if (viewController.navigationController) {
			viewController.navigationController.tabBarItem.badgeValue = badge;
		} else {
			viewController.tabBarItem.badgeValue = badge;
		}
	}
	
	if (self.visible) {
		[viewController.tabBarController setSelectedIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	[self resetOptions];
}

-(void)resetOptions {
	self.title = nil;
	self.badge = nil;
	self.visible = nil;
	self.icon = nil;
	self.testID = nil;
	self.iconInsets = nil;
}

@end
