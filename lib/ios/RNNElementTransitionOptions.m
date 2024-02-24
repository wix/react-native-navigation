#import "RNNElementTransitionOptions.h"

@implementation RNNElementTransitionOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.elementId = dict[@"id"];

    return self;
}

@end
