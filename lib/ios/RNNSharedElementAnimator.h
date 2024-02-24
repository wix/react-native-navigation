#import "AnimatedReactView.h"
#import "DisplayLinkAnimatorDelegate.h"
#import <Foundation/Foundation.h>

@interface RNNSharedElementAnimator : NSObject

- (instancetype)initWithTransitions:
                    (NSArray<RNNSharedElementTransitionOptions *> *)sharedElementTransitions
                             fromVC:(UIViewController *)fromVC
                               toVC:(UIViewController *)toVC
                      containerView:(UIView *)containerView;

- (NSArray<DisplayLinkAnimatorDelegate> *)create;

- (void)animationEnded;

@end
