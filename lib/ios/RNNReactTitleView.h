#import "RNNReactView.h"

@interface RNNReactTitleView : RNNReactView <RCTRootViewDelegate>

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame;

@property (nonatomic, copy) void (^rootViewDidChangeIntrinsicSize)(CGSize intrinsicSize);

@end
