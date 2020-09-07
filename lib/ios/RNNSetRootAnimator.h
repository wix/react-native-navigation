#import <Foundation/Foundation.h>
#import "TransitionOptions.h"

@interface RNNSetRootAnimator : NSObject

- (instancetype)initWithTransition:(TransitionOptions *)transition;

- (void)animate:(UIWindow *)window;

@end
