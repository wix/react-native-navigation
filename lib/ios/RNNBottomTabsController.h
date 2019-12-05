#import <UIKit/UIKit.h>
#import "RNNEventEmitter.h"
#import "RNNBottomTabsPresenter.h"
#import "UIViewController+LayoutProtocol.h"
#import "RCTConvert+RNNOptions.h"

@interface RNNBottomTabsController : UITabBarController <RNNLayoutProtocol, UITabBarControllerDelegate>

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@end
