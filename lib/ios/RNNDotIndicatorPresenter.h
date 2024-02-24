#import "RNNDotIndicatorOptions.h"
#import "RNNNavigationOptions.h"
#import <Foundation/Foundation.h>

@interface RNNDotIndicatorPresenter : NSObject
@property(nonatomic, strong) RNNNavigationOptions *defaultOptions;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)apply:(UIViewController *)child options:(RNNDotIndicatorOptions *)options;

- (void)bottomTabsDidLayoutSubviews:(UITabBarController *)bottomTabs;

- (void)mergeOptions:(NSDictionary *)options
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child;

@end
