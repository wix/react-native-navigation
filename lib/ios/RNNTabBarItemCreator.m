#import "RNNTabBarItemCreator.h"
#import "UIImage+tint.h"
#import "RNNFontAttributesCreator.h"

@implementation RNNTabBarItemCreator

+ (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    return [UITabBarItem new];
}

+ (UITabBarItem *)createTabBarItem:(RNNBottomTabOptions *)bottomTabOptions mergeItem:(UITabBarItem *)mergeItem {
    UITabBarItem* tabItem = [self createTabBarItem:mergeItem];
	UIImage* icon = [bottomTabOptions.icon getWithDefaultValue:nil];
	UIImage* selectedIcon = [bottomTabOptions.selectedIcon getWithDefaultValue:icon];
	UIColor* iconColor = [bottomTabOptions.iconColor getWithDefaultValue:nil];
	UIColor* selectedIconColor = [bottomTabOptions.selectedIconColor getWithDefaultValue:iconColor];
	
	tabItem.image = [self getIconImage:icon withTint:iconColor];
	tabItem.selectedImage = [self getSelectedIconImage:selectedIcon selectedIconColor:selectedIconColor];
	tabItem.title = [bottomTabOptions.text getWithDefaultValue:nil];
	tabItem.tag = bottomTabOptions.tag;
	tabItem.accessibilityIdentifier = [bottomTabOptions.testID getWithDefaultValue:nil];
	
	NSDictionary* iconInsets = [bottomTabOptions.iconInsets getWithDefaultValue:nil];
	if (iconInsets && ![iconInsets isKindOfClass:[NSNull class]]) {
		id topInset = iconInsets[@"top"];
		id leftInset = iconInsets[@"left"];
		id bottomInset = iconInsets[@"bottom"];
		id rightInset = iconInsets[@"right"];
		
		CGFloat top = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:topInset] : 0;
		CGFloat left = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:leftInset] : 0;
		CGFloat bottom = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:bottomInset] : 0;
		CGFloat right = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:rightInset] : 0;
		
		tabItem.imageInsets = UIEdgeInsetsMake(top, left, bottom, right);
	}
	
	[self appendTitleAttributes:tabItem bottomTabOptions:bottomTabOptions];
	
	return tabItem;
}

+ (UIImage *)getSelectedIconImage:(UIImage *)selectedIcon selectedIconColor:(UIColor *)selectedIconColor {
	if (selectedIcon) {
		if (selectedIconColor) {
			return [[selectedIcon withTintColor:selectedIconColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			return [selectedIcon imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		}
	}
	
	return nil;
}

+ (UIImage *)getIconImage:(UIImage *)icon withTint:(UIColor *)tintColor {
	if (icon) {
		if (tintColor) {
			return [[icon withTintColor:tintColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		} else {
			return [icon imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		}
	}
	
	return nil;
}

+ (void)appendTitleAttributes:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UIColor* textColor = [bottomTabOptions.textColor getWithDefaultValue:nil];
    UIColor* selectedTextColor = [bottomTabOptions.selectedTextColor getWithDefaultValue:nil];
    NSString* fontFamily = [bottomTabOptions.fontFamily getWithDefaultValue:nil];
    NSNumber* fontSize = [bottomTabOptions.fontSize getWithDefaultValue:nil];
    NSString* fontWeight = [bottomTabOptions.fontWeight getWithDefaultValue:nil];
    
	NSDictionary* selectedAttributes = [RNNFontAttributesCreator createFromDictionary:[tabItem titleTextAttributesForState:UIControlStateSelected] fontFamily:fontFamily fontSize:fontSize defaultFontSize:@(10) fontWeight:fontWeight color:selectedTextColor defaultColor:[UIColor blackColor]];
	[self setSelectedTitleAttributes:tabItem selectedTitleAttributes:selectedAttributes];
	
	
	NSDictionary* normalAttributes = [RNNFontAttributesCreator createFromDictionary:[tabItem titleTextAttributesForState:UIControlStateNormal] fontFamily:fontFamily fontSize:fontSize defaultFontSize:@(10) fontWeight:fontWeight color:textColor defaultColor:[UIColor blackColor]];
	[self setTitleAttributes:tabItem titleAttributes:normalAttributes];
}

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
	[tabItem setTitleTextAttributes:titleAttributes forState:UIControlStateNormal];
}

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
	[tabItem setTitleTextAttributes:selectedTitleAttributes forState:UIControlStateSelected];
}

@end
