#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNScreenTransition.h"
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerUtils.h>
#import <React/RCTUIManagerObserverCoordinator.h>

@interface CustomTransitionDelegate : NSObject <UIViewControllerAnimatedTransitioning, RCTUIManagerObserver>

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition uiManager:(RCTUIManager *)uiManager;

@end
