#import "RNNInterpolatorProtocol.h"
#import <React/RCTConvert.h>

@interface RCTConvert (Interpolation)

+ (id<RNNInterpolatorProtocol>)Interpolator:(id)json;

+ (id<RNNInterpolatorProtocol>)interpolatorFromJson:(id)json;

+ (id<RNNInterpolatorProtocol>)defaultInterpolator;

@end
