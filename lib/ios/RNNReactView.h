#import <React/RCTFabricSurfaceHostingProxyRootView.h>
#import <React/RCTRootViewDelegate.h>

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNReactView : RCTFabricSurfaceHostingProxyRootView <RCTRootViewDelegate>

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties availableSize:(CGSize)availableSize reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);
@property (nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame;

@end
