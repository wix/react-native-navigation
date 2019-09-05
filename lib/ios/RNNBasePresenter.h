#import "RNNNavigationOptions.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNBasePresenter : NSObject

@property(nonatomic, weak) id boundViewController;

@property(nonatomic, strong) NSString *boundComponentId;

@property(nonatomic, strong) RNNNavigationOptions * defaultOptions;

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)bindViewController:(UIViewController *)boundViewController;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions;

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)applyDotIndicator:(UIViewController *)child;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)viewDidLayoutSubviews;

- (UIStatusBarStyle)getStatusBarStyle:(RNNNavigationOptions *)resolvedOptions;

- (UIInterfaceOrientationMask)getOrientation:(RNNNavigationOptions *)options;

- (BOOL)isStatusBarVisibility:(UINavigationController *)stack resolvedOptions:(RNNNavigationOptions *)resolvedOptions;
@end
