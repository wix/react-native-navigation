#import "RNNBottomTabsPresenter.h"
#import <Foundation/Foundation.h>

@interface BottomTabsPresenterCreator : NSObject

+ (BottomTabsBasePresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
