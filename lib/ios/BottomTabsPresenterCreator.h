#import <Foundation/Foundation.h>
#import "RNNBottomTabsPresenter.h"

@interface BottomTabsPresenterCreator : NSObject

+ (BottomTabsBasePresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
