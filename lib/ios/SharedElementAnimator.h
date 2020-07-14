#import <Foundation/Foundation.h>
#import "SharedElementTransitionOptions.h"
#import "BaseAnimator.h"
#import "AnimatedReactView.h"
#import "ElementAnimator.h"

@interface SharedElementAnimator : ElementAnimator

- (instancetype)initWithTransitionOptions:(SharedElementTransitionOptions *)transitionOptions fromView:(UIView *)fromView toView:(UIView *)toView fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

@property (nonatomic, strong) AnimatedReactView* view;

@end
