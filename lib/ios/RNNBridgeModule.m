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
	[_commandsHandler setRoot:layout completion:^{
		resolve(layout);
	}];
}

RCT_EXPORT_METHOD(mergeOptions:(NSString*)componentId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler mergeOptions:componentId options:options completion:^{
		resolve(componentId);
	}];
}

RCT_EXPORT_METHOD(setDefaultOptions:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler setDefaultOptions:options completion:^{
		resolve(nil);
	}];
}

RCT_EXPORT_METHOD(push:(NSString*)commandId componentId:(NSString*)componentId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler push:componentId layout:layout completion:^{
		resolve(componentId);
	} rejection:reject];
}

RCT_EXPORT_METHOD(pop:(NSString*)commandId componentId:(NSString*)componentId options:(NSDictionary*)options resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler pop:componentId options:(NSDictionary*)options completion:^{
		resolve(componentId);
	} rejection:reject];
}

RCT_EXPORT_METHOD(setStackRoot:(NSString*)commandId componentId:(NSString*)componentId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler setStackRoot:componentId layout:layout completion:^{
		resolve(componentId);
	} rejection:reject];
}

RCT_EXPORT_METHOD(popTo:(NSString*)commandId componentId:(NSString*)componentId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler popTo:componentId completion:^{
		resolve(componentId);
	} rejection:reject];
}

RCT_EXPORT_METHOD(popToRoot:(NSString*)commandId componentId:(NSString*)componentId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler popToRoot:componentId completion:^{
		resolve(componentId);
	} rejection:reject];
}

RCT_EXPORT_METHOD(showModal:(NSString*)commandId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler showModal:layout completion:^(NSString *componentId) {
		resolve(componentId);
	}];
}

RCT_EXPORT_METHOD(dismissModal:(NSString*)commandId componentId:(NSString*)componentId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler dismissModal:componentId completion:^{
		resolve(componentId);
	}];
}

RCT_EXPORT_METHOD(dismissAllModals:(NSString*)commandId resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler dismissAllModalsWithCompletion:^{
		resolve(nil);
	}];
}

RCT_EXPORT_METHOD(showOverlay:(NSString*)commandId layout:(NSDictionary*)layout resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler showOverlay:layout completion:^{
		resolve(layout[@"id"]);
	}];
}

RCT_EXPORT_METHOD(dismissOverlay:(NSString*)commandId componentId:(NSString*)componentId resolve:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	[_commandsHandler dismissOverlay:componentId completion:^{
		resolve(@(1));
	} rejection:reject];
}

RCT_EXPORT_METHOD(getLaunchArgs:(NSString*)commandId :(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	NSArray* args = [[NSProcessInfo processInfo] arguments];
	resolve(args);
}

RCT_EXPORT_METHOD(getConstants:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
	resolve([Constants getConstants]);
}

@end

