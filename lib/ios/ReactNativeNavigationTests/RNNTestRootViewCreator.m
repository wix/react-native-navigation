#import "RNNTestRootViewCreator.h"

@implementation RNNTestRootViewCreator

- (UIView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	UIView *view = [[UIView alloc] init];
	return view;
}

@end
