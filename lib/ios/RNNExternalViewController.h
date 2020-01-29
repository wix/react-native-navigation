#import "RNNComponentViewController.h"

@interface RNNExternalViewController : RNNComponentViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNComponentPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions viewController:(UIViewController *)viewController;

@end
