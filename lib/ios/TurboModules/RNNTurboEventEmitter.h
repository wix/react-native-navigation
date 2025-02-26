#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNEventEmitter.h"
#import <rnnavigation/rnnavigation.h>

@class RCTHost;

@interface RNNTurboEventEmitter : RNNEventEmitter <NativeRNNTurboEventEmitterSpec>

- (void)setHost:(RCTHost *)host;

@property(nonatomic, strong, readonly) RCTHost *host;

@end
#endif
