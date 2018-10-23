#import "RNNSideMenuOptions.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.left = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"left"]];
	self.right = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"right"]];
	self.animationType = [TextParser parse:dict key:@"animationType"];
	
	return self;
}


@end
