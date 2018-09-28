#import "RNNLayoutProtocol.h"
#import "RNNLeafProtocol.h"

@protocol RNNParentProtocol <RNNLayoutProtocol, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;
- (void)performOnChildWillAppear:(RNNNavigationOptions *)options;
- (void)performOnChildLoad:(RNNNavigationOptions *)options;

@end
