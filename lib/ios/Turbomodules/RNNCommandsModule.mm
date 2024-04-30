#import "RNNCommandsModule.h"
#import <React/RCTBridge+Private.h>
#import <ReactCommon/RCTTurboModule.h>

@implementation RNNCommandsModule {
    
}

RCT_EXPORT_MODULE()

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRNNCommandsModuleSpecJSI>(params);
}

- (void)setDefaultOptions:(NSDictionary *)options {
    printf("RNN TurboModule test");
}

@end
