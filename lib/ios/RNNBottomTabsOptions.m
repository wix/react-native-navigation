#import "RNNBottomTabsOptions.h"

@implementation RNNBottomTabsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.currentTabIndex = [NumberParser parse:dict key:@"currentTabIndex"];
	self.drawBehind = [BoolParser parse:dict key:@"drawBehind"];
	self.tabColor = [NumberParser parse:dict key:@"tabColor"];
	self.selectedTabColor = [NumberParser parse:dict key:@"selectedTabColor"];
	self.translucent = [BoolParser parse:dict key:@"translucent"];
	self.hideShadow = [BoolParser parse:dict key:@"hideShadow"];
	self.backgroundColor = [NumberParser parse:dict key:@"backgroundColor"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	
	self.testID = [StringParser parse:dict key:@"testID"];
	self.currentTabId = [StringParser parse:dict key:@"currentTabId"];
	self.barStyle = [StringParser parse:dict key:@"barStyle"];
	self.fontFamily = [StringParser parse:dict key:@"fontFamily"];
	
	return self;
}

- (void)resetOptions {
	self.currentTabId = nil;
	self.currentTabIndex = nil;
}

@end
