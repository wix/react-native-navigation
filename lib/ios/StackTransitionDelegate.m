#import "StackTransitionDelegate.h"
#import "SharedElementTransitionsCreator.h"
#import "ElementTransitionsCreator.h"
#import "ContentTransitionCreator.h"

@implementation StackTransitionDelegate {
    RNNScreenTransition* _screenTransition;
}

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition bridge:(RCTBridge *)bridge {
    self = [super initWithBridge:bridge];
    _screenTransition = screenTransition;
    return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
    return _screenTransition.maxDuration;
}

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    ContentTransitionCreator* contentTransition = [ContentTransitionCreator createTransition:_screenTransition.content view:toVC.view fromVC:fromVC toVC:toVC containerView:containerView];
    
    NSArray* elementTransitions = [ElementTransitionsCreator create:_screenTransition.elementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    NSArray* sharedElementTransitions = [SharedElementTransitionsCreator create:_screenTransition.sharedElementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    
    
    return [[[NSArray arrayWithObject:contentTransition] arrayByAddingObjectsFromArray:elementTransitions] arrayByAddingObjectsFromArray:sharedElementTransitions];
}

@end
