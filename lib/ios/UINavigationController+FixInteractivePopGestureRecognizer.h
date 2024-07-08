#import "PanDirectionGestureRecognizer.h"
#import <UIKit/UIKit.h>

@interface UINavigationController (FixInteractivePopGestureRecognizer)

- (void)fixInteractivePopGestureRecognizerWithDelegate:(id<UIGestureRecognizerDelegate>)delegate;

@end
