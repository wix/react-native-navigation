#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNSharedElementAnimationOptions.h"

@interface RNNAnimator : NSObject <UIViewControllerAnimatedTransitioning>

-(instancetype)initWithTransitionOptions:(RNNSharedElementAnimationOptions *)transitionOptions;

@end
