#import "RNNBridgeModule.h"

@implementation RNNBridgeModule {
	RNNCommandsHandler* _commandsHandler;
}

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

RCT_EXPORT_METHOD(push:(NSString*)containerId layout:(NSDictionary*)layout) {
	[_commandsHandler push:containerId layout:layout];
}

RCT_EXPORT_METHOD(pop:(NSString*)containerId) {
	[_commandsHandler pop:containerId];
}

RCT_EXPORT_METHOD(popTo:(NSString*)containerId) {
	[_commandsHandler popTo:containerId];
}

RCT_EXPORT_METHOD(popToRoot:(NSString*)containerId) {
	[_commandsHandler popToRoot:containerId];
}

RCT_EXPORT_METHOD(showModal:(NSDictionary*)layout) {
	[_commandsHandler showModal:layout];
}

RCT_EXPORT_METHOD(dismissModal:(NSString*)containerId) {
	[_commandsHandler dismissModal:containerId];
}

RCT_EXPORT_METHOD(dismissAllModals) {
	[_commandsHandler dismissAllModals];
}

@end

