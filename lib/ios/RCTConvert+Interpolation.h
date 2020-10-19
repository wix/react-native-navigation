#import <React/RCTConvert.h>
#import "Interpolator.h"

@interface RCTConvert (Interpolation)

+ (id<Interpolator>)Interpolator:(id)json;

+ (id<Interpolator>)interpolatorFromJson:(id)json;

+ (id<Interpolator>)defaultInterpolator;

@end
