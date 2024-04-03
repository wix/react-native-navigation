#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@implementation RNNScrollEdgeAppearanceBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.color = [RNNColorParser parse:dict key:@"color"];
    self.translucent = [RNNBoolParser parse:dict key:@"translucent"];

    return self;
}

- (void)mergeOptions:(RNNScrollEdgeAppearanceBackgroundOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.translucent.hasValue)
        self.translucent = options.translucent;
}

@end
