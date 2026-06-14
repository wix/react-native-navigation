#import "RNNBasePresenter.h"
#import "RNNTabBarItemCreator.h"
@interface BottomTabPresenter : RNNBasePresenter

@property(nonatomic, strong, readonly) RNNTabBarItemCreator *tabCreator;

/**
 * When YES, tabs whose options declare `bottomTab.component` skip native
 * icon/text/sfSymbol/role application. The accompanying
 * `RNNCustomTabBarItemView` is responsible for visual rendering of the tab.
 */
@property(nonatomic, assign) BOOL useCustomItemViews;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions
                            tabCreator:(RNNTabBarItemCreator *)tabCreator;

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child;

- (void)createTabBarItem:(UIViewController *)child
        bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

- (void)mergeOptions:(RNNNavigationOptions *)options
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child;

@end
