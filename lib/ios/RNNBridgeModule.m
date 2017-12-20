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

RCT_EXPORT_METHOD(setRoot:(NSDictionary*)layout) {
	[_commandsHandler setRoot:layout];
}

RCT_EXPORT_METHOD(setOptions:(NSString*)containerId options:(NSDictionary*)options) {
	[_commandsHandler setOptions:containerId options:options];
}

RCT_EXPORT_METHOD(push:(NSString*)containerId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler push:containerId layout:layout completion:^(id result) {
		resolve(result);
	}];
}

RCT_EXPORT_METHOD(pop:(NSString*)containerId options:(NSDictionary*)options) {
	[_commandsHandler pop:containerId options:(NSDictionary*)options];
}

RCT_EXPORT_METHOD(popTo:(NSString*)containerId) {
	[_commandsHandler popTo:containerId];
}

RCT_EXPORT_METHOD(popToRoot:(NSString*)containerId) {
	[_commandsHandler popToRoot:containerId];
}

RCT_EXPORT_METHOD(showModal:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler showModal:layout completion:^(id containerID) {
		resolve(containerID);
	}];
}

RCT_EXPORT_METHOD(dismissModal:(NSString*)containerId) {
	[_commandsHandler dismissModal:containerId];
}

RCT_EXPORT_METHOD(dismissAllModals) {
	[_commandsHandler dismissAllModals];
}

@end

