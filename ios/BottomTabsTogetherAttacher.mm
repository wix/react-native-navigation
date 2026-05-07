#import "BottomTabsTogetherAttacher.h"
#import "RNNBottomTabsController.h"

@implementation BottomTabsTogetherAttacher

- (void)attach:(RNNBottomTabsController *)bottomTabsController {
    dispatch_group_t ready = dispatch_group_create();
    
    UIWindow *preloadWindow = [[UIWindow alloc] initWithFrame:CGRectZero];
    // Clip the preload window so the full-screen reactViews hosted here while
    // they render can't draw outside the zero-frame window. Today the splash
    // screen masks this, but without clipping it's a latent flicker.
    preloadWindow.clipsToBounds = YES;
    preloadWindow.hidden = NO;
    
    NSMapTable *reactViewToParent = [NSMapTable strongToStrongObjectsMapTable];
    
    for (UIViewController *vc in bottomTabsController.childViewControllers) {
        dispatch_group_enter(ready);
        [vc setReactViewReadyCallback:^{
            dispatch_group_leave(ready);
        }];
        
        [vc render];
        
        if ([vc isKindOfClass:[UINavigationController class]]) {
            UIView *containerView = [(UINavigationController *)vc topViewController].view;
            UIView *reactView = containerView.subviews.firstObject;
            
            if (reactView && !reactView.window) {
                [reactViewToParent setObject:containerView forKey:reactView];
                [preloadWindow addSubview:reactView];
            }
        }
    }
    
    dispatch_notify(ready, dispatch_get_main_queue(), ^{
        for (UIView *reactView in reactViewToParent) {
            UIView *parent = [reactViewToParent objectForKey:reactView];
            reactView.frame = parent.bounds;
            [parent addSubview:reactView];
        }
        preloadWindow.hidden = YES; //Keep preloadWindow reference alive to this point
        [bottomTabsController readyForPresentation];
    });
}

@end
