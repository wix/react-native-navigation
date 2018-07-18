
#import "RNNLayoutNode.h"

@implementation RNNLayoutNode

+(instancetype)create:(NSDictionary *)json
{
	RNNLayoutNode* node = [RNNLayoutNode new];
	node.type = json[@"type"];
	node.nodeId = json[@"id"];
	node.data = json[@"data"];
	node.children = json[@"children"];
	
	return node;
}

-(BOOL)isComponent
{
	return [self.type isEqualToString:@"Component"];
}
-(BOOL)isExternalComponent
{
	return [self.type isEqualToString:@"ExternalComponent"];
}
-(BOOL)isStack
{
	return [self.type isEqualToString:@"Stack"];
}
-(BOOL)isTabs
{
	return [self.type isEqualToString:@"BottomTabs"];
}
-(BOOL)isTopTabs
{
	return [self.type isEqualToString:@"TopTabs"];
}
-(BOOL)isSideMenuRoot
{
	return [self.type isEqualToString:@"SideMenuRoot"];
}
-(BOOL)isSideMenuLeft
{
	return [self.type isEqualToString:@"SideMenuLeft"];
}
-(BOOL)isSideMenuRight
{
	return [self.type isEqualToString:@"SideMenuRight"];
}
-(BOOL)isSideMenuCenter
{
	return [self.type isEqualToString:@"SideMenuCenter"];
}
-(BOOL)isSplitView
{
	return [self.type isEqualToString:@"SplitView"];
}

@end
