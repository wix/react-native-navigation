#import "RNNScrollEdgeEffectOptions.h"

@implementation RNNScrollEdgeOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.hidden = [BoolParser parse:dict key:@"hidden"];
    self.style = [TextParser parse:dict key:@"style"];
    return self;
}

- (void)mergeOptions:(RNNScrollEdgeOptions *)options {
    if (options.hidden.hasValue)
        self.hidden = options.hidden;
    if (options.style.hasValue)
        self.style = options.style;
}

@end

@implementation RNNScrollEdgeEffectOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.hidden = [BoolParser parse:dict key:@"hidden"];
    self.style = [TextParser parse:dict key:@"style"];
    self.top = [[RNNScrollEdgeOptions alloc] initWithDict:dict[@"top"]];
    self.bottom = [[RNNScrollEdgeOptions alloc] initWithDict:dict[@"bottom"]];
    self.left = [[RNNScrollEdgeOptions alloc] initWithDict:dict[@"left"]];
    self.right = [[RNNScrollEdgeOptions alloc] initWithDict:dict[@"right"]];
    return self;
}

- (void)mergeOptions:(RNNScrollEdgeEffectOptions *)options {
    if (options.hidden.hasValue)
        self.hidden = options.hidden;
    if (options.style.hasValue)
        self.style = options.style;
    [self.top mergeOptions:options.top];
    [self.bottom mergeOptions:options.bottom];
    [self.left mergeOptions:options.left];
    [self.right mergeOptions:options.right];
}

@end
