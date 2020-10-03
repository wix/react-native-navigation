#ifdef RN_FABRIC_ENABLED
#import <React/RCTFabricSurfaceHostingProxyRootView.h>
#else
#import <React/RCTRootView.h>
#endif

#import "RNNEventEmitter.h"
#import "UIView+Utils.h"
#import <React/RCTRootViewDelegate.h>
#import <React/RCTUIManager.h>

#define ComponentTypeScreen @"Component"
#define ComponentTypeTitle @"TopBarTitle"
#define ComponentTypeButton @"TopBarButton"
#define ComponentTypeBackground @"TopBarBackground"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

#ifdef RN_FABRIC_ENABLED
@interface RNNReactView
    : RCTFabricSurfaceHostingProxyRootView <RCTRootViewDelegate>
#else
@interface RNNReactView : RCTRootView <RCTRootViewDelegate>
#endif

- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
           reactViewReadyBlock:
               (RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@property(nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;
@property(nonatomic, strong) RNNEventEmitter *eventEmitter;

- (NSString *)componentId;

- (NSString *)componentType;

- (void)componentDidAppear;

- (void)componentDidDisappear;

- (void)invalidate;

@end
