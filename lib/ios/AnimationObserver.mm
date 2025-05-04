#import "AnimationObserver.h"

@implementation AnimationObserver {
    NSMutableArray<RNNAnimationEndedBlock> *_animationEndedBlocks;
}

- (instancetype)init {
    self = [super init];
    _animationEndedBlocks = [NSMutableArray array];
    return self;
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
    [_animationEndedBlocks addObject:block];
}

- (void)beginAnimation {
    _isAnimating = YES;
}

- (void)endAnimation {
    _isAnimating = NO;

    for (RNNAnimationEndedBlock block in _animationEndedBlocks) {
        block();
    }

    [_animationEndedBlocks removeAllObjects];
}

@end
