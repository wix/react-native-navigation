
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
		if ([self hasProperty:key]) {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

-(void)mergeIfEmptyWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([self hasProperty:key] && ![self valueForKey:key]) {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

- (BOOL)hasProperty:(NSString*)propName {
	return [self respondsToSelector:NSSelectorFromString(propName)];
}

@end
