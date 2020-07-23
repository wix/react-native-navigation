#import "StackTransitionDelegate.h"
#import "SharedElementTransitionsCreator.h"
#import "ElementTransitionsCreator.h"
#import "ContentTransitionCreator.h"

@implementation StackTransitionDelegate {
    RNNScreenTransition* _screenTransition;
    UINavigationControllerOperation _operation;
}

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition bridge:(RCTBridge *)bridge operation:(UINavigationControllerOperation)operation {
    self = [super initWithBridge:bridge];
    _screenTransition = screenTransition;
    _operation = operation;
    return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
    return _screenTransition.maxDuration;
}

- (void)prepareTransitionContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView* toView = [transitionContext viewForKey:UITransitionContextToViewKey];
    UIView* fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];
    if (_operation == UINavigationControllerOperationPush) {
        toView.alpha = 0;
        [transitionContext.containerView addSubview:fromView];
        [transitionContext.containerView addSubview:toView];
    } else {
        [transitionContext.containerView addSubview:toView];
        [transitionContext.containerView addSubview:fromView];
    }
}

- (id<DisplayLinkAnimatorDelegate>)createContentTransitionFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    UIView* contentView = _operation == UINavigationControllerOperationPush ? toVC.view : fromVC.view;
    return [ContentTransitionCreator createTransition:_screenTransition.content view:contentView fromVC:fromVC toVC:toVC containerView:containerView operation:_operation];
}

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    NSArray* elementTransitions = [ElementTransitionsCreator create:_screenTransition.elementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    NSArray* sharedElementTransitions = [SharedElementTransitionsCreator create:_screenTransition.sharedElementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    id<DisplayLinkAnimatorDelegate> contentTransition = [self createContentTransitionFromVC:fromVC toVC:toVC containerView:containerView];
    
    
    return [[[NSArray arrayWithObject:contentTransition] arrayByAddingObjectsFromArray:elementTransitions] arrayByAddingObjectsFromArray:sharedElementTransitions];
}

@end
