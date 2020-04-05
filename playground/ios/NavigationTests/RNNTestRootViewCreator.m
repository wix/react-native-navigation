#import "RNNTestRootViewCreator.h"

@implementation RNNTestRootViewCreator

- (RNNReactView *)createRootView:(NSString *)name rootViewId:(NSString *)rootViewId ofType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	UIView *view = [[UIView alloc] initWithFrame:UIScreen.mainScreen.bounds];
	UILabel* label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 200)];
	label.textAlignment = NSTextAlignmentCenter;
	label.center = [view convertPoint:view.center fromView:view.superview];;
	label.text = rootViewId;
	[view addSubview:label];
	view.tag = [rootViewId intValue];
	view.backgroundColor = UIColor.redColor;
	return view;
}

@end
