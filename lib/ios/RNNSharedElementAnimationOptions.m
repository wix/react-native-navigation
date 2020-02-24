#import "RNNSharedElementAnimationOptions.h"

#define DEFAULT_DURATION @(0.7)
#define DEFAULT_SPRING_VELOCITY @(0.8)
#define DEFAULT_SPRING_DAMPING @(0.85)

@implementation RNNSharedElementAnimationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.animations = dict[@"animations"];
	self.duration = dict[@"duration"];
	self.springDamping = dict[@"springDamping"];
	self.springVelocity = dict[@"springVelocity"];
	
	return self;
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

- (BOOL)hasValue {
    return self.animations;
}

@end
