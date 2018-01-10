#import "RNNBridgeModule.h"

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

RCT_EXPORT_METHOD(setRoot:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler setRoot:layout completion:^{
		resolve(layout);
	}];
}

RCT_EXPORT_METHOD(setOptions:(NSString*)containerId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler setOptions:containerId options:options completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(push:(NSString*)containerId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler push:containerId layout:layout completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(pop:(NSString*)containerId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler pop:containerId options:(NSDictionary*)options completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(popTo:(NSString*)containerId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler popTo:containerId completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(popToRoot:(NSString*)containerId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler popToRoot:containerId completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(showModal:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler showModal:layout completion:^{
		resolve(nil);
	}];
}

RCT_EXPORT_METHOD(dismissModal:(NSString*)containerId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler dismissModal:containerId completion:^{
		resolve(containerId);
	}];
}

RCT_EXPORT_METHOD(dismissAllModals:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler dismissAllModalsWithCompletion:^{
		resolve(nil);
	}];
}

@end

