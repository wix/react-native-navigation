#import "RNNElementView.h"
#import <UIKit/UIKit.h>

@interface RNNInteractivePopAnimator
    : UIPercentDrivenInteractiveTransition <UINavigationControllerDelegate,
                                            UIViewControllerAnimatedTransitioning,
                                            UIViewControllerInteractiveTransitioning>

@property(nonatomic, strong) RNNElementView *topView;
@property(nonatomic, strong) RNNElementView *bottomView;
@property(nonatomic, strong) UIViewController *vc;
@property(nonatomic) CGRect originFrame;
@property CGPoint toCenter;

- (instancetype)initWithTopView:(RNNElementView *)topView
                  andBottomView:(RNNElementView *)bottomView
                 andOriginFrame:(CGRect)originFrame
              andViewController:(UIViewController *)vc;
- (void)handleGesture:(UIPanGestureRecognizer *)recognizer;

@end
