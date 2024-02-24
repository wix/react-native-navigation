#import "RNNDisplayLinkAnimatorDelegateProtocol.h"
#import "RNNElementAnimator.h"
#import "RNNSharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNElementTransitionsCreator : NSObject

+ (NSArray<RNNDisplayLinkAnimatorDelegateProtocol> *)
           create:(NSArray<RNNElementTransitionOptions *> *)elementTransitions
           fromVC:(UIViewController *)fromVC
             toVC:(UIViewController *)toVC
    containerView:(UIView *)containerView;

+ (id<RNNDisplayLinkAnimatorDelegateProtocol>)createTransition:(TransitionOptions *)elementTransition
                                               view:(UIView *)view
                                      containerView:(UIView *)containerView;

@end
