#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNScreenTransition.h"
#import "TransitionDelegate.h"

@interface StackTransitionDelegate : TransitionDelegate

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition uiManager:(RCTUIManager *)uiManager;

@end
