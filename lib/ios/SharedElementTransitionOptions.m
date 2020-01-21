#import "SharedElementTransitionOptions.h"
#import "RNNUtils.h"
#import "TimeIntervalParser.h"

@implementation SharedElementTransitionOptions

- (instancetype)initWithDict:(NSDictionary *)transition {
	self = [super initWithDict:transition];
	
	self.fromId = [transition objectForKey:@"fromId"];
	self.toId = [transition objectForKey:@"toId"];
    self.startDelay = [TimeIntervalParser parse:transition key:@"startDelay"];
    self.duration = [TimeIntervalParser parse:transition key:@"duration"];
	self.interpolation = [TextParser parse:transition key:@"interpolation"];
	
	return self;
}

@end
