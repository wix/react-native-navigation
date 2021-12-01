#import "AnimatedReactView.h"
#import "BaseAnimator.h"
#import "ElementAnimator.h"
#import "SharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface SharedElementTransition : ElementAnimator

- (instancetype)initWithTransitionOptions:(SharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView
                            containerView:(UIView *)containerView;

@property(nonatomic, strong) AnimatedReactView *view;
@property(nonatomic, strong) UIView *parentView;

@end
