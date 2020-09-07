#import "RNNSetRootAnimator.h"

@implementation RNNSetRootAnimator {
    TransitionOptions* _transition;
}

- (instancetype)initWithTransition:(TransitionOptions *)transition {
    self = [super init];
    _transition = transition;
    return self;
}

- (void)animate:(UIWindow *)window completion:(RNNAnimationEndedBlock)completion {
    [window.rootViewController.view setNeedsDisplay];
    [UIView transitionWithView:window
                      duration:self.duration
                       options:UIViewAnimationOptionTransitionCrossDissolve
                    animations:^{
        [window.rootViewController.view.layer displayIfNeeded];
    }
                    completion:^(BOOL finished) {
        if (completion) {
            completion();
        }
    }];
}

- (CGFloat)duration {
    return [_transition.alpha.duration getWithDefaultValue:0];
}

@end
