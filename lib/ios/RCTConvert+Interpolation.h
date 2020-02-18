#import <React/RCTConvert.h>

typedef NS_ENUM(NSInteger, RNNInterpolationOptions) {
    RNNInterpolationLinear = 0,
    RNNInterpolationAccelerateDecelerate,
    RNNInterpolationDecelerate,
    RNNInterpolationAccelerate
};

@interface RCTConvert (Interpolation)

@end

@implementation RCTConvert (Interpolation)

RCT_ENUM_CONVERTER(RNNInterpolationOptions, (@{
  @"linear": @(RNNInterpolationLinear),
  @"accelerateDecelerate": @(RNNInterpolationAccelerateDecelerate),
  @"decelerate": @(RNNInterpolationDecelerate),
  @"accelerate": @(RNNInterpolationAccelerate),
}), RNNInterpolationLinear, integerValue)

@end
