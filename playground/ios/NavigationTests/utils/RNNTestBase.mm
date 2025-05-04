#import "RNNTestBase.h"

@interface RNNTestBase ()
@property(nonatomic, strong) UIWindow *window;
@end

@implementation RNNTestBase

- (void)setupTopLevelUI:(UIViewController *)withViewController {
    [withViewController viewDidLoad];
    [withViewController viewWillAppear:YES];
    [withViewController viewDidAppear:YES];
}

- (void)tearDownTopLevelUI:(UIViewController *)withViewController {
    [withViewController viewWillDisappear:YES];
    [withViewController viewDidDisappear:YES];
}
@end