#import "RNNScrollEdgeAppearanceOptions.h"

@implementation RNNScrollEdgeAppearanceOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    
    self.background = [[RNNBackgroundOptions alloc] initWithDict:dict[@"background"]];
    
    return self;
}

@end
