#import "RNNBottomTabOptions.h"
#import "UIImage+tint.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"

@implementation RNNBottomTabOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.text = [TextParser parse:dict key:@"text"];
	self.badge = [TextParser parse:dict key:@"badge"];
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.testID = [TextParser parse:dict key:@"testID"];
	
	
	self.badgeColor = [ColorParser parse:dict key:@"badgeColor"];
	self.icon = [ImageParser parse:dict key:@"icon"];
	self.selectedIcon = [ImageParser parse:dict key:@"selectedIcon"];
	self.iconColor = [ColorParser parse:dict key:@"iconColor"];
	self.selectedIconColor = [ColorParser parse:dict key:@"selectedIconColor"];
	self.selectedTextColor = [ColorParser parse:dict key:@"selectedTextColor"];
	self.iconInsets = [DictionaryParser parse:dict key:@"iconInsets"];
	
	self.textColor = [ColorParser parse:dict key:@"textColor"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.visible = [BoolParser parse:dict key:@"visible"];
	
	return self;
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
