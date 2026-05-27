#import "RNNBottomTabsCustomRowOptions.h"

@implementation RNNBottomTabsCustomRowOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.height = [NumberParser parse:dict key:@"height"];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    self.backgroundEffect = [TextParser parse:dict key:@"backgroundEffect"];
    self.cornerRadius = [NumberParser parse:dict key:@"cornerRadius"];
    self.horizontalMargin = [NumberParser parse:dict key:@"horizontalMargin"];
    self.bottomMargin = [NumberParser parse:dict key:@"bottomMargin"];
    return self;
}

- (void)mergeOptions:(RNNBottomTabsCustomRowOptions *)options {
    if (options.height.hasValue)
        self.height = options.height;
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
    if (options.backgroundEffect.hasValue)
        self.backgroundEffect = options.backgroundEffect;
    if (options.cornerRadius.hasValue)
        self.cornerRadius = options.cornerRadius;
    if (options.horizontalMargin.hasValue)
        self.horizontalMargin = options.horizontalMargin;
    if (options.bottomMargin.hasValue)
        self.bottomMargin = options.bottomMargin;
}

- (BOOL)hasValue {
    return self.height.hasValue || self.backgroundColor.hasValue ||
           self.backgroundEffect.hasValue || self.cornerRadius.hasValue ||
           self.horizontalMargin.hasValue || self.bottomMargin.hasValue;
}

@end
