
#import "RNNOptions.h"

@implementation RNNOptions

-(instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	[self mergeWith:dict];
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

@end
