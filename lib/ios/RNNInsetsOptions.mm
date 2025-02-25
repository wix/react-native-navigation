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

+ (RNNInsetsOptions *)withValue:(UIEdgeInsets)insets {
    RNNInsetsOptions *insetsOptions = RNNInsetsOptions.new;
    insetsOptions.top = [Double withValue:insets.top];
    insetsOptions.left = [Double withValue:insets.left];
    insetsOptions.bottom = [Double withValue:insets.bottom];
    insetsOptions.right = [Double withValue:insets.right];

    return insetsOptions;
}

- (UIEdgeInsets)edgeInsetsWithDefault:(UIEdgeInsets)defaultInsets {
    return UIEdgeInsetsMake([self.top withDefault:defaultInsets.top],
                            [self.left withDefault:defaultInsets.left],
                            [self.bottom withDefault:defaultInsets.bottom],
                            [self.right withDefault:defaultInsets.right]);
}

- (UIEdgeInsets)UIEdgeInsets {
    return UIEdgeInsetsMake([self.top withDefault:0], [self.left withDefault:0],
                            [self.bottom withDefault:0], [self.right withDefault:0]);
}

- (BOOL)hasValue {
    return self.top.hasValue || self.left.hasValue || self.bottom.hasValue || self.right.hasValue;
}

@end
