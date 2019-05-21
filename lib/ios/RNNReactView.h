#ifdef RN_FABRIC_ENABLED
#import <React/RCTFabricSurfaceHostingProxyRootView.h>
#else
#import <React/RCTRootView.h>
#endif

#import <React/RCTRootViewDelegate.h>

typedef void (^RNNReactViewReadyCompletionBlock)(void);

#ifdef RN_FABRIC_ENABLED
@interface RNNReactView : RCTFabricSurfaceHostingProxyRootView <RCTRootViewDelegate>
#else
@interface RNNReactView : RCTRootView <RCTRootViewDelegate>
#endif

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties availableSize:(CGSize)availableSize reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);
@property (nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame;

@end
