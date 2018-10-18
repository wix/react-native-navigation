#import "RNNLayoutInfo.h"
#import "RNNViewControllerPresenter.h"
#import "RNNLeafProtocol.h"

@protocol RNNLayoutProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (UIViewController<RNNLeafProtocol, RNNLayoutProtocol> *)getCurrentChild;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

@end
