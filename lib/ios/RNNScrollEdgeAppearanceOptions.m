#import "RNNScrollEdgeAppearanceOptions.h"

@implementation RNNScrollEdgeAppearanceOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.background =
        [[RNNScrollEdgeAppearanceBackgroundOptions alloc] initWithDict:dict[@"background"]];
    self.active = [BoolParser parse:dict key:@"active"];

    return self;
}

- (void)mergeOptions:(RNNScrollEdgeAppearanceOptions *)options {
    [self.background mergeOptions:options.background];

    if (options.active.hasValue)
        self.active = options.active;
}

@end
