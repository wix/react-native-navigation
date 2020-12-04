#import "TransitionDetailsOptions.h"
#import "RCTConvert+Interpolation.h"

@implementation TransitionDetailsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];

    self.from = [DoubleParser parse:dict key:@"from"];
    self.to = [DoubleParser parse:dict key:@"to"];
    self.startDelay = [TimeIntervalParser parse:dict key:@"startDelay"];
    self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.interpolator = [RCTConvert Interpolator:dict[@"interpolation"]];

    return self;
}

- (BOOL)hasAnimation {
    return self.from.hasValue || self.to.hasValue;
}

@end
