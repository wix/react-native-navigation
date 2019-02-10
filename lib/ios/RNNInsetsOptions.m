#import "RNNInsetsOptions.h"

@implementation RNNInsetsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.top = [DoubleParser parse:dict key:@"top"];
	self.left = [DoubleParser parse:dict key:@"left"];
	self.bottom = [DoubleParser parse:dict key:@"bottom"];
	self.right = [DoubleParser parse:dict key:@"right"];
	
	return self;
}

@end
