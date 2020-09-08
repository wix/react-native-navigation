#import <React/RCTConvert.h>

typedef NS_ENUM(NSInteger, RNNInterpolationOptions) {
    RNNInterpolationLinear = 0,
    RNNInterpolationAccelerateDecelerate,
    RNNInterpolationDecelerate,
    RNNInterpolationAccelerate,
    RNNInterpolationSpring
};

@interface RCTConvert (Interpolation)

+ (RNNInterpolationOptions)RNNInterpolationOptions:(id)json;

@end
