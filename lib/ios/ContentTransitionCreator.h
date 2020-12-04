#import "ElementTransitionsCreator.h"

@interface ContentTransitionCreator : ElementTransitionsCreator

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition
                                               view:(UIView *)view
                                             fromVC:(UIViewController *)fromVC
                                               toVC:(UIViewController *)toVC
                                      containerView:(UIView *)containerView
                                           reversed:(BOOL)reversed;

@end
