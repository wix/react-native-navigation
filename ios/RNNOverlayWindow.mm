#import "RNNOverlayWindow.h"
#import "RNNReactView.h"

@implementation RNNOverlayWindow

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    static Class modalHostViewClass;
    static Class rootComponentViewClass;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      modalHostViewClass = NSClassFromString(@"RCTModalHostView");
      rootComponentViewClass = NSClassFromString(@"RCTRootComponentView");
    });

    UIView *hitTestResult = [super hitTest:point withEvent:event];

    if ([hitTestResult isKindOfClass:[UIWindow class]] ||
        [hitTestResult.subviews.firstObject isKindOfClass:RNNReactView.class] ||
        [hitTestResult isKindOfClass:modalHostViewClass] ||
        [hitTestResult isKindOfClass:rootComponentViewClass]) {
        return nil;
    }

    return hitTestResult;
}

@end
