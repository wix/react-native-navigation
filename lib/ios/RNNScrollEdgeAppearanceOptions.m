#import "RNNScrollEdgeAppearanceOptions.h"

@implementation RNNScrollEdgeAppearanceOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.background =
        [[RNNScrollEdgeAppearanceBackgroundOptions alloc] initWithDict:dict[@"background"]];
    self.active = [BoolParser parse:dict key:@"active"];
    return self;
}

@end
