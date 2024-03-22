#import "RNNAnimatedReactView.h"
#import "RNNDisplayLinkAnimatorDelegateProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNSharedElementAnimator : NSObject

- (instancetype)initWithTransitions:
                    (NSArray<RNNSharedElementTransitionOptions *> *)sharedElementTransitions
                             fromVC:(UIViewController *)fromVC
                               toVC:(UIViewController *)toVC
                      containerView:(UIView *)containerView;

- (NSArray<RNNDisplayLinkAnimatorDelegateProtocol> *)create;

- (void)animationEnded;

@end
