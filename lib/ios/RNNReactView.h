#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>

@interface RNNReactView : RCTRootView <RCTRootViewDelegate>

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);

- (void)setAlignment:(NSString *)alignment;

@end
