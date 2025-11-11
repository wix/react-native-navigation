#import "RNNBottomTabOptions.h"
#import "DotIndicatorOptions.h"
#import "DotIndicatorParser.h"

@implementation RNNBottomTabOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.tag = arc4random();

    self.text = [TextParser parse:dict key:@"text"];
    self.badge = [TextParser parse:dict key:@"badge"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
    self.testID = [TextParser parse:dict key:@"testID"];
    self.accessibilityLabel = [TextParser parse:dict key:@"accessibilityLabel"];
    self.badgeColor = [ColorParser parse:dict key:@"badgeColor"];

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
    self.sfSymbol = [TextParser parse:dict key:@"sfSymbol"];
    self.sfSelectedSymbol = [TextParser parse:dict key:@"sfSelectedSymbol"];

    return self;
}

- (void)mergeOptions:(RNNBottomTabOptions *)options {
    [self.dotIndicator mergeOptions:options.dotIndicator];

    if (options.text.hasValue)
        self.text = options.text;
    if (options.badge.hasValue)
        self.badge = options.badge;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.fontWeight.hasValue)
        self.fontWeight = options.fontWeight;
    if (options.testID.hasValue)
        self.testID = options.testID;
    if (options.accessibilityLabel.hasValue)
        self.accessibilityLabel = options.accessibilityLabel;
    if (options.badgeColor.hasValue)
        self.badgeColor = options.badgeColor;
    if (options.icon.hasValue)
        self.icon = options.icon;
    if (options.selectedIcon.hasValue)
        self.selectedIcon = options.selectedIcon;
    if (options.iconColor.hasValue)
        self.iconColor = options.iconColor;
    if (options.selectedIconColor.hasValue)
        self.selectedIconColor = options.selectedIconColor;
    if (options.selectedTextColor.hasValue)
        self.selectedTextColor = options.selectedTextColor;
    if (options.iconInsets.hasValue)
        self.iconInsets = options.iconInsets;
    if (options.textColor.hasValue)
        self.textColor = options.textColor;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.selectTabOnPress.hasValue)
        self.selectTabOnPress = options.selectTabOnPress;
    if (options.sfSymbol.hasValue)
        self.sfSymbol = options.sfSymbol;
    if (options.sfSelectedSymbol.hasValue)
        self.sfSelectedSymbol = options.sfSelectedSymbol;
}

- (BOOL)hasValue {
    return self.text.hasValue || self.badge.hasValue || self.badgeColor.hasValue ||
           self.fontFamily.hasValue || self.fontWeight.hasValue || self.fontSize.hasValue ||
           self.testID.hasValue || self.icon.hasValue || self.selectedIcon.hasValue ||
           self.iconColor.hasValue || self.selectedIconColor.hasValue ||
           self.selectedTextColor.hasValue || self.iconInsets.hasValue || self.textColor.hasValue ||
           self.visible.hasValue || self.selectTabOnPress.hasValue || self.sfSymbol.hasValue ||
           self.sfSelectedSymbol.hasValue;
}

@end
