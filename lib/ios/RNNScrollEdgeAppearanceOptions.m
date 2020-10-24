#import "RNNScrollEdgeAppearanceOptions.h"

@implementation RNNScrollEdgeAppearanceOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    
    self.background = [[RNNScrollEdgeAppearanceBackgroundOptions alloc] initWithDict:dict[@"background"]];
    
    return self;
}

@end
