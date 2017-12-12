#import "RNNSideMenuOptions.h"

@implementation RNNSideMenuOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)sideMenuOptions {
	self = [super init];
	
	[self mergeWith:sideMenuOptions];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	self.leftSideVisible = [[otherOptions valueForKey:@"left"] valueForKey:@"visible"];
	self.rightSideVisible = [[otherOptions valueForKey:@"right"] valueForKey:@"visible"];
}

-(void)resetOptions {
	self.leftSideVisible = nil;
	self.rightSideVisible = nil;
}

@end
