#import "RNNSetRootAnimator.h"

@implementation RNNSetRootAnimator {
    TransitionOptions* _transition;
}

- (instancetype)initWithTransition:(TransitionOptions *)transition {
    self = [super init];
    _transition = transition;
    return self;
}

- (void)animate:(UIWindow *)window {
    [window.rootViewController.view setNeedsDisplay];
    [UIView transitionWithView:window
                      duration:self.duration
                       options:UIViewAnimationOptionTransitionCrossDissolve
                    animations:^{
        [window.rootViewController.view.layer displayIfNeeded];
    }
                    completion:nil];
}

- (CGFloat)duration {
    return [_transition.alpha.duration getWithDefaultValue:0];
}

@end
