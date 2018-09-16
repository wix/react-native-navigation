#import "RNNLayoutInfo.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@protocol RNNLeafProtocol <NSObject>

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
