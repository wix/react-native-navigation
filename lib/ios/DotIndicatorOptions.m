#import "DotIndicatorOptions.h"
#import "NullBool.h"
#import "NullColor.h"
#import "NullNumber.h"

@implementation DotIndicatorOptions
- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

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

- (void)mergeOptions:(DotIndicatorOptions *)options {
    if (options.color.hasValue)
        self.color = options.color;
    if (options.size.hasValue)
        self.size = options.size;
    if (options.visible.hasValue)
        self.visible = options.visible;
}

- (bool)hasValue {
    return [self.visible hasValue];
}

@end
