#import "BaseAnimator.h"

@implementation BaseAnimator {
    NSMutableArray* _mutableAnimations;
}

- (void)setAnimations:(NSArray<id<DisplayLinkAnimation>> *)animations {
    _animations = animations;
    _mutableAnimations = [NSMutableArray arrayWithArray:animations];
}

- (void)updateAnimations:(NSTimeInterval)elapsed {
    CATransform3D transform = CATransform3DIdentity;
    for (int i = 0; i < _mutableAnimations.count; i++) {
        id<DisplayLinkAnimation> animation = _mutableAnimations[i];
        if (elapsed < animation.duration + animation.startDelay && elapsed > animation.startDelay) {
            CGFloat p = (elapsed-animation.startDelay)/(animation.duration-animation.startDelay);
            transform = CATransform3DConcat(transform, [animation animateWithProgress:p]);
        } else if (elapsed >= animation.duration + animation.startDelay) {
            transform = CATransform3DConcat(transform, [animation animateWithProgress:1]);
            [animation end];
            [_mutableAnimations removeObject:animation];
        }
    }
    
    self.view.layer.transform = transform;
}

- (NSTimeInterval)maxDuration {
    CGFloat maxDuration = 0;
    for (id<DisplayLinkAnimation> animation in _animations) {
        if (animation.duration + animation.startDelay > maxDuration) {
            maxDuration = animation.duration;
        }
    }
    
    return maxDuration;
}

- (void)end {
    for (id<DisplayLinkAnimatorDelegate> animation in _animations) {
        if ([animation respondsToSelector:@selector(end)]) {
            [animation end];
        }
    }
}

@end
