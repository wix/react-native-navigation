#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "RNNLayoutInfo.h"
#import "RNNLeafProtocol.h"

@protocol RNNParentProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;

@end
