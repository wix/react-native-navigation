#import "RNNLayoutProtocol.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <RNNLayoutProtocol>

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;

- (void)bindViewController:(UIViewController *)viewController;

@end
