#import "TransitionDetailsOptions.h"

@implementation TransitionDetailsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.from = [DoubleParser parse:dict key:@"from"];
	self.to = [DoubleParser parse:dict key:@"to"];
	self.startDelay = [TimeIntervalParser parse:dict key:@"startDelay"];
	self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.interpolation = [TextParser parse:dict key:@"interpolation"];
	
	return self;
}

- (BOOL)hasAnimation {
	return self.from.hasValue || self.to.hasValue;
}

@end
