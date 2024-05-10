#import "AnimatedReactView.h"
#import "DisplayLinkAnimatorDelegate.h"
#import <Foundation/Foundation.h>

@interface SharedElementAnimator : NSObject

- (instancetype)initWithTransitions:
                    (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
                             fromVC:(UIViewController *)fromVC
                               toVC:(UIViewController *)toVC
                      containerView:(UIView *)containerView;

- (NSArray<DisplayLinkAnimatorDelegate> *)create;

- (void)animationEnded;

@end
