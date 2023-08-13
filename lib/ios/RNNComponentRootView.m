#import "RNNComponentRootView.h"

@implementation RNNComponentRootView

- (instancetype)init {
    self = [super init];
    self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
    return self;
}

#ifndef RCT_NEW_ARCH_ENABLED
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
    [bridge.uiManager setAvailableSize:UIScreen.mainScreen.bounds.size forRootView:self];
    return self;
}
#endif

- (NSString *)componentType {
    return ComponentTypeScreen;
}

@end
