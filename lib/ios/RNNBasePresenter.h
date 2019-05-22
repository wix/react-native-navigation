#import "RNNNavigationOptions.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

@property (nonatomic, strong) NSString* bindedComponentId;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
