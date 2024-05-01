#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTSurfaceHostingProxyRootView.h>
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
@class RCTHost;

@protocol RNNComponentProtocol <NSObject>

- (NSString *)componentId;

- (NSString *)componentType;

- (void)componentWillAppear;

- (void)componentDidAppear;

- (void)componentDidDisappear;

@end

#ifdef RCT_NEW_ARCH_ENABLED
@interface RNNReactView
    : RCTSurfaceHostingView <RCTRootViewDelegate, RNNComponentProtocol>
#else
@interface RNNReactView : RCTRootView <RCTRootViewDelegate, RNNComponentProtocol>
#endif

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
               sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;
#else
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;
#endif

#pragma mark - Bridgeless

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithHost:(RCTHost *)host
                  moduleName:(NSString *)moduleName
           initialProperties:(NSDictionary *)initialProperties
                eventEmitter:(RNNEventEmitter *)eventEmitter
             sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
         reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;
#endif

@property(nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;
@property(nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (atomic, readonly) NSString *moduleName;
@property (atomic, copy, readwrite) NSDictionary *properties;
@property (nonatomic, strong, readonly) UIView *view;
@property (nonatomic, strong, readonly) UIView *contentView;
@property (nonatomic, assign) RCTRootViewSizeFlexibility sizeFlexibility;
@property (atomic, readwrite, weak, nullable) id<RCTSurfaceDelegate> delegate;
@property (nonatomic, assign) BOOL passThroughTouches;

- (void)invalidate;

@end
