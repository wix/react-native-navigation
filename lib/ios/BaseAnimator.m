#import "BaseAnimator.h"

@implementation BaseAnimator

- (void)updateAnimations:(NSTimeInterval)elapsed {
    CGAffineTransform transform = CGAffineTransformIdentity;
    for (id<DisplayLinkAnimation> animation in self.animations) {
        if (elapsed < animation.duration && elapsed > animation.startDelay) {
            CGFloat p = (elapsed-animation.startDelay)/(animation.duration-animation.startDelay);
            transform = CGAffineTransformConcat(transform, [animation animateWithProgress:p]);
        }
    }
    
    self.view.transform = transform;
}


- (NSTimeInterval)maxDuration {
    CGFloat maxDuration = 0;
    for (id<DisplayLinkAnimation> animation in self.animations) {
        if (animation.duration > maxDuration) {
            maxDuration = animation.duration;
        }
    }
    
    return maxDuration;
}

- (void)end {
    for (id<DisplayLinkAnimatorDelegate> animation in self.animations) {
        [animation end];
    }
}

@end
