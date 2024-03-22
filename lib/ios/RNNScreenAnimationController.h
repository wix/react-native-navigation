#import "RNNScreenTransition.h"
#import <Foundation/Foundation.h>
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerObserverCoordinator.h>
#import <React/RCTUIManagerUtils.h>

@interface RNNScreenAnimationController
    : NSObject <UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning,
                RCTUIManagerObserver>

- (instancetype)initWithContentTransition:(RNNEnterExitAnimation *)contentTransition
                       elementTransitions:(NSArray<RNNElementTransitionOptions *> *)elementTransitions
                 sharedElementTransitions:
                     (NSArray<RNNSharedElementTransitionOptions *> *)sharedElementTransitions
                                 duration:(CGFloat)duration
                                   bridge:(RCTBridge *)bridge;

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView;

@property(nonatomic, strong) RNNEnterExitAnimation *content;
@property(nonatomic, strong) NSArray<RNNElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<RNNSharedElementTransitionOptions *> *sharedElementTransitions;

@end
