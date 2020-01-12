#import "RNNModalOptions.h"

@implementation RNNModalOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    self.swipeToDismiss = [BoolParser parse:dict key:@"swipeToDismiss"];
    return self;
}

@end
