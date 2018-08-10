#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@optional

- (void)performOnRotation:(void (^)(void))block;
- (void)applyTabBarItem;

@required
- (RNNRootViewController *)getLeafViewController;

@end


