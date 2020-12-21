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

- (id)copyWithZone:(NSZone *)zone {
    RNNInsetsOptions *newOptions = RNNInsetsOptions.new;
    newOptions.top = self.top.copy;
    newOptions.left = self.left.copy;
    newOptions.bottom = self.bottom.copy;
    newOptions.right = self.right.copy;
    return newOptions;
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

- (UIEdgeInsets)edgeInsetsWithDefault:(UIEdgeInsets)defaultInsets {
    return UIEdgeInsetsMake([self.top getWithDefaultValue:defaultInsets.top],
                            [self.left getWithDefaultValue:defaultInsets.left],
                            [self.bottom getWithDefaultValue:defaultInsets.bottom],
                            [self.right getWithDefaultValue:defaultInsets.right]);
}

- (UIEdgeInsets)UIEdgeInsets {
    return UIEdgeInsetsMake([self.top getWithDefaultValue:0], [self.left getWithDefaultValue:0],
                            [self.bottom getWithDefaultValue:0],
                            [self.right getWithDefaultValue:0]);
}

- (BOOL)hasValue {
    return UIEdgeInsetsEqualToEdgeInsets([self edgeInsetsWithDefault:UIEdgeInsetsZero],
                                         UIEdgeInsetsZero);
}

@end
