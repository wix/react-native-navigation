#import "BottomTabsAfterInitialTabAttacher.h"
#import "UITabBarController+RNNUtils.h"

@implementation BottomTabsAfterInitialTabAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
      [bottomTabsController readyForPresentation];
      for (UIViewController *viewController in bottomTabsController.deselectedViewControllers) {
        dispatch_async(dispatch_get_main_queue(), ^{
          UIWindow *preloadWindow = [[UIWindow alloc] initWithFrame:CGRectZero];
          // Clip the preload window so the deselected tab's full-screen reactView,
          // which is briefly hosted here while it renders, can't draw outside the
          // zero-frame window and flicker over the already-visible selected tab.
          preloadWindow.clipsToBounds = YES;
          preloadWindow.hidden = NO;

          dispatch_group_t ready = dispatch_group_create();
          dispatch_group_enter(ready);

          viewController.waitForRender = YES;

          [viewController setReactViewReadyCallback:^{
            dispatch_group_leave(ready);
          }];

          [viewController render];

          UIView *containerView = nil;
          UIView *reactView = nil;
          if ([viewController isKindOfClass:[UINavigationController class]]) {
              containerView = [(UINavigationController *)viewController topViewController].view;
              reactView = containerView.subviews.firstObject;
          }

          if (reactView && !reactView.window) {
              [preloadWindow addSubview:reactView];
          }

          dispatch_notify(ready, dispatch_get_main_queue(), ^{
            if (reactView) {
                reactView.frame = containerView.bounds;
                [containerView addSubview:reactView];
            }
            preloadWindow.hidden = YES;
          });
        });
      }
    }];

    [bottomTabsController.selectedViewController render];
}

@end
