#import "RNNAnimationOptions.h"

#define DEFAULT_DURATION @(0.7)
#define DEFAULT_SPRING_VELOCITY @(0.8)
#define DEFAULT_SPRING_DAMPING @(0.85)

@implementation RNNAnimationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	return [super initWithDict:dict];
}

- (NSNumber *)duration {
	return _duration ? _duration : DEFAULT_DURATION;
}

- (NSNumber *)springVelocity {
	return _springVelocity ? _springVelocity : DEFAULT_SPRING_VELOCITY;
}

- (NSNumber *)springDamping {
	return _springDamping ? _springDamping : DEFAULT_SPRING_DAMPING;
}

@end
