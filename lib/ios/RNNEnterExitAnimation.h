#import "RNNEnterExitAnimation.h"
#import "RNNSharedElementTransitionOptions.h"
#import "RNNTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNEnterExitAnimation : RNNOptions

@property(nonatomic, strong) NSArray<RNNElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<RNNSharedElementTransitionOptions *> *sharedElementTransitions;
@property(nonatomic, strong) RNNTransitionOptions *enter;
@property(nonatomic, strong) RNNTransitionOptions *exit;

- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;

@end
