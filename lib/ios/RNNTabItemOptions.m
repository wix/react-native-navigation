#import "RNNTabItemOptions.h"

@implementation RNNTabItemOptions

-(instancetype)initWithDict:(NSDictionary *)tabItemDict {
	self = [super init];
	
	self.title = tabItemDict[@"title"];
	self.tag = [tabItemDict[@"tag"] integerValue];
	self.badge = tabItemDict[@"badge"];
	self.testID = tabItemDict[@"testID"];
	self.visible = tabItemDict[@"visible"];
	self.icon = tabItemDict[@"icon"];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

-(void)resetOptions {
	self.title = nil;
	self.badge = nil;
	self.visible = nil;
	self.icon = nil;
	self.testID = nil;
}

@end
