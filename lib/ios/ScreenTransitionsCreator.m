#import "ScreenTransitionsCreator.h"
#import "SharedElementTransitionsCreator.h"
#import "ElementTransitionsCreator.h"
#import "ContentTransitionCreator.h"

@implementation ScreenTransitionsCreator {
	RNNScreenTransition* _screenTransition;
	SharedElementTransitionsCreator* _sharedElementTransitionsCreator;
	ElementTransitionsCreator* _elementTransitionsCreator;
}

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition {
    self = [super init];
    _screenTransition = screenTransition;
	_sharedElementTransitionsCreator = [[SharedElementTransitionsCreator alloc] initWithSharedElementTransitions:screenTransition.sharedElementTransitions];
	_elementTransitionsCreator = [[ElementTransitionsCreator alloc] initWithElementTransitions:screenTransition.elementTransitions];
    return self;
}

- (NSArray<id<DisplayLinkAnimatorDelegate>> *)createFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
	NSMutableArray* transitions = [NSMutableArray new];
    ContentTransitionCreator* contentTransition = [[ContentTransitionCreator alloc] initWithTransitionOptions:_screenTransition.content fromVC:fromVC toVC:toVC containerView:containerView view:toVC.view];
    [transitions addObject:contentTransition];
    [transitions addObjectsFromArray:[_elementTransitionsCreator createFromVC:fromVC toVC:toVC containerView:containerView]];
    [transitions addObjectsFromArray:[_sharedElementTransitionsCreator createFromVC:fromVC toVC:toVC containerView:containerView]];
	
	return transitions;
}

- (NSTimeInterval)minDuration {
    return _screenTransition.minDuration;
}

@end
