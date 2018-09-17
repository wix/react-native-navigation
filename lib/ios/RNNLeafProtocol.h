#import "RNNLayoutProtocol.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <RNNLayoutProtocol>

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
