#import "RNNEnterExitAnimation.h"
#import "RNNSharedElementTransitionOptions.h"
#import "TransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNEnterExitAnimation : RNNOptions

@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<RNNSharedElementTransitionOptions *> *sharedElementTransitions;
@property(nonatomic, strong) TransitionOptions *enter;
@property(nonatomic, strong) TransitionOptions *exit;

- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;

@end
