#import "RNNNavigationOptions.h"

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
