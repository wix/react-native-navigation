#import "UINavigationController+FixInteractivePopGestureRecognizer.h"

@implementation UINavigationController (FixInteractivePopGestureRecognizer)

- (void)fixInteractivePopGestureRecognizerWithDelegate:(id<UIGestureRecognizerDelegate>)delegate {
    UIGestureRecognizer *popGestureRecognizer = self.interactivePopGestureRecognizer;
    NSArray *targets = [popGestureRecognizer valueForKey:@"targets"];
    NSArray *gestureRecognizers = self.view.gestureRecognizers;

    if (!popGestureRecognizer || targets.count == 0 || !gestureRecognizers)
        return;

    if (self.viewControllers.count == 1) {
        for (UIGestureRecognizer *recognizer in gestureRecognizers) {
            if ([recognizer isKindOfClass:[PanDirectionGestureRecognizer class]]) {
                [self.view removeGestureRecognizer:recognizer];
                popGestureRecognizer.enabled = NO;
                recognizer.delegate = nil;
            }
        }
    } else {
        if (gestureRecognizers.count == 1) {
            PanDirectionGestureRecognizer *gestureRecognizer =
                [[PanDirectionGestureRecognizer alloc] initWithAxis:PanAxisHorizontal
                                                          direction:PanDirectionRight
                                                             target:nil
                                                             action:nil];
            gestureRecognizer.cancelsTouchesInView = YES;
            [gestureRecognizer setValue:targets forKey:@"targets"];
            [gestureRecognizer requireGestureRecognizerToFail:popGestureRecognizer];
            gestureRecognizer.delegate = delegate;
            popGestureRecognizer.enabled = YES;

            [self.view addGestureRecognizer:gestureRecognizer];
        }
    }
}

@end
