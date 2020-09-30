#import "RCTConvert+Interpolation.h"
#import "Interpolators/Interpolator.h"
#import "Interpolators/LinearInterpolator.h"
#import "Interpolators/EaseInterpolator.h"
#import "Interpolators/BackInterpolator.h"
#import "Interpolators/OvershootInterpolator.h"
#import "Interpolators/SpringInterpolator.h"
#import "TextParser.h"
#import "NumberParser.h"

@implementation RCTConvert (Interpolation)

RCT_CUSTOM_CONVERTER(id<Interpolator>, Interpolator, [RCTConvert interpolatorFromJson:json])


#pragma mark Private

+ (id<Interpolator>)interpolatorFromJson:(id)json {
    Text* interpolation = [TextParser parse:json key:@"interpolation"];
    
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
			CGFloat tension = [[NumberParser parse:json key:@"tension"] getWithDefaultValue:[NSNumber numberWithFloat:1.0f]].floatValue;
			return [[OvershootInterpolator alloc] init:tension];
        },
        @"spring" : ^{
            CGFloat mass = [[NumberParser parse:json key:@"mass"] getWithDefaultValue:[NSNumber numberWithFloat:3.0f]].floatValue;
            CGFloat damping = [[NumberParser parse:json key:@"damping"] getWithDefaultValue:[NSNumber numberWithFloat:500.0f]].floatValue;
            CGFloat stiffness = [[NumberParser parse:json key:@"stiffness"] getWithDefaultValue:[NSNumber numberWithFloat:1000.0f]].floatValue;
			return [[SpringInterpolator alloc] init:mass damping:damping stiffness:stiffness];
        },
    }[interpolation];
    
    if (interpolator != nil) {
        return interpolator();
    } else {
        return [[LinearInterpolator alloc] init];
    }
}

@end
