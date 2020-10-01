#import "ContentTransitionCreator.h"

@implementation ContentTransitionCreator

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition view:(UIView *)view fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView reversed:(BOOL)reversed {
   if (!elementTransition.alpha.hasAnimation) {
       elementTransition.alpha = [self defaultAlphaTransitionForOperation:reversed];
   }
    
    return [super createTransition:elementTransition view:view fromVC:fromVC toVC:toVC containerView:containerView];
}

+ (TransitionDetailsOptions *)defaultAlphaTransitionForOperation:(BOOL)reversed {
    CGFloat from = reversed ? 1 : 0;
    CGFloat to = reversed ? 0 : 1;
    TransitionDetailsOptions* defaultAlphaTransition = [TransitionDetailsOptions new];
    defaultAlphaTransition.duration = [TimeInterval withValue:300];
    defaultAlphaTransition.from = [Double withValue:from];
    defaultAlphaTransition.to = [Double withValue:to];
    return defaultAlphaTransition;
}

@end
