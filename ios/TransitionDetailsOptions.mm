#import "TransitionDetailsOptions.h"
#import "RCTConvert+Interpolation.h"

@implementation TransitionDetailsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.from = [DoubleParser parse:dict key:@"from"];
    self.to = [DoubleParser parse:dict key:@"to"];
    self.startDelay = [TimeIntervalParser parse:dict key:@"startDelay"];
    self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.interpolator = [RCTConvert Interpolator:dict[@"interpolation"]];

    return self;
}

- (void)mergeOptions:(TransitionDetailsOptions *)options {
    if (options.from.hasValue)
        self.from = options.from;
    if (options.to.hasValue)
        self.to = options.to;
    if (options.startDelay.hasValue)
        self.startDelay = options.startDelay;
    if (options.duration.hasValue)
        self.duration = options.duration;
    if (options.interpolator)
        self.interpolator = options.interpolator;
}

- (BOOL)hasAnimation {
    return self.from.hasValue || self.to.hasValue;
}

@end
