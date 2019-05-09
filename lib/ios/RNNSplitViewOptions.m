#import "RNNSplitViewOptions.h"

@implementation RNNSplitViewOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.displayMode = dict[@"displayMode"];
	self.primaryEdge = dict[@"primaryEdge"];
	self.minWidth = [NumberParser parse:dict key:@"minWidth"];
	self.maxWidth = [NumberParser parse:dict key:@"maxWidth"];
	return self;	
}

@end
