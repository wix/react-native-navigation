#import <UIKit/UIKit.h>
#import "RNNEventEmitter.h"
#import "RNNBottomTabsPresenter.h"
#import "UIViewController+LayoutProtocol.h"
#import "RCTConvert+RNNOptions.h"
#import "BottomTabsAttacher.h"

@interface RNNBottomTabsController : UITabBarController <RNNLayoutProtocol, UITabBarControllerDelegate>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers
                bottomTabsAttacher:(BottomTabsAttacher *)bottomTabsAttacher;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@end
