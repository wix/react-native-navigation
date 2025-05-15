#import "RNNSetRootAnimator.h"

@implementation RNNSetRootAnimator

- (void)animate:(UIWindow *)window
       duration:(CGFloat)duration
     completion:(RNNAnimationEndedBlock)completion {
    if (duration > 0) {
        [window.rootViewController.view setNeedsDisplay];
        [UIView transitionWithView:window
            duration:duration
            options:UIViewAnimationOptionTransitionCrossDissolve
            animations:^{
              [window.rootViewController.view.layer displayIfNeeded];
            }
            completion:^(BOOL finished) {
              if (completion)
                  completion();
            }];
    } else if (completion)
        completion();
}

@end
