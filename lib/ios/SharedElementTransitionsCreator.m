#import "SharedElementTransitionsCreator.h"
#import "RNNElementFinder.h"
#import "AnimatedViewFactory.h"
#import "BaseAnimator.h"

@implementation SharedElementTransitionsCreator {
	NSArray<SharedElementTransitionOptions *>* _sharedElementTransitions;
}

- (instancetype)initWithSharedElementTransitions:(NSArray<SharedElementTransitionOptions *>*)sharedElementTransitions {
    self = [super init];
	_sharedElementTransitions = sharedElementTransitions;
    return self;
}

- (NSArray<DisplayLinkAnimatorDelegate>*)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    NSMutableArray<DisplayLinkAnimatorDelegate>* transitions = [NSMutableArray<DisplayLinkAnimatorDelegate> new];
    for (SharedElementTransitionOptions* transitionOptions in _sharedElementTransitions) {
        AnimatedReactView* animatedView = [self createAnimatedView:transitionOptions fromVC:fromVC toVC:toVC];
        BaseAnimator* animator = [[BaseAnimator alloc] init];
        animator.animations = @[animatedView];
        animator.view = animatedView;
        [transitions addObject:animator];
    }
	
	[self addSharedElementViews:transitions toContainerView:containerView];
    
    return transitions;
}

- (void)addSharedElementViews:(NSArray<BaseAnimator *> *)animators toContainerView:(UIView *)containerView {
    for (BaseAnimator* animator in animators) {
        [containerView addSubview:animator.view];
        [containerView bringSubviewToFront:animator.view];
    }
}

- (AnimatedReactView *)createAnimatedView:(SharedElementTransitionOptions *)transitionOptions fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC {
    UIView *fromElement = [RNNElementFinder findElementForId:transitionOptions.fromId inView:fromVC.view];
    UIView *toElement = [RNNElementFinder findElementForId:transitionOptions.toId inView:toVC.view];
    return [AnimatedViewFactory createFromElement:fromElement toElement:toElement transitionOptions:transitionOptions];
}


@end
