#import "RNNOverlayManager.h"
#import "RNNOverlayWindow.h"

@implementation RNNOverlayManager

- (instancetype)init {
    self = [super init];
    _overlayWindows = [[NSMutableArray alloc] init];
    _keyOverlayWindows = [[NSMutableArray alloc] init];
    return self;
}

#pragma mark - public

- (void)showOverlayWindow:(RNNOverlayWindow *)overlayWindow withOptions:(NSDictionary *)options {
    [_overlayWindows addObject:overlayWindow];
    overlayWindow.rootViewController.view.backgroundColor = [UIColor clearColor];
    [overlayWindow setWindowLevel:UIWindowLevelNormal];
    if (options[@"becomeKeyWindow"]) {
        [overlayWindow makeKeyAndVisible];
        [_keyOverlayWindows addObject: overlayWindow];
    } else {
        [overlayWindow setHidden:NO];
    }
}

- (void)dismissOverlay:(UIViewController*)viewController {
    RNNOverlayWindow* overlayWindow = [self findWindowByRootViewController:viewController];
    [self detachOverlayWindow:overlayWindow];
    [self assignKeyWindow];
}

#pragma mark - private

- (void)detachOverlayWindow:(UIWindow *)overlayWindow {
    [overlayWindow setHidden:YES];
    [overlayWindow setRootViewController:nil];
    [_overlayWindows removeObject:overlayWindow];
    [_keyOverlayWindows removeObject:overlayWindow];
}

- (void)assignKeyWindow {
    UIWindow *nextKeyWindow = [_keyOverlayWindows firstObject];
    if (!nextKeyWindow) {
        nextKeyWindow =[[[UIApplication sharedApplication] delegate] window];
    }
    [nextKeyWindow makeKeyWindow];
}

- (RNNOverlayWindow *)findWindowByRootViewController:(UIViewController *)viewController {
    for (RNNOverlayWindow* window in _overlayWindows) {
        if ([window.rootViewController isEqual:viewController]) {
            return window;
        }
    }
    
    return nil;
}

@end
