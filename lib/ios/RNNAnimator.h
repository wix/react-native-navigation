#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNAnimationOptions.h"

@interface RNNAnimator : NSObject <UIViewControllerAnimatedTransitioning>

-(instancetype)initWithTransitionOptions:(RNNAnimationOptions *)transitionOptions;

@end
