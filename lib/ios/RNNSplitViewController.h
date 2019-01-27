#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNSplitViewOptions.h"
#import "RNNAnimator.h"
#import "RNNTopTabsViewController.h"
#import "RNNParentProtocol.h"
#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"

@interface RNNSplitViewController : UISplitViewController <RNNParentProtocol>

@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNSplitViewControllerPresenter* presenter;

@end
