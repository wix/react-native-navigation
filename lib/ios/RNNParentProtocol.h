#import "RNNLayoutProtocol.h"
#import "RNNLeafProtocol.h"

@protocol RNNParentProtocol <RNNLayoutProtocol, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter;

- (void)onChildWillAppear;

@end
