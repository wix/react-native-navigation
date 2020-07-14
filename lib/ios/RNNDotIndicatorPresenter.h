#import <Foundation/Foundation.h>
#import "DotIndicatorOptions.h"
#import "RNNNavigationOptions.h"

@interface RNNDotIndicatorPresenter : NSObject
@property(nonatomic, strong) RNNNavigationOptions* defaultOptions;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)apply:(UIViewController *)child :(DotIndicatorOptions *)options;

- (void)bottomTabsDidLayoutSubviews:(UITabBarController *)bottomTabs;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions child:(UIViewController *)child;

@end
