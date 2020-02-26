#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import "RNNBottomTabsController.h"

@interface RNNBottomTabsController (Helpers)

+ (RNNBottomTabsController *)create;

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children;

@end
