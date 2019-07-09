#import "DotIndicatorOptions.h"


@implementation DotIndicatorOptions
- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.color = [ColorParser parse:dict key:@"color"];
    self.size = [NumberParser parse:dict key:@"size"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    return self;
}

- (bool)hasValue {
    return [self.visible hasValue];
}

@end