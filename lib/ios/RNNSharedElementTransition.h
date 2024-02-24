#import "RNNAnimatedReactView.h"
#import "RNNBaseAnimator.h"
#import "RNNElementAnimator.h"
#import "RNNSharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNSharedElementTransition : RNNElementAnimator

- (instancetype)initWithTransitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView
                            containerView:(UIView *)containerView;

@property(nonatomic, strong) RNNAnimatedReactView *view;
@property(nonatomic, strong) UIView *parentView;

@end
