#import "DisplayLinkAnimator.h"

@implementation DisplayLinkAnimator {
    NSArray<id<DisplayLinkAnimatorDelegate>>* _animators;
    NSMutableArray<id<DisplayLinkAnimatorDelegate>>* _activeAnimators;
    CADisplayLink* _displayLink;
    NSDate* _startDate;
    CGFloat _duration;
}

- (instancetype)initWithDisplayLinkAnimators:(NSArray<id<DisplayLinkAnimatorDelegate>> *)displayLinkAnimators duration:(CGFloat)duration {
    self = [super init];
    _animators = displayLinkAnimators;
    _activeAnimators = [NSMutableArray arrayWithArray:displayLinkAnimators];
    _duration = [self maxDuration:displayLinkAnimators];
    return self;
}

- (instancetype)initWithDisplayLinkAnimator:(id<DisplayLinkAnimatorDelegate>)displayLinkAnimator duration:(CGFloat)duration {
	self = [self initWithDisplayLinkAnimators:@[displayLinkAnimator] duration:duration];
    return self;
}

- (void)start {
    _startDate = NSDate.date;
	_displayLink = [CADisplayLink displayLinkWithTarget:self selector:@selector(_displayLinkDidTick:)];
	[_displayLink addToRunLoop:NSRunLoop.mainRunLoop forMode:NSDefaultRunLoopMode];
}

- (CGFloat)maxDuration:(NSArray<id<DisplayLinkAnimatorDelegate>> *)displayLinkAnimators {
    CGFloat maxDuration = 0;
    for (id<DisplayLinkAnimatorDelegate> animator in displayLinkAnimators) {
        if (animator.maxDuration > maxDuration) {
            maxDuration = animator.maxDuration;
        }
    }
    
    return maxDuration;
}

- (void)_displayLinkDidTick:(CADisplayLink*)displayLink {
	NSTimeInterval elapsed = [NSDate.date timeIntervalSinceDate:_startDate];
	if (elapsed > _duration) {
		[self updateAnimators:_duration];
        [self end];
		[displayLink invalidate];
        if (_completion) {
            _completion();
            _completion = nil;
        }
		return;
	}
	
    [self updateAnimators:elapsed];
}

- (void)updateAnimators:(NSTimeInterval)elapsed {
    for (int i = 0; i < _activeAnimators.count; i++) {
        id<DisplayLinkAnimatorDelegate> animator = _activeAnimators[i];
        if (elapsed < animator.maxDuration) {
            [animator updateAnimations:elapsed];
        } else {
            [self deactivateAnimator:animator];
        }
    }
}

- (void)deactivateAnimator:(id<DisplayLinkAnimatorDelegate>)animator {
    [animator end];
    [_activeAnimators removeObject:animator];
}

- (void)end {
    for (int i = 0; i < _activeAnimators.count; i++) {
        id<DisplayLinkAnimatorDelegate> animator = _activeAnimators[i];
        [animator end];
    }
    
    _activeAnimators = nil;
}

@end
