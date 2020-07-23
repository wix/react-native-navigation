#import "ContentTransitionCreator.h"

@implementation ContentTransitionCreator

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition view:(UIView *)view fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView operation:(UINavigationControllerOperation)operation {
   if (!elementTransition.alpha.hasAnimation) {
       elementTransition.alpha = [self defaultAlphaTransitionForOperation:operation];
   }
    
    return [super createTransition:elementTransition view:view fromVC:fromVC toVC:toVC containerView:containerView];
}

+ (TransitionDetailsOptions *)defaultAlphaTransitionForOperation:(UINavigationControllerOperation)operation {
    CGFloat from = operation == UINavigationControllerOperationPush ? 0 : 1;
    CGFloat to = operation == UINavigationControllerOperationPush ? 1 : 0;
    TransitionDetailsOptions* defaultAlphaTransition = [TransitionDetailsOptions new];
    defaultAlphaTransition.duration = [TimeInterval withValue:300];
    defaultAlphaTransition.from = [Double withValue:from];
    defaultAlphaTransition.to = [Double withValue:to];
    return defaultAlphaTransition;
}

@end
