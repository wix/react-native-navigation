
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

-(UIInterfaceOrientationMask)supportedOrientations {
	if (!self.data[@"orientations"]) {
		return [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	}
	
	NSUInteger supportedOrientations = 0;
	for (NSString* orientation in self.data[@"orientations"]) {
		if ([orientation isEqualToString:@"all"]) {
			return UIInterfaceOrientationMaskAll;
		}
		if ([orientation isEqualToString:@"landscape"]) {
			supportedOrientations = (supportedOrientations | UIInterfaceOrientationMaskLandscape);
		}
		if ([orientation isEqualToString:@"portrait"]) {
			supportedOrientations = (supportedOrientations | UIInterfaceOrientationMaskPortrait);
		}
		if ([orientation isEqualToString:@"upsideDown"]) {
			supportedOrientations = (supportedOrientations | UIInterfaceOrientationMaskPortraitUpsideDown);
		}
	}
	
	return supportedOrientations;
}


@end
