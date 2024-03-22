#import "RNNBottomTabsPresenter.h"
#import <Foundation/Foundation.h>

@interface RNNBottomTabsPresenterCreator : NSObject

+ (RNNBottomTabsBasePresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
