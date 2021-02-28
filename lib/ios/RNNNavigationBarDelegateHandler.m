#import "RNNNavigationBarDelegateHandler.h"

@implementation RNNNavigationBarDelegateHandler {
    BOOL _isPopping;
}

- (void)popViewControllerAnimated:(BOOL)animated {
    _isPopping = YES;
}

- (BOOL)navigationController:(UINavigationController *)navigationController
               shouldPopItem:(BOOL)shouldPopItem {
    if (@available(iOS 13.0, *)) {
        return shouldPopItem;
    } else {
        if (_isPopping) {
            return YES;
        } else if (shouldPopItem) {
            [navigationController popViewControllerAnimated:YES];
            _isPopping = NO;
            return YES;
        } else {
            return NO;
        }
    }
}

- (void)navigationBar:(UINavigationBar *)navigationBar didPopItem:(UINavigationItem *)item {
    _isPopping = NO;
}

@end
