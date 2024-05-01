#import "RNNTurboModule.h"
#import "RNNTurboCommandsHandler.h"
#import <React/RCTBridge+Private.h>
#import <ReactCommon/RCTTurboModule.h>

@implementation RNNTurboModule

RCT_EXPORT_MODULE()

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRNNTurboModuleSpecJSI>(params);
}

- (void)setDefaultOptions:(NSDictionary *)options {
    printf("Here");
    RCTExecuteOnMainQueue(^{
      [[RNNTurboCommandsHandler sharedInstance] setDefaultOptions:options completion: nil];
    });
}

@end
