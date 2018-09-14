#import "RNNLayoutInfo.h"

@implementation RNNLayoutInfo

- (instancetype)initWithNode:(RNNLayoutNode *)node optionsManager:(RNNOptionsManager *)optionsManager {
	self = [super init];
	
	self.componentId = node.nodeId;
	self.name = node.data[@"name"];
	self.props = node.data[@"passProps"];
	self.options = [optionsManager createOptions:node.data[@"options"]];
	
	return self;
}

@end
