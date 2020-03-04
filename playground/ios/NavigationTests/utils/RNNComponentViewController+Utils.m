#import "RNNComponentViewController+Utils.h"

@implementation RNNComponentViewController (Utils)

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.componentId = componentId;
	return [[RNNComponentViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:nil presenter:nil options:nil defaultOptions:nil];
}

@end
