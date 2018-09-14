#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "RNNLayoutInfo.h"

@protocol RNNRootViewProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@optional

- (void)performOnRotation:(void (^)(void))block;

@required

- (RNNRootViewController *)getLeafViewController;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;

@end


