#import <Foundation/Foundation.h>

typedef void (^RNNAnimationEndedBlock)(void);

@interface AnimationObserver : NSObject

+ (AnimationObserver *)sharedObserver;

@property(nonatomic) BOOL isAnimating;

- (void)registerAnimationEndedBlock:(RNNAnimationEndedBlock)block;

- (void)beginAnimation;

- (void)endAnimation;

@end
