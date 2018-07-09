#import "RNNEventEmitter.h"
#import "RNNUtils.h"

@implementation RNNEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static const NSString* AppLaunched		= @"RNN.AppLaunched";
static const NSString* CommandCompleted	= @"RNN.CommandCompleted";
static const NSString* NativeEvent		= @"RNN.NativeEvent";

static const NSString* ComponentDidAppear = @"RNN.ComponentDidAppear";
static const NSString* ComponentDidDisappear = @"RNN.ComponentDidDisappear";
static const NSString* NavigationButtonPressed = @"RNN.NavigationButtonPressed";
static const NSString* SearchBarUpdated = @"RNN.SearchBarUpdated";
static const NSString* SearchBarCancelPressed = @"RNN.SearchBarCancelPressed";

-(NSArray<NSString *> *)supportedEvents {
	return @[AppLaunched, CommandCompleted, NativeEvent, ];
}

# pragma mark public

-(void)sendOnAppLaunched {
	if (_appLaunchedListenerCount > 0) {
		[self send:onAppLaunched body:nil];
	} else {
		_appLaunchedEventDeferred = TRUE;
	}
}

-(void)sendComponentDidAppear:(NSString *)componentId componentName:(NSString *)componentName {
	[self send:componentLifecycle body:@{
										 @"type": componentLifecycleDidAppear,
										 @"componentId":componentId,
										 @"componentName": componentName
										 }];
}

-(void)sendComponentDidDisappear:(NSString *)componentId componentName:(NSString *)componentName{
	[self send:componentLifecycle body:@{
										 @"type": componentLifecycleDidDisappear,
										 @"componentId":componentId,
										 @"componentName": componentName
										 }];
}

-(void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString*)buttonId {
	[self send:navigationEvent body:@{
									  @"name": @"buttonPressed",
									  @"params": @{
											  @"componentId": componentId,
											  @"buttonId": buttonId
											  }
									  }];
}

-(void)sendOnNavigationCommand:(NSString *)commandName params:(NSDictionary*)params {
	[self send:navigationEvent body:@{
									  @"name":commandName ,
									  @"params": params
									  }];
}

-(void)sendOnNavigationCommandCompletion:(NSString *)commandName params:(NSDictionary*)params {
	[self send:commandCompleted body:@{
									   @"commandId":commandName,
									   @"params": params,
									   @"completionTime": [RNNUtils getCurrentTimestamp]
									   }];
}

-(void)sendOnNavigationEvent:(NSString *)commandName params:(NSDictionary*)params {
	[self send:navigationEvent body:@{@"name":commandName , @"params": params}];
}

-(void)sendOnSearchBarUpdated:(NSString *)componentId
						 text:(NSString*)text
					isFocused:(BOOL)isFocused {
	[self send:navigationEvent body:@{@"name": @"searchBarUpdated",
									  @"params": @{
												  @"componentId": componentId,
												  @"text": text,
												  @"isFocused": @(isFocused)}}];
}

- (void)sendOnSearchBarCancelPressed:(NSString *)componentId {
	[self send:navigationEvent body:@{@"name": @"searchBarCancelPressed",
									  @"params": @{
											  @"componentId": componentId}}];
}

- (void)addListener:(NSString *)eventName {
	[super addListener:eventName];
	if ([eventName isEqualToString:onAppLaunched]) {
		_appLaunchedListenerCount++;
		if (_appLaunchedEventDeferred) {
			_appLaunchedEventDeferred = FALSE;
			[self sendOnAppLaunched];
		}
	}
}

# pragma mark private

-(void)send:(NSString *)eventName body:(id)body {
    if (self.bridge == nil) {
        return;
    }
	[self sendEventWithName:eventName body:body];
}

@end
