#import "RNNBasePresenter.h"
#import "RNNTabBarItemCreator.h"
@interface BottomTabPresenter : RNNBasePresenter

@property(nonatomic, strong, readonly) RNNTabBarItemCreator *tabCreator;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions
                            tabCreator:(RNNTabBarItemCreator *)tabCreator;

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child;

- (void)createTabBarItem:(UIViewController *)child
        bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

- (void)mergeOptions:(RNNNavigationOptions *)options
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child;

@end
