#import "ElementTransitionsCreator.h"

@implementation ElementTransitionsCreator {
	NSArray<ElementTransitionOptions *>* _elementTransitions;
}

- (instancetype)initWithElementTransitions:(NSArray<ElementTransitionOptions *> *)elementTransitions {
    self = [super init];
	_elementTransitions = elementTransitions;
    return self;
}

- (NSArray<DisplayLinkAnimatorDelegate>*)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    NSMutableArray<DisplayLinkAnimatorDelegate>* transitions = [NSMutableArray<DisplayLinkAnimatorDelegate> new];
    for (ElementTransitionOptions* transitionOptions in _elementTransitions) {
        ElementTransition* elementTransition = [[ElementTransition alloc] initWithTransitionOptions:transitionOptions
                                                                                             fromVC:fromVC
                                                                                               toVC:toVC
                                                                                      containerView:containerView];
        [transitions addObject:elementTransition];
    }
    
    return transitions;
}

@end
