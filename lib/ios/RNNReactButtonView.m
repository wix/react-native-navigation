#import "RNNReactButtonView.h"

@implementation RNNReactButtonView
#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
               sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithBridge:bridge
                      moduleName:moduleName
               initialProperties:initialProperties
                    eventEmitter:eventEmitter
                 sizeMeasureMode:RCTSurfaceSizeMeasureModeWidthUndefined |
                                 RCTSurfaceSizeMeasureModeHeightUndefined
             reactViewReadyBlock:reactViewReadyBlock];

    return self;
}
#else
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithBridge:bridge
                      moduleName:moduleName
               initialProperties:initialProperties
                    eventEmitter:eventEmitter
             reactViewReadyBlock:reactViewReadyBlock];

    return self;
}
#endif

- (NSString *)componentType {
    return ComponentTypeButton;
}

@end
