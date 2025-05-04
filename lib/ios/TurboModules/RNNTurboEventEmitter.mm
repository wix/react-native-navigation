#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNTurboEventEmitter.h"

@implementation RNNTurboEventEmitter {}

RCT_EXPORT_MODULE()

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
	(const facebook::react::ObjCTurboModule::InitParams &)params
{
	return std::make_shared<facebook::react::NativeRNNTurboEventEmitterSpecJSI>(params);
}

- (void)setHost:(RCTHost *)host {
	if (_host != nil) {
		return;
	}
	_host = host;
}

@end
#endif
