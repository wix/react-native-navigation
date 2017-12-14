#import "RNNUITabBarItem.h"

@implementation RNNUITabBarItem

-(instancetype)initWithDictionary:(NSDictionary *)tabItemDict {
	self = [super init];
	
	self.title = tabItemDict[@"title"];
	self.accessibilityIdentifier = tabItemDict[@"testID"];
	self.tag = [tabItemDict[@"tag"] integerValue];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

@end
