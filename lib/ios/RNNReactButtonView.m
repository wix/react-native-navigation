#import "RNNReactButtonView.h"

@implementation RNNReactButtonView

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

- (NSString *)componentType {
    return ComponentTypeButton;
}

@end
