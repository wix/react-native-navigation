
typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <NSObject>

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)bindViewController:(UIViewController *)viewController;

- (void)mergeAndPresentOptions:(RNNNavigationOptions *)newOptions;

@end
