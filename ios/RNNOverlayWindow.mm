#import "RNNOverlayWindow.h"
#import "RNNReactView.h"
#ifndef RCT_NEW_ARCH_ENABLED
#import <React/RCTModalHostView.h>
#endif

@implementation RNNOverlayWindow

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    UIView *hitTestResult = [super hitTest:point withEvent:event];

    if ([hitTestResult isKindOfClass:[UIWindow class]] ||
        [hitTestResult.subviews.firstObject isKindOfClass:RNNReactView.class] ||
        [hitTestResult isKindOfClass:NSClassFromString(@"RCTModalHostView")] ||
        [hitTestResult isKindOfClass:NSClassFromString(@"RCTRootComponentView")]) {
        return nil;
    }

    return hitTestResult;
}

@end
