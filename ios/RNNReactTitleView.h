#import "RNNComponentView.h"

@interface RNNReactTitleView : RNNComponentView
#ifndef RCT_NEW_ARCH_ENABLED
                               <RCTRootViewDelegate>
#endif

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame;

@property(nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);

@end
