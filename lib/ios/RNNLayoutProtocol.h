#import "RNNLayoutInfo.h"
#import "RNNViewControllerPresenter.h"
#import "RNNLeafProtocol.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLayoutProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (UIViewController<RNNLayoutProtocol> *)getCurrentChild;

- (UIViewController<RNNLeafProtocol, RNNLayoutProtocol> *)getCurrentLeaf;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)overrideOptions:(RNNNavigationOptions *)options;

@end
