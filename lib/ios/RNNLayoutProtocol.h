#import "RNNLayoutInfo.h"
#import "RNNViewControllerPresenter.h"
#import "RNNLeafProtocol.h"

@protocol RNNLayoutProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

- (UIViewController<RNNLeafProtocol, RNNLayoutProtocol> *)getCurrentChild;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)overrideOptions:(RNNNavigationOptions *)options;

@end
