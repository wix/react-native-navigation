#import <Foundation/Foundation.h>
#import "BottomTabPresenter.h"

@interface BottomTabPresenterCreator : NSObject

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
