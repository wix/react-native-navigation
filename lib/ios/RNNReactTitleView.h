#import "RNNComponentView.h"

@interface RNNReactTitleView : RNNComponentView <RCTRootViewDelegate>

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame;

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);

@end
