#import "Interpolator.h"
#import <React/RCTConvert.h>

@interface RCTConvert (Interpolation)

+ (id<Interpolator>)Interpolator:(id)json;

+ (id<Interpolator>)interpolatorFromJson:(id)json;

+ (id<Interpolator>)defaultInterpolator;

@end
