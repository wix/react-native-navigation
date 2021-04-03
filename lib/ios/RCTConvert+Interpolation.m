#import "AccelerateDecelerateInterpolator.h"
#import "AccelerateInterpolator.h"
#import "BoolParser.h"
#import "DecelerateAccelerateInterpolator.h"
#import "DecelerateInterpolator.h"
#import "FastOutSlowIn.h"
#import "Interpolator.h"
#import "LinearInterpolator.h"
#import "NumberParser.h"
#import "OvershootInterpolator.h"
#import "RCTConvert+Interpolation.h"
#import "SpringInterpolator.h"

@implementation RCTConvert (Interpolation)

RCT_CUSTOM_CONVERTER(id<Interpolator>, Interpolator, [RCTConvert interpolatorFromJson:json])

+ (id<Interpolator>)defaultInterpolator {
    return [[LinearInterpolator alloc] init];
}

#pragma mark Private

+ (id<Interpolator>)interpolatorFromJson:(id)json {
    if (json == nil || ![json isKindOfClass:[NSDictionary class]]) {
        return [RCTConvert defaultInterpolator];
    }
    NSString *interpolation = json[@"type"] ? json[@"type"] : nil;

    id<Interpolator> (^interpolator)(void) = @{
        @"decelerate" : ^{
          CGFloat factor = [[[NumberParser parse:json key:@"factor"]
              withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
          return [[DecelerateInterpolator alloc] init:factor];
        },
        @"accelerate" : ^{
          CGFloat factor = [[[NumberParser parse:json key:@"factor"]
              withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
          return [[AccelerateInterpolator alloc] init:factor];
        },
        @"accelerateDecelerate" : ^{
          return [[AccelerateDecelerateInterpolator alloc] init];
        },
        @"decelerateAccelerate" : ^{
          return [[DecelerateAccelerateInterpolator alloc] init];
        },
        @"fastOutSlowIn" : ^{
          return [FastOutSlowIn new];
        },
        @"linear" : ^{
          return [[LinearInterpolator alloc] init];
        },
        @"overshoot" : ^{
          CGFloat tension = [[[NumberParser parse:json key:@"tension"]
              withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
          return [[OvershootInterpolator alloc] init:tension];
        },
        @"spring" : ^{
          CGFloat mass = [[[NumberParser parse:json key:@"mass"]
              withDefault:[NSNumber numberWithFloat:3.0f]] floatValue];
          CGFloat damping = [[[NumberParser parse:json key:@"damping"]
              withDefault:[NSNumber numberWithFloat:500.0f]] floatValue];
          CGFloat stiffness = [[[NumberParser parse:json key:@"stiffness"]
              withDefault:[NSNumber numberWithFloat:200.0f]] floatValue];
          CGFloat allowsOverdamping = [[BoolParser parse:json
                                                     key:@"allowsOverdamping"] withDefault:NO];
          CGFloat initialVelocity = [[[NumberParser parse:json key:@"initialVelocity"]
              withDefault:[NSNumber numberWithFloat:0.0f]] floatValue];
          return [[SpringInterpolator alloc] init:mass
                                          damping:damping
                                        stiffness:stiffness
                                allowsOverdamping:allowsOverdamping
                                  initialVelocity:initialVelocity];
        },
    }[interpolation];

    if (interpolator != nil) {
        return interpolator();
    } else {
        return [RCTConvert defaultInterpolator];
    }
}

@end
