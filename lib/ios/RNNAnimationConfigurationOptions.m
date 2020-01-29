#import "RNNAnimationConfigurationOptions.h"

@implementation RNNAnimationConfigurationOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.from = [DoubleParser parse:dict key:@"from"];
	self.to = [DoubleParser parse:dict key:@"to"];
	self.startDelay = [DoubleParser parse:dict key:@"startDelay"];
	self.duration = [DoubleParser parse:dict key:@"duration"];
	
	return self;
}

- (BOOL)hasAnimation {
	return self.from.hasValue && self.to.hasValue;
}

@end
