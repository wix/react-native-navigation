#import "ContentTransitionCreator.h"

@implementation ContentTransitionCreator

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(TransitionOptions *)elementTransition view:(UIView *)view fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
   if (!elementTransition.alpha.hasAnimation) {
       elementTransition.alpha = self.defaultAlphaTransition;
   }
    
    return [super createTransition:elementTransition view:view fromVC:fromVC toVC:toVC containerView:containerView];
}

+ (TransitionDetailsOptions *)defaultAlphaTransition {
    TransitionDetailsOptions* defaultAlphaTransition = [TransitionDetailsOptions new];
    defaultAlphaTransition.duration = [TimeInterval withValue:300];
    defaultAlphaTransition.from = [Double withValue:0];
    defaultAlphaTransition.to = [Double withValue:1];
    return defaultAlphaTransition;
}

@end
