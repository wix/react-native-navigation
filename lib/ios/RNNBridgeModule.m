#import "RNNBridgeModule.h"
#import "Constants.h"

@implementation RNNBridgeModule {
    RNNCommandsHandler* _commandsHandler;
}
@synthesize bridge = _bridge;
RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

-(instancetype)initWithCommandsHandler:(RNNCommandsHandler *)commandsHandler {
    self = [super init];
    _commandsHandler = commandsHandler;
    return self;
}

#pragma mark - JS interface

RCT_EXPORT_METHOD(setRoot:(NSString*)commandId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler setRoot:layout commandId:commandId completion:^{
            resolve(layout);
        }];
    });
}

RCT_EXPORT_METHOD(mergeOptions:(NSString*)componentId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler mergeOptions:componentId options:options completion:^{
            resolve(componentId);
        }];
    });
}

RCT_EXPORT_METHOD(setDefaultOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler setDefaultOptions:options completion:^{
            resolve(nil);
        }];
    });
}

RCT_EXPORT_METHOD(push:(NSString*)commandId componentId:(NSString*)componentId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler push:componentId commandId:commandId layout:layout completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(pop:(NSString*)commandId componentId:(NSString*)componentId mergeOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler pop:componentId commandId:commandId mergeOptions:(NSDictionary*)options completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(setStackRoot:(NSString*)commandId componentId:(NSString*)componentId children:(NSArray*)children resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler setStackRoot:componentId commandId:commandId children:children completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(popTo:(NSString*)commandId componentId:(NSString*)componentId mergeOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler popTo:componentId commandId:commandId mergeOptions:options completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(popToRoot:(NSString*)commandId componentId:(NSString*)componentId mergeOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler popToRoot:componentId commandId:commandId mergeOptions:options completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(showModal:(NSString*)commandId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler showModal:layout commandId:commandId completion:^(NSString *componentId) {
            resolve(componentId);
        }];
    });
}

RCT_EXPORT_METHOD(dismissModal:(NSString*)commandId componentId:(NSString*)componentId mergeOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler dismissModal:componentId commandId:commandId mergeOptions:options completion:^{
            resolve(componentId);
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(dismissAllModals:(NSString*)commandId mergeOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler dismissAllModals:options commandId:commandId completion:^{
            resolve(nil);
        }];
    });
}

RCT_EXPORT_METHOD(showOverlay:(NSString*)commandId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler showOverlay:layout commandId:commandId completion:^{
            resolve(layout[@"id"]);
        }];
    });
}

RCT_EXPORT_METHOD(dismissOverlay:(NSString*)commandId componentId:(NSString*)componentId resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        [self->_commandsHandler dismissOverlay:componentId commandId:commandId completion:^{
            resolve(@(1));
        } rejection:reject];
    });
}

RCT_EXPORT_METHOD(getLaunchArgs:(NSString*)commandId :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSArray* args = [[NSProcessInfo processInfo] arguments];
    resolve(args);
}

RCT_EXPORT_METHOD(getNavigationConstants:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    RCTExecuteOnMainQueue(^{
        resolve([Constants getConstants]);
    });
}

@end

