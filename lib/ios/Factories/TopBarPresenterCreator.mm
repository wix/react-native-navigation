#import "TopBarPresenterCreator.h"
#import "TopBarAppearancePresenter.h"

@implementation TopBarPresenterCreator

+ (TopBarPresenter *)createWithBoundedNavigationController:
    (UINavigationController *)navigationController {
    if (@available(iOS 13.0, *)) {
        return
            [[TopBarAppearancePresenter alloc] initWithNavigationController:navigationController];
    } else {
        return [[TopBarPresenter alloc] initWithNavigationController:navigationController];
    }
}

@end
