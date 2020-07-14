
#import "RNNReactRootViewCreator.h"
#import "RNNReactView.h"
#import "RNNComponentRootView.h"

@implementation RNNReactRootViewCreator {
	RCTBridge *_bridge;
    RNNEventEmitter* _eventEmitter;
}

- (instancetype)initWithBridge:(RCTBridge*)bridge eventEmitter:(RNNEventEmitter*)eventEmitter {
	self = [super init];
	_bridge = bridge;
    _eventEmitter = eventEmitter;
	return self;
}

- (RNNReactView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId ofType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    [self verifyRootViewId:rootViewId];
    return [[[self resolveComponentViewClass:componentType] alloc] initWithBridge:_bridge
             moduleName:name
      initialProperties:@{@"componentId": rootViewId}
           eventEmitter:_eventEmitter
    reactViewReadyBlock:reactViewReadyBlock];
}

- (Class)resolveComponentViewClass:(RNNComponentType)componentType {
    switch (componentType) {
        case RNNComponentTypeTopBarTitle:
            return RNNReactTitleView.class;
        case RNNComponentTypeTopBarButton:
            return RNNReactButtonView.class;
        case RNNComponentTypeTopBarBackground:
            return RNNReactBackgroundView.class;
        case RNNComponentTypeComponent:
        default:
            return RNNComponentRootView.class;
    }
}

- (void)verifyRootViewId:(NSString *)rootViewId {
    if (!rootViewId) {
        @throw [NSException exceptionWithName:@"MissingViewId" reason:@"Missing view id" userInfo:nil];
    }
}

@end
