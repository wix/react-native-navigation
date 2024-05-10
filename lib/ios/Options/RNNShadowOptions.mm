#import "RNNShadowOptions.h"

@implementation RNNShadowOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.color = [ColorParser parse:dict key:@"color"];
    self.radius = [NumberParser parse:dict key:@"radius"];
    self.opacity = [NumberParser parse:dict key:@"opacity"];
    return self;
}

- (void)mergeOptions:(RNNShadowOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.radius.hasValue)
        self.radius = options.radius;
    if (options.opacity.hasValue)
        self.opacity = options.opacity;
}

- (BOOL)hasValue {
    return self.color || self.radius || self.opacity;
}

@end
