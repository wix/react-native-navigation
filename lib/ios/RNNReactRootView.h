#import <UIKit/UIKit.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>

@interface RNNReactRootView : RCTRootView <RCTRootViewDelegate>

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);

- (void)setAlignment:(NSString*)alignment;

@end
