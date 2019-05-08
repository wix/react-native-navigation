
#import "RNNReactRootViewCreator.h"
#import "RNNReactView.h"

@implementation RNNReactRootViewCreator {
	RCTBridge *_bridge;
}

- (instancetype)initWithBridge:(RCTBridge*)bridge {
	self = [super init];
	
	_bridge = bridge;
	
	return self;
}

- (RNNReactView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId availableSize:(CGSize)availableSize reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	if (!rootViewId) {
		@throw [NSException exceptionWithName:@"MissingViewId" reason:@"Missing view id" userInfo:nil];
	}
	
	RNNReactView *view = [[RNNReactView alloc] initWithBridge:_bridge
												   moduleName:name
											initialProperties:@{@"componentId": rootViewId}
												availableSize:availableSize
										  reactViewReadyBlock:reactViewReadyBlock];
	return view;
}

- (UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions {
	return [self createRootView:componentOptions.name.get rootViewId:componentOptions.componentId.get availableSize:CGSizeZero reactViewReadyBlock:nil];
}

- (UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	return [self createRootView:componentOptions.name.get rootViewId:componentOptions.componentId.get availableSize:CGSizeZero reactViewReadyBlock:reactViewReadyBlock];
}

@end
