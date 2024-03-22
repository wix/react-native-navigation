#import "RNNBaseAnimator.h"

@implementation RNNBaseAnimator {
    NSMutableArray *_mutableAnimations;
}

- (void)setAnimations:(NSArray<id<RNNDisplayLinkAnimation>> *)animations {
    _animations = animations;
    _mutableAnimations = [NSMutableArray arrayWithArray:animations];
}

- (void)updateAnimations:(NSTimeInterval)elapsed {
    CATransform3D transform = CATransform3DIdentity;
    NSMutableIndexSet *discardedAnimations = [NSMutableIndexSet indexSet];

    for (int i = 0; i < _mutableAnimations.count; i++) {
        id<RNNDisplayLinkAnimation> animation = _mutableAnimations[i];
        if (elapsed < animation.duration + animation.startDelay && elapsed > animation.startDelay) {
            CGFloat p =
                (elapsed - animation.startDelay) / (animation.duration - animation.startDelay);
            transform = CATransform3DConcat(transform, [animation animateWithProgress:p]);
        } else if (elapsed >= animation.duration + animation.startDelay) {
            transform = CATransform3DConcat(transform, [animation animateWithProgress:1]);
            [animation end];
            [discardedAnimations addIndex:i];
        }
    }

    [_mutableAnimations removeObjectsAtIndexes:discardedAnimations];

    self.view.layer.transform = transform;
}

- (NSTimeInterval)maxDuration {
    CGFloat maxDuration = 0;
    for (id<RNNDisplayLinkAnimation> animation in _animations) {
        if (animation.duration + animation.startDelay > maxDuration) {
            maxDuration = animation.duration;
        }
    }

    return maxDuration;
}

- (void)end {
    CATransform3D transform = CATransform3DIdentity;
    for (id<RNNDisplayLinkAnimation> animation in _mutableAnimations) {
        if ([animation respondsToSelector:@selector(end)]) {
            transform = CATransform3DConcat(transform, [animation animateWithProgress:1]);
            [animation end];
        }
    }

    self.view.layer.transform = transform;
}

@end
