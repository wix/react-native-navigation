#import "RNNBottomTabsOptions.h"

@implementation RNNBottomTabsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.currentTabIndex = [IntNumberParser parse:dict key:@"currentTabIndex"];
	self.drawBehind = [BoolParser parse:dict key:@"drawBehind"];
	self.animate = [BoolParser parse:dict key:@"animate"];
	self.tabColor = [ColorParser parse:dict key:@"tabColor"];
	self.selectedTabColor = [ColorParser parse:dict key:@"selectedTabColor"];
	self.translucent = [BoolParser parse:dict key:@"translucent"];
	self.hideShadow = [BoolParser parse:dict key:@"hideShadow"];
	self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	
	self.testID = [TextParser parse:dict key:@"testID"];
	self.currentTabId = [TextParser parse:dict key:@"currentTabId"];
	self.barStyle = [TextParser parse:dict key:@"barStyle"];
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	
	return self;
}

@end
