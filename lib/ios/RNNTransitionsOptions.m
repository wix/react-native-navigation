#import "RNNTransitionsOptions.h"

@implementation RNNTransitionsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super initWithDict:dict];
	
	[self mergeWith:dict];
	
	return self;
}

@end
