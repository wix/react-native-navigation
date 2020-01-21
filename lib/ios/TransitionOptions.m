#import "TransitionOptions.h"

@implementation TransitionOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.alpha = [[TransitionDetailsOptions alloc] initWithDict:dict[@"alpha"]];
	self.x = [[TransitionDetailsOptions alloc] initWithDict:dict[@"x"]];
    self.y = [[TransitionDetailsOptions alloc] initWithDict:dict[@"y"]];
    self.translationX = [[TransitionDetailsOptions alloc] initWithDict:dict[@"translationX"]];
    self.translationY = [[TransitionDetailsOptions alloc] initWithDict:dict[@"translationY"]];
    self.rotationX = [[TransitionDetailsOptions alloc] initWithDict:dict[@"rotationX"]];
    self.rotationY = [[TransitionDetailsOptions alloc] initWithDict:dict[@"rotationY"]];
    
	return self;
}

- (BOOL)hasAnimation {
	return self.x.hasAnimation || self.y.hasAnimation || self.alpha.hasAnimation;
}

- (NSTimeInterval)minDuration {
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
