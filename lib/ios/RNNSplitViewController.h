#import <UIKit/UIKit.h>
#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"
#import "RNNLayoutProtocol.h"

@interface RNNSplitViewController : UISplitViewController <RNNLayoutProtocol>

- (void)bindChildViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers;

@end
