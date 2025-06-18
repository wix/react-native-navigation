#import "RNNOptions.h"

@implementation RNNOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    return self;
}

- (void)mergeOptions:(RNNOptions *)otherOptions {
    @throw [NSException
        exceptionWithName:@"RNNOptions mergeOptions:otherOptions not implemented"
                   reason:@"mergeOptions:otherOptions should be implemented by subclass"
                 userInfo:nil];
}

@end
