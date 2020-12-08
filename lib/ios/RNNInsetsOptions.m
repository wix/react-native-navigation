#import "RNNInsetsOptions.h"

@implementation RNNInsetsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.top = [DoubleParser parse:dict key:@"top"];
    self.left = [DoubleParser parse:dict key:@"left"];
    self.bottom = [DoubleParser parse:dict key:@"bottom"];
    self.right = [DoubleParser parse:dict key:@"right"];

    return self;
}

- (void)mergeOptions:(RNNInsetsOptions *)options {
    if (options.top.hasValue)
        self.top = options.top;
    if (options.left.hasValue)
        self.left = options.left;
    if (options.bottom.hasValue)
        self.bottom = options.bottom;
    if (options.right.hasValue)
        self.right = options.right;
}

@end
