#import "DisplayLinkAnimatorDelegate.h"
#import "ElementAnimator.h"
#import "SharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface ElementTransitionsCreator : NSObject

+ (NSArray<DisplayLinkAnimatorDelegate> *)
           create:(NSArray<ElementTransitionOptions *> *)elementTransitions
           fromVC:(UIViewController *)fromVC
             toVC:(UIViewController *)toVC
    containerView:(UIView *)containerView;

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition
                                               view:(UIView *)view
                                      containerView:(UIView *)containerView;

@end
