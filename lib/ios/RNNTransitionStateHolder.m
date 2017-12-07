#import "RNNTransitionStateHolder.h"
#import "RNNUtils.h"
#import "RNNElementFinder.h"

@implementation RNNTransitionStateHolder

-(instancetype)initWithTransition:(NSDictionary *)transition {
	self = [super init];
	self.springDamping = [RNNUtils getDoubleOrKey:transition withKey:@"springDamping" withDefault:0.85];
	self.springVelocity = [RNNUtils getDoubleOrKey:transition withKey:@"springVelocity" withDefault:0.8];
	self.startDelay = [RNNUtils getDoubleOrKey:transition withKey:@"startDelay" withDefault:0];
	self.duration = [RNNUtils getDoubleOrKey:transition withKey:@"duration" withDefault:1];
	self.startAlpha = [RNNUtils getDoubleOrKey:transition withKey:@"startAlpha" withDefault:1];
	self.endAlpha = [RNNUtils getDoubleOrKey:transition withKey:@"endAlpha" withDefault:1];
	self.interactivePop = [RNNUtils getBoolOrKey:transition withKey:@"interactivePop" withDefault:NO];
	self.startX = [RNNUtils getDoubleOrKey:transition withKey:@"startX" withDefault:0];
	self.startY = [RNNUtils getDoubleOrKey:transition withKey:@"startY" withDefault:0];
	self.endX = [RNNUtils getDoubleOrKey:transition withKey:@"endX" withDefault:0];
	self.endY = [RNNUtils getDoubleOrKey:transition withKey:@"endY" withDefault:0];
	self.fromId = [transition objectForKey:@"fromId"];
	self.toId = [transition objectForKey:@"toId"];
	self.fromElement = nil;
	self.fromElementType = nil;
	self.fromElementResizeMode = UIViewContentModeScaleAspectFill;
	self.toElement = nil;
	self.animatedView = nil;
	self.locations  = nil; 
	self.isSharedElementTransition = [transition objectForKey:@"type"];
	return self;
}

@end
