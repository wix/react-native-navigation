#import "RCTConvert+Interpolation.h"
#import "Interpolators/Interpolator.h"
#import "Interpolators/LinearInterpolator.h"
#import "Interpolators/EaseInterpolator.h"
#import "Interpolators/BackInterpolator.h"
#import "Interpolators/OvershootInterpolator.h"
#import "Interpolators/SpringInterpolator.h"
#import "NumberParser.h"
#import "BoolParser.h"

@implementation RCTConvert (Interpolation)

RCT_CUSTOM_CONVERTER(id<Interpolator>, Interpolator, [RCTConvert interpolatorFromJson:json])


#pragma mark Private

+ (id<Interpolator>)defaultInterpolator {
    return [[LinearInterpolator alloc] init];
}

+ (id<Interpolator>)interpolatorFromJson:(id)json {
    if (json == nil || ![json isKindOfClass:[NSDictionary class]]) {
        return [RCTConvert defaultInterpolator];
    }
    NSString* interpolation = json[@"type"] ? json[@"type"] : nil;
    
    id<Interpolator> (^interpolator)(void) = @{
        @"back" : ^{
            return [[BackInterpolator alloc] init];
        },
        @"ease" : ^{
            return [[EaseInterpolator alloc] init];
        },
        @"linear" : ^{
            return [[LinearInterpolator alloc] init];
        },
        @"overshoot" : ^{
            CGFloat tension = [[[NumberParser parse:json key:@"tension"] getWithDefaultValue:[NSNumber numberWithFloat:1.0f]] floatValue];
			return [[OvershootInterpolator alloc] init:tension];
        },
        @"spring" : ^{
            CGFloat mass = [[[NumberParser parse:json key:@"mass"] getWithDefaultValue:[NSNumber numberWithFloat:3.0f]] floatValue];
            CGFloat damping = [[[NumberParser parse:json key:@"damping"] getWithDefaultValue:[NSNumber numberWithFloat:500.0f]] floatValue];
            CGFloat stiffness = [[[NumberParser parse:json key:@"stiffness"] getWithDefaultValue:[NSNumber numberWithFloat:1000.0f]] floatValue];
            CGFloat allowsOverdamping = [[BoolParser parse:json key:@"allowsOverdamping"] getWithDefaultValue:NO];
            CGFloat initialVelocity = [[[NumberParser parse:json key:@"initialVelocity"] getWithDefaultValue:[NSNumber numberWithFloat:0.0f]] floatValue];
			return [[SpringInterpolator alloc] init:mass damping:damping stiffness:stiffness allowsOverdamping:allowsOverdamping initialVelocity:initialVelocity];
        },
    }[interpolation];
    
    if (interpolator != nil) {
        return interpolator();
    } else {
        return [RCTConvert defaultInterpolator];
    }
}

@end
