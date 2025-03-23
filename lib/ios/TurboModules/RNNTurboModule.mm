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

- (void)setDefaultOptions:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
	  [[RNNTurboCommandsHandler sharedInstance] setDefaultOptions:options completion: nil];
	});
}

- (void)dismissAllModals:(NSString *)commandId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] dismissAllModals:options
														 commandId:commandId
									completion:^{
									}];
	});
}


- (void)dismissAllOverlays:(NSString *)commandId {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] dismissAllOverlays:commandId];
	});
}


- (void)dismissModal:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] dismissModal:componentId
													 commandId:commandId
												  mergeOptions:options
													completion:^(NSString *componentId) {
													}
													 rejection:nil];
	});
}


- (void)dismissOverlay:(NSString *)commandId componentId:(NSString *)componentId {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] dismissOverlay:componentId
													   commandId:commandId
													  completion:^{
													   }
													   rejection:nil];
	});
}


- (NSArray<NSString *> *)getLaunchArgs:(NSString *)commandId {
	return [[NSProcessInfo processInfo] arguments];
}


- (NSString *)mergeOptions:(NSString *)componentId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] mergeOptions:componentId
								   options:options
								completion:^{
								}];
	});

	return componentId;
}


- (NSString *)pop:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] pop:componentId
											commandId:commandId
										 mergeOptions:(NSDictionary *)options
										   completion:^{
											}
											rejection:nil];
	});

	return componentId;
}


- (NSString *)popTo:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] popTo:componentId
											  commandId:commandId
										   mergeOptions:options
											 completion:^{
											 }
											 rejection:nil];
	});

	return componentId;
}


- (NSString *)popToRoot:(NSString *)commandId componentId:(NSString *)componentId options:(NSDictionary *)options {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] popToRoot:componentId
												  commandId:commandId
											   mergeOptions:options
												 completion:^{
												  }
												  rejection:nil];
	});

	return componentId;
}


- (NSString *)push:(NSString *)commandId componentId:(NSString *)onComponentId layout:(NSDictionary *)layout {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] push:onComponentId
											 commandId:commandId
												layout:layout
											completion:^(NSString *pushedComponentId) {
						}
											 rejection:nil];
	});

	return layout[@"id"];
}


- (NSString *)setRoot:(NSString *)commandId layout:(NSDictionary *)layout {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] setRoot:layout
												commandId:commandId
											   completion:^(NSString *componentId) {
												}];
	});

	return layout[@"id"];
}


- (NSString *)setStackRoot:(NSString *)commandId componentId:(NSString *)componentId layout:(NSDictionary *)layout {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] setStackRoot:componentId
													 commandId:commandId
													  children:(NSArray *)layout
													completion:^{
								}
													 rejection:nil];
	});

	return componentId;
}


- (NSString *)showModal:(NSString *)commandId layout:(NSDictionary *)layout {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] showModal:layout
												  commandId:commandId
												  completion:^(NSString *componentId) {
												  }];
	});

	return layout[@"id"];
}


- (NSString *)showOverlay:(NSString *)commandId layout:(NSDictionary *)layout {
	RCTExecuteOnMainQueue(^{
		[[RNNTurboCommandsHandler sharedInstance] showOverlay:layout
													commandId:commandId
												   completion:^(NSString *_Nonnull componentId) {
												   }];
	});

	return layout[@"id"];
}

- (facebook::react::ModuleConstants<JS::NativeRNNTurboModule::Constants>)constantsToExport {
	return facebook::react::typedConstants<JS::NativeRNNTurboModule::Constants>([Constants getTurboConstants]);
}

- (facebook::react::ModuleConstants<JS::NativeRNNTurboModule::Constants::Builder>)getConstants {
	return [self constantsToExport];
}

@end
#endif
