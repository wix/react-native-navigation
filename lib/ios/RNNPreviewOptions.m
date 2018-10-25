#import "RNNPreviewOptions.h"

@implementation RNNPreviewOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super initWithDict:dict];

	self.reactTag = [NumberParser parse:dict key:@"reactTag"];
	self.height = [NumberParser parse:dict key:@"height"];
	self.width = [NumberParser parse:dict key:@"width"];
	self.commit = [BoolParser parse:dict key:@"commit"];
	self.actions = dict[@"actions"];
	
	return self;
}

@end
