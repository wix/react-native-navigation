#import "RNNBottomTabsController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

@interface RNNBottomTabsController (Helpers)

+ (RNNBottomTabsController *)create;

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children;

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children
                                        options:(RNNNavigationOptions *)options;

@end
