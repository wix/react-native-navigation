#import "RNNSideMenuOptions.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.left = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"left"]];
	self.right = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"right"]];
	self.shouldStretchDrawer = [BoolParser parse:dict key:@"shouldStretchDrawer"];
	self.animationVelocity = [DoubleParser parse:dict key:@"animationVelocity"];
	
	return self;
}


@end
