#import "RNNLayoutInfo.h"

@implementation RNNLayoutInfo

- (instancetype)initWithNode:(RNNLayoutNode *)node {
    self = [super init];

    self.componentId = node.nodeId;
    self.name = node.data[@"name"];
    self.props = node.data[@"passProps"];

    return self;
}

@end
