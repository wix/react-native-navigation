#import "RNNReactButtonView.h"

@implementation RNNReactButtonView

- (instancetype)initWithHost:(RCTHost *)host
                  moduleName:(NSString *)moduleName
           initialProperties:(NSDictionary *)initialProperties
                eventEmitter:(RNNEventEmitter *)eventEmitter
             sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
         reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithHost:host
                    moduleName:moduleName
             initialProperties:initialProperties
                  eventEmitter:eventEmitter
               sizeMeasureMode:RCTSurfaceSizeMeasureModeWidthAtMost |
                               RCTSurfaceSizeMeasureModeHeightAtMost
           reactViewReadyBlock:reactViewReadyBlock];

    return self;
}

- (NSString *)componentType {
    return ComponentTypeButton;
}

- (CGSize)intrinsicContentSize {
    return CGSizeMake(50, 50);
}

@end
