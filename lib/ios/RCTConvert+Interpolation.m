#import "RCTConvert+Interpolation.h"

@implementation RCTConvert (Interpolation)

RCT_ENUM_CONVERTER(RNNInterpolationOptions, (@{
  @"linear": @(RNNInterpolationLinear),
  @"accelerateDecelerate": @(RNNInterpolationAccelerateDecelerate),
  @"decelerate": @(RNNInterpolationDecelerate),
  @"accelerate": @(RNNInterpolationAccelerate),
  @"overshoot": @(RNNInterpolationOvershoot),
  @"spring": @(RNNInterpolationSpring),
}), RNNInterpolationLinear, integerValue)

@end
