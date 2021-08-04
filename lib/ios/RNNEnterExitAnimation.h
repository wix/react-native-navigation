#import "TransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNEnterExitAnimation : RNNOptions

@property(nonatomic, strong) TransitionOptions *enter;
@property(nonatomic, strong) TransitionOptions *exit;

- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;

@end
