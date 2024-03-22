#import "RNNSharedElementTransitionOptions.h"
#import "RCTConvert+Interpolation.h"
#import "RNNUtils.h"
#import "RNNTimeIntervalParser.h"

@implementation RNNSharedElementTransitionOptions

- (instancetype)initWithDict:(NSDictionary *)transition {
    self = [super initWithDict:transition];

    self.fromId = [transition objectForKey:@"fromId"];
    self.toId = [transition objectForKey:@"toId"];
    self.startDelay = [RNNTimeIntervalParser parse:transition key:@"startDelay"];
    self.duration = [RNNTimeIntervalParser parse:transition key:@"duration"];
    self.interpolator = [RCTConvert Interpolator:transition[@"interpolation"]];

    return self;
}

@end
