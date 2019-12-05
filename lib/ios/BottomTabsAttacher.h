#import <Foundation/Foundation.h>
#import "RNNBottomTabsController.h"

@interface BottomTabsAttacher : NSObject

- (void)attach:(RNNBottomTabsController *)bottomTabsController withMode:(BottomTabsAttachMode)tabsAttachMode;

@end
