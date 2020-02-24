#import <Foundation/Foundation.h>
#import "SharedElementTransitionOptions.h"
#import "ElementAnimator.h"
#import "DisplayLinkAnimatorDelegate.h"

@interface ElementTransitionsCreator : NSObject

+ (NSArray<DisplayLinkAnimatorDelegate>*)create:(NSArray<ElementTransitionOptions *>*)elementTransitions fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition view:(UIView *)view fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

@end
