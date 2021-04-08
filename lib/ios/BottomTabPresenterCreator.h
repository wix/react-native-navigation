#import "BottomTabPresenter.h"
#import <Foundation/Foundation.h>

@interface BottomTabPresenterCreator : NSObject

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
