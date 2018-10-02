#import "RNNLayoutProtocol.h"
#import "RNNLeafProtocol.h"

@protocol RNNParentProtocol <RNNLayoutProtocol, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo options:(RNNNavigationOptions *)options presenter:(RNNBasePresenter *)presenter;

- (void)bindChildrenViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers;

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;

@end
