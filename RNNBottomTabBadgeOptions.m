#import "RNNBottomTabBadgeOptions.h"

@implementation RNNBottomTabBadgeOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.text = [TextParser parse:dict key:@"text"];
    self.textColor = [ColorParser parse:dict key:@"textColor"];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    return self;
}

- (void)mergeOptions:(RNNBottomTabBadgeOptions *)options {
    if (options.text.hasValue)
        self.text = options.text;
    if (options.textColor.hasValue)
        self.textColor = options.textColor;
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
}

- (BOOL)hasValue {
    return self.text.hasValue || self.textColor.hasValue || self.backgroundColor.hasValue;
}

+ (RNNBottomTabBadgeOptions *)parse:(NSDictionary *)dict {
    return [[RNNBottomTabBadgeOptions alloc] initWithDict:dict[@"badge"]];
}

@end
