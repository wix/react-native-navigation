#import "RNNLargeTitleOptions.h"

@implementation RNNLargeTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.fontSize = [NumberParser parse:dict key:@"fontSize"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    self.color = [ColorParser parse:dict key:@"color"];
    self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
    self.fontWeight = [TextParser parse:dict key:@"fontWeight"];

    return self;
}

- (void)mergeOptions:(RNNLargeTitleOptions *)options {
    if (options.fontSize.hasValue)
        self.fontSize = options.fontSize;
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.color.hasValue)
        self.color = options.color;
    if (options.fontFamily.hasValue)
        self.fontFamily = options.fontFamily;
    if (options.fontWeight.hasValue)
        self.fontWeight = options.fontWeight;
}

@end
