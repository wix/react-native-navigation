#import "RNNEnterExitAnimation.h"
#import "SharedElementTransitionOptions.h"
#import "TransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNEnterExitAnimation : RNNOptions

@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<SharedElementTransitionOptions *> *sharedElementTransitions;
@property(nonatomic, strong) TransitionOptions *enter;
@property(nonatomic, strong) TransitionOptions *exit;

- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;

@end
