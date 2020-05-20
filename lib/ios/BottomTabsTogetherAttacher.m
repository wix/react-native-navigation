#import "BottomTabsTogetherAttacher.h"

@implementation BottomTabsTogetherAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    for (UIViewController* childViewController in bottomTabsController.children) {
        [childViewController render];
    }
    
    [bottomTabsController readyForPresentation];
}

@end
