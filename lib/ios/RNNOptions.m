
#import "RNNOptions.h"

@implementation RNNOptions

-(instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	[self mergeWith:dict];
	return self;
}

- (void)applyOn:(UIViewController *)viewController defaultOptions:(RNNOptions *)defaultOptions {
	[defaultOptions applyOn:viewController];
	[self applyOn:viewController];
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

@end
