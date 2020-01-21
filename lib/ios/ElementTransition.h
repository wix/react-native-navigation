#import <Foundation/Foundation.h>
#import "ElementTransitionOptions.h"
#import "BaseAnimator.h"

@interface ElementTransition : BaseAnimator

- (instancetype)initWithTransitionOptions:(ElementTransitionOptions *)transitionOptions fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

- (NSArray<id<DisplayLinkAnimation>> *)createAnimations;

@end
