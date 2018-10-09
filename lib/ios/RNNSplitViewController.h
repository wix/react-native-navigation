
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNSplitViewOptions.h"
#import "RNNAnimator.h"
#import "RNNTopTabsViewController.h"
#import "RNNParentProtocol.h"

@interface RNNSplitViewController : UISplitViewController <RNNParentProtocol>

@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;

@end
