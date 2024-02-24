#import "RNNWindowOptions.h"

@implementation RNNWindowOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.backgroundColor = [RNNColorParser parse:dict key:@"backgroundColor"];
    return self;
}

- (void)mergeOptions:(RNNWindowOptions *)options {
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
}

@end
