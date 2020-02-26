#import <Foundation/Foundation.h>
#import "RNNBottomTabsPresenter.h"

@interface BottomTabsPresenterCreator : NSObject

+ (RNNBottomTabsPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
