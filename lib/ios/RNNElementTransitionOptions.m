#import "RNNElementTransitionOptions.h"

@implementation RNNElementTransitionOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.alpha = [[RNNAnimationConfigurationOptions alloc] initWithDict:dict[@"alpha"]];
	self.x = [[RNNAnimationConfigurationOptions alloc] initWithDict:dict[@"x"]];
	self.y = [[RNNAnimationConfigurationOptions alloc] initWithDict:dict[@"y"]];

	return self;
}

- (BOOL)hasAnimation {
	return self.x.hasAnimation || self.y.hasAnimation || self.alpha.hasAnimation;
}

- (double)maxDuration {
	double maxDuration = 0;
	if ([_x.duration getWithDefaultValue:0] > maxDuration) {
		maxDuration = [_x.duration getWithDefaultValue:0];
	}
	if ([_y.duration getWithDefaultValue:0] > maxDuration) {
		maxDuration = [_y.duration getWithDefaultValue:0];
	}
	if ([_alpha.duration getWithDefaultValue:0] > maxDuration) {
		maxDuration = [_alpha.duration getWithDefaultValue:0];
	}
	
	return maxDuration;
}

@end
