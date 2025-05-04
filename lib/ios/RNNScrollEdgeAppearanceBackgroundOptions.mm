#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@implementation RNNScrollEdgeAppearanceBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.color = [ColorParser parse:dict key:@"color"];
    self.translucent = [BoolParser parse:dict key:@"translucent"];

    return self;
}

- (void)mergeOptions:(RNNScrollEdgeAppearanceBackgroundOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.translucent.hasValue)
        self.translucent = options.translucent;
}

@end
