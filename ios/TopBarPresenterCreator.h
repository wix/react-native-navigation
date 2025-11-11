#import "TopBarPresenter.h"
#import <Foundation/Foundation.h>

@interface TopBarPresenterCreator : NSObject

+ (TopBarPresenter *)createWithBoundedNavigationController:
    (UINavigationController *)navigationController;

@end
