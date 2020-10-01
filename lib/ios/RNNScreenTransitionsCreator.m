#import "RNNScreenTransitionsCreator.h"
#import "DisplayLinkAnimatorDelegate.h"
#import "ElementTransitionsCreator.h"
#import "SharedElementTransitionsCreator.h"
#import "ContentTransitionCreator.h"

@implementation RNNScreenTransitionsCreator

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView screenTransition:(RNNScreenTransition *)screenTransition reversed:(BOOL)reversed {
    NSArray* elementTransitions = [ElementTransitionsCreator create:screenTransition.elementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    NSArray* sharedElementTransitions = [SharedElementTransitionsCreator create:screenTransition.sharedElementTransitions fromVC:fromVC toVC:toVC containerView:containerView];
    id<DisplayLinkAnimatorDelegate> contentTransition = [self createContentTransitionFromVC:fromVC toVC:toVC containerView:containerView screenTransition:screenTransition reversed:reversed];
    
    
    return [[[NSArray arrayWithObject:contentTransition] arrayByAddingObjectsFromArray:elementTransitions] arrayByAddingObjectsFromArray:sharedElementTransitions];
}

+ (id<DisplayLinkAnimatorDelegate>)createContentTransitionFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView screenTransition:(RNNScreenTransition *)screenTransition reversed:(BOOL)reversed {
    UIView* contentView = reversed ? fromVC.view : toVC.view;
    return [ContentTransitionCreator createTransition:screenTransition.content view:contentView fromVC:fromVC toVC:toVC containerView:containerView reversed:reversed];
}

@end
