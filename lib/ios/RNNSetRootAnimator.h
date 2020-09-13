#import "TransitionOptions.h"
#import <Foundation/Foundation.h>

typedef void (^RNNAnimationEndedBlock)(void);

@interface RNNSetRootAnimator : NSObject

- (void)animate:(UIWindow *)window
       duration:(CGFloat)duration
     completion:(RNNAnimationEndedBlock)completion;

@end
