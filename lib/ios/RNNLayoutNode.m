
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

-(BOOL)isContainer
{
	return [self.type isEqualToString:@"Container"];
}
-(BOOL)isContainerStack
{
	return [self.type isEqualToString:@"ContainerStack"];
}
-(BOOL)isTabs
{
	return [self.type isEqualToString:@"BottomTabs"];
}
-(BOOL)isTopTabs
{
	return [self.type isEqualToString:@"TopTabs"];
}
-(BOOL)isTopTab
{
	return [self.type isEqualToString:@"TopTab"];
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

@end
