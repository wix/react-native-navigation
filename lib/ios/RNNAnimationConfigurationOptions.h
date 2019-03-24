#import "RNNOptions.h"

@interface RNNAnimationConfigurationOptions : RNNOptions

@property (nonatomic, strong) Double* from;
@property (nonatomic, strong) Double* to;
@property (nonatomic, strong) Double* duration;
@property (nonatomic, strong) Double* startDelay;

- (BOOL)hasAnimation;

@end
