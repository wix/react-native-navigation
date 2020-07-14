#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNScreenTransition.h"
#import "TransitionDelegate.h"

@interface StackTransitionDelegate : TransitionDelegate

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition bridge:(RCTBridge *)bridge;

@end
