
#import "RNNReactRootViewCreator.h"
#import "RNNReactRootView.h"

@implementation RNNReactRootViewCreator {
	RCTBridge *_bridge;
}

-(instancetype)initWithBridge:(RCTBridge*)bridge {
	self = [super init];
	
	_bridge = bridge;
	
	return self;
	
}

- (UIView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId {
	if (!rootViewId) {
		@throw [NSException exceptionWithName:@"MissingViewId" reason:@"Missing view id" userInfo:nil];
	}
	
	UIView *view = [[RNNReactRootView alloc] initWithBridge:_bridge
										 moduleName:name
								  initialProperties:@{@"componentId": rootViewId}];
	return view;
}

-(UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions {
	return [self createRootView:componentOptions.name rootViewId:componentOptions.componentId];
}

@end
