#import "RNNOverlayWindow.h"
#import "RNNReactView.h"
#import <React/RCTModalHostView.h>

@implementation RNNOverlayWindow

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    UIView *hitTestResult = [super hitTest:point withEvent:event];

    if ([hitTestResult isKindOfClass:[UIWindow class]] ||
        [hitTestResult.subviews.firstObject isKindOfClass:RNNReactView.class] ||
        [hitTestResult isKindOfClass:[RCTModalHostView class]]) {
        return nil;
    }

    return hitTestResult;
}

@end
