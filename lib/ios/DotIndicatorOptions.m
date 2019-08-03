#import "DotIndicatorOptions.h"
#import "NullColor.h"
#import "NullNumber.h"
#import "NullBool.h"


@implementation DotIndicatorOptions
- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.color = [ColorParser parse:dict key:@"color"];
    self.size = [NumberParser parse:dict key:@"size"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    return self;
}

- (instancetype)init {
    _color = [NullColor new];
    _size = [NullNumber new];
    _visible = [NullBool new];
    return self;
}

- (bool)hasValue {
    return [self.visible hasValue];
}

@end