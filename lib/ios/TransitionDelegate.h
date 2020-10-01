#import <Foundation/Foundation.h>
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerUtils.h>
#import <React/RCTUIManagerObserverCoordinator.h>
#import "RNNScreenTransition.h"

@interface TransitionDelegate : NSObject <UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning, RCTUIManagerObserver>

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition bridge:(RCTBridge *)bridge;

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView;

@property (nonatomic, strong) RNNScreenTransition* screenTransition;

@end
