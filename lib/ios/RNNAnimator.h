#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNElementView.h"
#import "RNNTransitionOptions.h"

@interface RNNAnimator : NSObject <UIViewControllerAnimatedTransitioning>

-(instancetype)initWithTransitionOptions:(RNNTransitionOptions *)transitionOptions;

@end
