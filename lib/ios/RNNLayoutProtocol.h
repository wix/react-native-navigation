#import "RNNLayoutInfo.h"
#import "RNNBasePresenter.h"

@protocol RNNLayoutProtocol <NSObject, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (void)mergeOptions:(RNNNavigationOptions *)options;

@end
