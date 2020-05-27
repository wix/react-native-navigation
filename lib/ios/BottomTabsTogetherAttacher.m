#import "BottomTabsTogetherAttacher.h"
#import "RNNBottomTabsController.h"

@implementation BottomTabsTogetherAttacher

- (void)attach:(RNNBottomTabsController *)bottomTabsController {
    for (UIViewController* childViewController in bottomTabsController.pendingChildViewControllers) {
        [childViewController render];
    }
    
    [bottomTabsController readyForPresentation];
}

@end
