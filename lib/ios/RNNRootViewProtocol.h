#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@optional

- (void)performOnRotation:(void (^)(void))block;

@required
- (RNNRootViewController *)getLeafViewController;

@end


