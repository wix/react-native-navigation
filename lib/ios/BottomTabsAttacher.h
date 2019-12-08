#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"

@interface BottomTabsAttacher : NSObject

- (instancetype)initWithOptions:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)attach:(UITabBarController *)bottomTabsController;

@end
