#import "ElementTransitionsCreator.h"

@interface ContentTransitionCreator : ElementTransition

- (instancetype)initWithTransitionOptions:(ElementTransitionOptions *)transitionOptions fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView view:(UIView *)view;

@end
