#import "RNNRootViewCreator.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <NSObject>

- (void)waitForReactViewReady:(BOOL)wait waitForUIEvent:(BOOL)waitForUIEvent perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)bindViewController:(UIViewController *)viewController;

- (BOOL)isCustomTransitioned;

- (id<RNNRootViewCreator>)creator;

@end
