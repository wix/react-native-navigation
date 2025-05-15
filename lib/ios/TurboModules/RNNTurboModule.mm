#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNTurboModule.h"
#import "Constants.h"
#import "RNNTurboCommandsHandler.h"
#import <ReactCommon/RCTTurboModule.h>

@implementation RNNTurboModule

RCT_EXPORT_MODULE()

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
	(const facebook::react::ObjCTurboModule::InitParams &)params
{
	return std::make_shared<facebook::react::NativeRNNTurboModuleSpecJSI>(params);
}

- (facebook::react::ModuleConstants<JS::NativeRNNTurboModule::Constants>)constantsToExport {
	return facebook::react::typedConstants<JS::NativeRNNTurboModule::Constants>([Constants getTurboConstants]);
}

- (facebook::react::ModuleConstants<JS::NativeRNNTurboModule::Constants::Builder>)getConstants {
	return [self constantsToExport];
}

- (void)dismissAllModals:(NSString *)commandId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] dismissAllModals:options
                                                         commandId:commandId
                                    completion:^{
            resolve(nil);
                                    }];
    });
}

- (void)dismissAllOverlays:(NSString *)commandId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] dismissAllOverlays:commandId];
        resolve(nil);
    });
}

- (void)dismissModal:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] dismissModal:componentId
                                                     commandId:commandId
                                                  mergeOptions:options
                                                    completion:^(NSString *componentId) {
            resolve(componentId);
                                                    }
                                                     rejection:reject];
    });
}

- (void)dismissOverlay:(NSString *)commandId componentId:(NSString *)componentId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] dismissOverlay:componentId
                                                       commandId:commandId
                                                      completion:^{
            resolve(@(1));
                                                       }
                                                       rejection:reject];
    });
}

- (void)getLaunchArgs:(NSString *)commandId resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    NSArray *args = [[NSProcessInfo processInfo] arguments];
    resolve(args);
}


- (void)pop:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] pop:componentId
                                            commandId:commandId
                                         mergeOptions:(NSDictionary *)options
                                           completion:^{
            resolve(componentId);
                                            }
                                            rejection:reject];
    });
}

- (void)popTo:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] popTo:componentId
                                              commandId:commandId
                                           mergeOptions:options
                                             completion:^{
            resolve(componentId);
                                             }
                                             rejection:reject];
    });
}

- (void)popToRoot:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] popToRoot:componentId
                                                  commandId:commandId
                                               mergeOptions:options
                                                 completion:^{
            resolve(componentId);
                                                  }
                                                  rejection:reject];
    });
}

- (void)push:(NSString *)commandId componentId:(NSString *)componentId layout:(NSDictionary *)layout resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] push:componentId
                                             commandId:commandId
                                                layout:layout
                                            completion:^(NSString *pushedComponentId) {
                                            resolve(pushedComponentId);
                        }
                                             rejection:reject];
    });
}

- (void)setRoot:(NSString *)commandId layout:(NSDictionary *)layout resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] setRoot:layout
                                                commandId:commandId
                                               completion:^(NSString *componentId) {
                                                resolve(componentId);
                                                }];
    });
}

- (void)setStackRoot:(NSString *)commandId componentId:(NSString *)componentId layout:(NSDictionary *)layout resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] setStackRoot:componentId
                                                     commandId:commandId
                                                      children:(NSArray *)layout
                                                    completion:^{
            resolve(componentId);
                                }
                                                     rejection:reject];
    });
}

- (void)showModal:(NSString *)commandId layout:(NSDictionary *)layout resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] showModal:layout
                                                  commandId:commandId
                                                  completion:^(NSString *componentId) {
            resolve(componentId);
                                                  }];
    });
}

- (void)showOverlay:(NSString *)commandId layout:(NSDictionary *)layout resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] showOverlay:layout
                                                    commandId:commandId
                                                   completion:^(NSString *_Nonnull componentId) {
            resolve(componentId);
                                                   }];
    });
}

- (void)mergeOptions:(NSString *)componentId options:(NSDictionary *)options { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] mergeOptions:componentId
                                   options:options
                                completion:^{}];
    });
}


- (void)setDefaultOptions:(NSDictionary *)options { 
    RCTExecuteOnMainQueue(^{
        [[RNNTurboCommandsHandler sharedInstance] setDefaultOptions:options completion: ^() {}];
    });
}

@end
#endif
