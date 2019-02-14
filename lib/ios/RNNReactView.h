#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNReactView : RCTRootView <RCTRootViewDelegate>

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);
@property (nonatomic, copy) RNNReactViewReadyCompletionBlock reactViewReadyBlock;

- (void)setAlignment:(NSString *)alignment;

@end
