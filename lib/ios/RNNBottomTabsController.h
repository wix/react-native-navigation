#import "BottomTabPresenter.h"
#import "BottomTabsBaseAttacher.h"
#import "RNNBottomTabsPresenter.h"
#import "RNNDotIndicatorPresenter.h"
#import "RNNEventEmitter.h"
#import "UIViewController+LayoutProtocol.h"
#import <UIKit/UIKit.h>

@interface RNNBottomTabsController
    : UITabBarController <RNNLayoutProtocol, UITabBarControllerDelegate>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                bottomTabPresenter:(BottomTabPresenter *)bottomTabPresenter
             dotIndicatorPresenter:(RNNDotIndicatorPresenter *)dotIndicatorPresenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers
                bottomTabsAttacher:(BottomTabsBaseAttacher *)bottomTabsAttacher;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

- (void)setTabBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)setTabBarVisible:(BOOL)visible;

- (void)handleTabBarLongPress:(CGPoint)locationInTabBar;

@property(nonatomic, strong) NSArray *pendingChildViewControllers;

@end
