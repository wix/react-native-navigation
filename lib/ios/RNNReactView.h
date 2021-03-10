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

@protocol RNNComponentProtocol <NSObject>

- (NSString *)componentId;

- (NSString *)componentType;

- (void)componentWillAppear;

- (void)componentDidAppear;

- (void)componentDidDisappear;

@end

#ifdef RN_FABRIC_ENABLED
@interface RNNReactView
    : RCTFabricSurfaceHostingProxyRootView <RCTRootViewDelegate, RNNComponentProtocol>
#else
@interface RNNReactView : RCTRootView <RCTRootViewDelegate, RNNComponentProtocol>
#endif

- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@property(nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;
@property(nonatomic, strong) RNNEventEmitter *eventEmitter;

- (void)invalidate;

@end
