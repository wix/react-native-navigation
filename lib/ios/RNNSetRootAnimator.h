#import <Foundation/Foundation.h>
#import "TransitionOptions.h"

typedef void (^RNNAnimationEndedBlock)(void);

@interface RNNSetRootAnimator : NSObject

- (instancetype)initWithTransition:(TransitionOptions *)transition;

- (void)animate:(UIWindow *)window completion:(RNNAnimationEndedBlock)completion;

@end
