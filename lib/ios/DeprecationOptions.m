#import "DeprecationOptions.h"

@implementation DeprecationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    self.deprecateDrawBehind = [BoolParser parse:dict key:@"deprecateDrawBehind"];
    return self;
}

@end
