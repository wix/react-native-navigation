#import "WindowOptions.h"

@implementation WindowOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    return self;
}

- (void)mergeOptions:(WindowOptions *)options {
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
}

@end
