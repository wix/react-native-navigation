#import "DisplayLinkAnimatorDelegate.h"
#import "RNNElementAnimator.h"
#import "RNNSharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNElementTransitionsCreator : NSObject

+ (NSArray<DisplayLinkAnimatorDelegate> *)
           create:(NSArray<RNNElementTransitionOptions *> *)elementTransitions
           fromVC:(UIViewController *)fromVC
             toVC:(UIViewController *)toVC
    containerView:(UIView *)containerView;

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition
                                               view:(UIView *)view
                                      containerView:(UIView *)containerView;

@end
