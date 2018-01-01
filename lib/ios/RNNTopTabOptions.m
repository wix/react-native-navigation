#import "RNNTopTabOptions.h"

@implementation RNNTopTabOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)tabBarOptions {
	self = [super init];
	
	self.title = [tabBarOptions valueForKey:@"title"];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}
@end

