#import "RNNTestRootViewCreator.h"

@implementation RNNTestRootViewCreator

- (RNNReactView *)createRootView:(NSString *)name rootViewId:(NSString *)rootViewId ofType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	UIView *view = [[UIView alloc] init];
	view.tag = [rootViewId intValue];
	return view;
}

@end
