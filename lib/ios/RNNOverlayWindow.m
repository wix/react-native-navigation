#import "RNNOverlayWindow.h"
#import "RNNReactView.h"

@implementation RNNOverlayWindow

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    UIView *hitTestResult = [super hitTest:point withEvent:event];

    if ([hitTestResult isKindOfClass:[UIWindow class]] ||
        ([hitTestResult.subviews count] > 0
         && [hitTestResult.subviews[0] isKindOfClass:RNNReactView.class])) {
        return nil;
    }

    return hitTestResult;
}

@end
