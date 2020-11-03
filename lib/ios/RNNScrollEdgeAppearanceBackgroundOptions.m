#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@implementation RNNScrollEdgeAppearanceBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.color = [ColorParser parse:dict key:@"color"];
    self.translucent = [BoolParser parse:dict key:@"translucent"];

    return self;
}

@end
