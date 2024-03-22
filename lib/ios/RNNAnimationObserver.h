#import <Foundation/Foundation.h>

typedef void (^RNNAnimationEndedBlock)(void);

@interface RNNAnimationObserver : NSObject

+ (RNNAnimationObserver *)sharedObserver;

@property(nonatomic) BOOL isAnimating;

- (void)registerAnimationEndedBlock:(RNNAnimationEndedBlock)block;

- (void)beginAnimation;

- (void)endAnimation;

@end
