#import "RNNScrollEdgeAppearanceOptions.h"

@implementation RNNScrollEdgeAppearanceOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.background =
        [[RNNScrollEdgeAppearanceBackgroundOptions alloc] initWithDict:dict[@"background"]];
    self.active = [BoolParser parse:dict key:@"active"];
    self.noBorder = [BoolParser parse:dict key:@"noBorder"];
    self.borderColor = [ColorParser parse:dict key:@"borderColor"];

    return self;
}

- (void)mergeOptions:(RNNScrollEdgeAppearanceOptions *)options {
    [self.background mergeOptions:options.background];

    if (options.active.hasValue)
        self.active = options.active;
    if (options.noBorder.hasValue)
        self.noBorder = options.noBorder;
    if (options.borderColor.hasValue)
        self.borderColor = options.borderColor;
}

@end
