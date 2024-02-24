#import "RNNInterpolatorProtocol.h"
#import "RNNOptions.h"

@interface TransitionDetailsOptions : RNNOptions

@property(nonatomic, strong) Double *from;
@property(nonatomic, strong) Double *to;
@property(nonatomic, strong) TimeInterval *duration;
@property(nonatomic, strong) TimeInterval *startDelay;
@property(nonatomic, strong) id<RNNInterpolatorProtocol> interpolator;

- (BOOL)hasAnimation;

@end
