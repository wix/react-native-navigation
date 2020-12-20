#import "RNNButtonOptions.h"

@implementation RNNButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.text = [TextParser parse:dict key:@"text"];
    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.color = [ColorParser parse:dict key:@"color"];
    self.disabledColor = [ColorParser parse:dict key:@"disabledColor"];
    self.icon = [ImageParser parse:dict key:@"icon"];
    self.iconInsets = [[RNNInsetsOptions alloc] initWithDict:dict[@"iconInsets"]];
    self.enabled = [BoolParser parse:dict key:@"enabled"];
    self.selectTabOnPress = [BoolParser parse:dict key:@"selectTabOnPress"];

    return self;
}

- (void)mergeOptions:(RNNButtonOptions *)options {
    [self.iconInsets mergeOptions:options.iconInsets];

    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.text.hasValue)
        self.text = options.text;
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.color.hasValue)
        self.color = options.color;
    if (options.disabledColor.hasValue)
        self.disabledColor = options.disabledColor;
    if (options.icon.hasValue)
        self.icon = options.icon;
    if (options.enabled.hasValue)
        self.enabled = options.enabled;
    if (options.selectTabOnPress.hasValue)
        self.selectTabOnPress = options.selectTabOnPress;
}

@end
