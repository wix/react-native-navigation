#import "RNNInterpolatorProtocol.h"
#import "RNNOptions.h"

@interface TransitionDetailsOptions : RNNOptions

@property(nonatomic, strong) RNNDouble *from;
@property(nonatomic, strong) RNNDouble *to;
@property(nonatomic, strong) RNNTimeInterval *duration;
@property(nonatomic, strong) RNNTimeInterval *startDelay;
@property(nonatomic, strong) id<RNNInterpolatorProtocol> interpolator;

- (BOOL)hasAnimation;

@end
