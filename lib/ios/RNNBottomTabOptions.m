#import "RNNBottomTabOptions.h"
#import "UIImage+tint.h"

@implementation RNNBottomTabOptions

-(instancetype)initWithDict:(NSDictionary *)tabItemDict {
	self = [super init];
	
	[self mergeWith:tabItemDict];
	self.tag = [tabItemDict[@"tag"] integerValue];
	
	return self;
}

- (void)applyOn:(UIViewController *)viewController {
	if (self.text || self.icon || self.selectedIcon) {
		UITabBarItem* tabItem = viewController.tabBarItem;
		
		tabItem.selectedImage = [self getSelectedIconImage];
		tabItem.image = [self getIconImage];
		tabItem.title = self.text;
		tabItem.tag = self.tag;
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
		
		[self appendTitleAttributes:tabItem];
		
		[viewController setTabBarItem:tabItem];
	}
	
	if (self.badge) {
		NSString *badge = nil;
		if (self.badge != nil && ![self.badge isEqual:[NSNull null]]) {
			badge = [RCTConvert NSString:self.badge];
		}
		UITabBarItem *tabBarItem = viewController.tabBarItem;
		if (viewController.navigationController) {
			tabBarItem = viewController.navigationController.tabBarItem;
		}
		tabBarItem.badgeValue = badge;
		if (self.badgeColor) {
			tabBarItem.badgeColor = [RCTConvert UIColor:self.badgeColor];
		}
		
		if ([self.badge isEqual:[NSNull null]] || [self.badge isEqualToString:@""]) {
			tabBarItem.badgeValue = nil;
		}
	}
	
	if (self.visible) {
		[viewController.tabBarController setSelectedIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
	}
	
	[self resetOptions];
}

- (UIImage *)getIconImage {
	return [self getIconImageWithTint:self.iconColor];
}

- (UIImage *)getSelectedIconImage {
	if (self.selectedIcon) {
		if (self.selectedIconColor) {
			return [[[RCTConvert UIImage:self.selectedIcon] withTintColor:[RCTConvert UIColor:self.selectedIconColor]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			return [[RCTConvert UIImage:self.selectedIcon] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		}
	} else {
		return [self getIconImageWithTint:self.selectedIconColor];
	}
	
	return nil;
}

- (UIImage *)getIconImageWithTint:(NSDictionary *)tintColor {
	if (self.icon) {
		if (tintColor) {
			return [[[RCTConvert UIImage:self.icon] withTintColor:[RCTConvert UIColor:tintColor]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			return [[RCTConvert UIImage:self.icon] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		}
	}
	
	return nil;
}

- (void)appendTitleAttributes:(UITabBarItem *)tabItem {
	NSMutableDictionary* selectedAttributes = [NSMutableDictionary dictionaryWithDictionary:[tabItem titleTextAttributesForState:UIControlStateNormal]];
	if (self.selectedTextColor) {
		selectedAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.selectedTextColor];
	} else {
		selectedAttributes[NSForegroundColorAttributeName] = [UIColor blackColor];
	}
	
	selectedAttributes[NSFontAttributeName] = [self tabBarTextFont];
	[tabItem setTitleTextAttributes:selectedAttributes forState:UIControlStateSelected];
	
	
	NSMutableDictionary* normalAttributes = [NSMutableDictionary dictionaryWithDictionary:[tabItem titleTextAttributesForState:UIControlStateNormal]];
	if (self.textColor) {
		normalAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.textColor];
	} else {
		normalAttributes[NSForegroundColorAttributeName] = [UIColor blackColor];
	}
	
	normalAttributes[NSFontAttributeName] = [self tabBarTextFont];
	[tabItem setTitleTextAttributes:normalAttributes forState:UIControlStateNormal];
}


-(UIFont *)tabBarTextFont {
	if (self.fontFamily) {
		return [UIFont fontWithName:self.fontFamily size:self.tabBarTextFontSizeValue];
	}
	else if (self.fontSize) {
		return [UIFont systemFontOfSize:self.tabBarTextFontSizeValue];
	}
	else {
		return nil;
	}
}

-(CGFloat)tabBarTextFontSizeValue {
	return self.fontSize ? [self.fontSize floatValue] : 10;
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
