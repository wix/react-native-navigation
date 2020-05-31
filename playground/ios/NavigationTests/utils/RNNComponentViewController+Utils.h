#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>

@interface RNNComponentViewController (Utils)

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId;

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId initialOptions:(RNNNavigationOptions *)initialOptions;

@end
