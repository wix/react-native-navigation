#import "RNNModalOptions.h"

@implementation RNNModalOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.swipeToDismiss = [BoolParser parse:dict key:@"swipeToDismiss"];
    return self;
}

- (void)mergeOptions:(RNNModalOptions *)options {
    if (options.swipeToDismiss.hasValue)
        self.swipeToDismiss = options.swipeToDismiss;
}

@end
