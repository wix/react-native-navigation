#import "RNNSideMenuOptions.h"
#import "SideMenuOpenGestureModeParser.h"

@implementation RNNSideMenuOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.left = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"left"]];
	self.right = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"right"]];
	self.animationType = [TextParser parse:dict key:@"animationType"];
	self.openGestureMode = [SideMenuOpenGestureModeParser parse:dict key:@"openGestureMode"];

	return self;
}


@end
