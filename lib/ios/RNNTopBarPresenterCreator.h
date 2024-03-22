#import "RNNTopBarPresenter.h"
#import <Foundation/Foundation.h>

@interface RNNTopBarPresenterCreator : NSObject

+ (RNNTopBarPresenter *)createWithBoundedNavigationController:
    (UINavigationController *)navigationController;

@end
