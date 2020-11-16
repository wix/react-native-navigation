#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <ReactNativeNavigation/ReactNativeNavigation.h>

@interface RNNComponentViewController (Utils)

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId;

+ (RNNComponentViewController *)createWithComponentId:(NSString *)componentId
                                       initialOptions:(RNNNavigationOptions *)initialOptions;

@end
