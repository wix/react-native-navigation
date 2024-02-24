#import "RNNAccelerateDecelerateInterpolator.h"
#import "RNNAccelerateInterpolator.h"
#import "RNNBoolParser.h"
#import "RNNDecelerateAccelerateInterpolator.h"
#import "RNNDecelerateInterpolator.h"
#import "RNNFastOutSlowIn.h"
#import "RNNInterpolatorProtocol.h"
#import "RNNLinearInterpolator.h"
#import "RNNNumberParser.h"
#import "RNNOvershootInterpolator.h"
#import "RCTConvert+Interpolation.h"
#import "RNNSpringInterpolator.h"

@implementation RCTConvert (Interpolation)

RCT_CUSTOM_CONVERTER(id<RNNInterpolatorProtocol>, Interpolator, [RCTConvert interpolatorFromJson:json])

+ (id<RNNInterpolatorProtocol>)defaultInterpolator {
    return [[RNNLinearInterpolator alloc] init];
}

#pragma mark Private

+ (id<RNNInterpolatorProtocol>)interpolatorFromJson:(id)json {
    if (json == nil || ![json isKindOfClass:[NSDictionary class]]) {
        return [RCTConvert defaultInterpolator];
    }
    NSString *interpolation = json[@"type"] ? json[@"type"] : nil;

    id<RNNInterpolatorProtocol> (^interpolator)(void) = @{@"decelerate" : ^{
        CGFloat factor = [[[RNNNumberParser parse:json key:@"factor"]
            withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
    return [[RNNDecelerateInterpolator alloc] init:factor];
}
,
        @"accelerate" : ^{
          CGFloat factor = [[[RNNNumberParser parse:json key:@"factor"]
              withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
          return [[RNNAccelerateInterpolator alloc] init:factor];
        },
        @"accelerateDecelerate" : ^{
          return [[RNNAccelerateDecelerateInterpolator alloc] init];
        },
        @"decelerateAccelerate" : ^{
          return [[RNNDecelerateAccelerateInterpolator alloc] init];
        },
        @"fastOutSlowIn" : ^{
          return [RNNFastOutSlowIn new];
        },
        @"linear" : ^{
          return [[RNNLinearInterpolator alloc] init];
        },
        @"overshoot" : ^{
          CGFloat tension = [[[RNNNumberParser parse:json key:@"tension"]
              withDefault:[NSNumber numberWithFloat:1.0f]] floatValue];
          return [[RNNOvershootInterpolator alloc] init:tension];
        },
        @"spring" : ^{
          CGFloat mass = [[[RNNNumberParser parse:json key:@"mass"]
              withDefault:[NSNumber numberWithFloat:3.0f]] floatValue];
          CGFloat damping = [[[RNNNumberParser parse:json key:@"damping"]
              withDefault:[NSNumber numberWithFloat:500.0f]] floatValue];
          CGFloat stiffness = [[[RNNNumberParser parse:json key:@"stiffness"]
              withDefault:[NSNumber numberWithFloat:200.0f]] floatValue];
          CGFloat allowsOverdamping = [[RNNBoolParser parse:json
                                                     key:@"allowsOverdamping"] withDefault:NO];
          CGFloat initialVelocity = [[[RNNNumberParser parse:json key:@"initialVelocity"]
              withDefault:[NSNumber numberWithFloat:0.0f]] floatValue];
          return [[RNNSpringInterpolator alloc] init:mass
                                             damping:damping
                                           stiffness:stiffness
                                   allowsOverdamping:allowsOverdamping
                                     initialVelocity:initialVelocity];
        },
}
[interpolation];

if (interpolator != nil) {
    return interpolator();
} else {
    return [RCTConvert defaultInterpolator];
}
}

@end
