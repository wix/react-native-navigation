#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTEventEmitter.h>
#import <rnnavigation/rnnavigation.h>

@interface RNNTurboEventEmitter : RCTEventEmitter <NativeRNNTurboEventEmitterSpec>
- (void)send:(NSString *)eventName body:(id)body;
@end
#endif
