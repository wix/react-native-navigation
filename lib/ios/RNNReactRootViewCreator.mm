
#import "RNNReactRootViewCreator.h"
#import "RNNComponentRootView.h"
#import "RNNReactTitleView.h"
#import "RNNReactView.h"

@implementation RNNReactRootViewCreator {
    RCTBridge *_bridge;
#ifdef RCT_NEW_ARCH_ENABLED
	RCTHost *_host;
#endif
    RNNEventEmitter *_eventEmitter;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge eventEmitter:(RNNEventEmitter *)eventEmitter {
    self = [super init];
    _bridge = bridge;
    _eventEmitter = eventEmitter;
    return self;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithHost:(RCTHost *)host eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [super init];
	_host = host;
	_eventEmitter = eventEmitter;
	return self;
}
#endif

- (RNNReactView *)createRootView:(NSString *)name
                      rootViewId:(NSString *)rootViewId
                          ofType:(RNNComponentType)componentType
             reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    [self verifyRootViewId:rootViewId];
#ifdef RCT_NEW_ARCH_ENABLED
	if (_host != nil) {
		return [[[self resolveComponentViewClass:componentType] alloc]
				   initWithHost:_host
					 moduleName:name
			  initialProperties:@{@"componentId" : rootViewId}
				   eventEmitter:_eventEmitter
				sizeMeasureMode:RCTSurfaceSizeMeasureModeWidthExact |
								RCTSurfaceSizeMeasureModeHeightExact
			reactViewReadyBlock:reactViewReadyBlock];
	} else {
		return [[[self resolveComponentViewClass:componentType] alloc]
				 initWithBridge:_bridge
					 moduleName:name
			  initialProperties:@{@"componentId" : rootViewId}
				   eventEmitter:_eventEmitter
				sizeMeasureMode:RCTSurfaceSizeMeasureModeWidthExact |
								RCTSurfaceSizeMeasureModeHeightExact
			reactViewReadyBlock:reactViewReadyBlock];
	}
#else
    return [[[self resolveComponentViewClass:componentType] alloc]
             initWithBridge:_bridge
                 moduleName:name
          initialProperties:@{@"componentId" : rootViewId}
               eventEmitter:_eventEmitter
        reactViewReadyBlock:reactViewReadyBlock];
#endif
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
        @throw [NSException exceptionWithName:@"MissingViewId"
                                       reason:@"Missing view id"
                                     userInfo:nil];
    }
}

@end
