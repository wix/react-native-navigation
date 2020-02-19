#import "WindowOptions.h"

@implementation WindowOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
	return self;
}

@end
