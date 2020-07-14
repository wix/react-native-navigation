#import "RNNBottomTabOptions.h"
#import "DotIndicatorOptions.h"
#import "DotIndicatorParser.h"

@implementation RNNBottomTabOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    self.tag = arc4random();

    self.text = [TextParser parse:dict key:@"text"];
    self.badge = [TextParser parse:dict key:@"badge"];
    self.badgeColor = [ColorParser parse:dict key:@"badgeColor"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.testID = [TextParser parse:dict key:@"testID"];

    self.dotIndicator = [DotIndicatorParser parse:dict];

    self.icon = [ImageParser parse:dict key:@"icon"];
    self.selectedIcon = [ImageParser parse:dict key:@"selectedIcon"];
    self.iconColor = [ColorParser parse:dict key:@"iconColor"];
    self.selectedIconColor = [ColorParser parse:dict key:@"selectedIconColor"];
    self.selectedTextColor = [ColorParser parse:dict key:@"selectedTextColor"];
    self.iconInsets = [DictionaryParser parse:dict key:@"iconInsets"];

    self.textColor = [ColorParser parse:dict key:@"textColor"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    self.selectTabOnPress = [BoolParser parse:dict key:@"selectTabOnPress"];

    return self;
}

- (BOOL)hasValue {
    return
    self.text.hasValue ||
    self.badge.hasValue ||
    self.badgeColor.hasValue ||
    self.fontFamily.hasValue ||
    self.fontWeight.hasValue ||
    self.fontSize.hasValue ||
    self.testID.hasValue ||
    self.icon.hasValue ||
    self.selectedIcon.hasValue ||
    self.iconColor.hasValue ||
    self.selectedIconColor.hasValue ||
    self.selectedTextColor.hasValue ||
    self.iconInsets.hasValue ||
    self.textColor.hasValue ||
    self.visible.hasValue ||
    self.selectTabOnPress.hasValue;
    
}

@end
