#import <Foundation/Foundation.h>
#import "TopBarPresenter.h"

@interface TopBarPresenterCreator : NSObject

+ (TopBarPresenter *)createWithBoundedNavigationController:(UINavigationController *)navigationController;

@end
