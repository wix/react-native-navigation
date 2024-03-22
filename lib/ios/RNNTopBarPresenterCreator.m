#import "RNNTopBarPresenterCreator.h"
#import "RNNTopBarAppearancePresenter.h"

@implementation RNNTopBarPresenterCreator

+ (RNNTopBarPresenter *)createWithBoundedNavigationController:
    (UINavigationController *)navigationController {
    if (@available(iOS 13.0, *)) {
        return
            [[RNNTopBarAppearancePresenter alloc] initWithNavigationController:navigationController];
    } else {
        return [[RNNTopBarPresenter alloc] initWithNavigationController:navigationController];
    }
}

@end
