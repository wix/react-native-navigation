
typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <NSObject>

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)bindViewController:(UIViewController *)viewController;

@end
