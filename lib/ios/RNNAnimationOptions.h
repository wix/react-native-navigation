#import "RNNOptions.h"

@interface RNNAnimationOptions : RNNOptions

@property (nonatomic, strong) NSArray* animations;
@property (nonatomic, strong) NSNumber* duration;
@property (nonatomic, strong) NSNumber* springDamping;
@property (nonatomic, strong) NSNumber* springVelocity;

@end
