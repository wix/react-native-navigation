#import "RNNTestBase.h"

@interface RNNTestBase ()
@property(nonatomic, strong) UIWindow *window;
@end

@implementation RNNTestBase

- (void)setupTopLevelUI:(UIViewController *)withViewController {
    self.window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
    _window.rootViewController = withViewController;
    [_window setHidden:NO];
    [withViewController viewWillAppear:NO];
    [withViewController viewDidAppear:NO];
}

- (void)tearDownTopLevelUI {
    [_window.rootViewController viewWillDisappear:NO];
    [_window.rootViewController viewDidDisappear:NO];
    [_window setHidden:YES];
    _window.rootViewController = nil;
    self.window = nil;
}
@end