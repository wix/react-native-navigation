#import "RNNNavigationOptions.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNBasePresenter : NSObject

@property(nonatomic, weak) id boundViewController;

@property(nonatomic, strong) NSString *boundComponentId;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)bindViewController:(UIViewController *)boundViewController;

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions;

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)applyDotIndicator:(UIViewController *)child;

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)viewDidLayoutSubviews;
@end
