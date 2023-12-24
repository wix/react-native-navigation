#import "UIViewController+Utils.h"

@implementation UIViewController (Utils)

+ (instancetype)snapshotKeyWindow {
    UIViewController *vc = [UIViewController new];
    UIView *snapshotView =
        [[[UIApplication sharedApplication] keyWindow] snapshotViewAfterScreenUpdates:YES];
    [vc.view addSubview:snapshotView];
    return vc;
}

- (void)forEachChild:(void (^)(UIViewController *child))perform {
    for (UIViewController *child in [self childViewControllers]) {
        perform(child);
    }
}

- (BOOL)isLastInStack {
    return self.navigationController.childViewControllers.lastObject == self;
}

@end
