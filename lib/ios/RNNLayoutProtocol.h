#import "RNNLayoutInfo.h"
#import "RNNViewControllerPresenter.h"
#import "RNNLeafProtocol.h"

@protocol RNNLayoutProtocol <RNNOptionsDelegate, NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, retain) RNNViewControllerPresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (UIViewController<RNNLeafProtocol, RNNLayoutProtocol> *)getLeafViewController;

@end
