#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNModalAnimation.h"

@interface RNNPushAnimation : RNNModalAnimation <UIViewControllerAnimatedTransitioning>

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition;

@end
