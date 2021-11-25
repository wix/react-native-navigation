#import "AnimationObserver.h"

@implementation AnimationObserver {
    RNNAnimationEndedBlock _animationEndedBlock;
}

+ (AnimationObserver *)sharedObserver {
    static AnimationObserver *_sharedObserver = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      _sharedObserver = [[AnimationObserver alloc] init];
    });

    return _sharedObserver;
}

- (void)registerAnimationEndedBlock:(RNNAnimationEndedBlock)block {
    _animationEndedBlock = block;
}

- (void)beginAnimation {
    _isAnimating = YES;
}

- (void)endAnimation {
    _isAnimating = NO;

    if (_animationEndedBlock) {
        _animationEndedBlock();
        _animationEndedBlock = nil;
    }
}

@end
