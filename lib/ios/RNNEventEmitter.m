#import "RNNEventEmitter.h"
#import "RNNUtils.h"

@implementation RNNEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.appLaunched";
static NSString* const componentDidAppear	= @"RNN.componentDidAppear";
static NSString* const componentDidDisappear	= @"RNN.componentDidDisappear";
static NSString* const commandComplete	= @"RNN.commandCompleted";
static NSString* const navigationEvent	= @"RNN.nativeEvent";

-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, componentDidAppear, componentDidDisappear, commandComplete, navigationEvent];
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
	[self send:componentDidAppear body:@{@"componentId":componentId, @"componentName": componentName}];
}

-(void)sendComponentDidDisappear:(NSString *)componentId componentName:(NSString *)componentName{
	[self send:componentDidDisappear body:@{@"componentId":componentId, @"componentName": componentName}];
}

-(void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString*)buttonId {
	[self send:navigationEvent body:@{@"name": @"buttonPressed", @"params": @{@"componentId": componentId , @"buttonId": buttonId}}];
}

-(void)sendOnNavigationCommand:(NSString *)commandName params:(NSDictionary*)params {
	[self send:navigationEvent body:@{@"name":commandName , @"params": params}];
}

-(void)sendOnNavigationCommandCompletion:(NSString *)commandName params:(NSDictionary*)params {
	[self send:commandComplete body:@{@"commandId":commandName , @"params": params, @"completionTime": [RNNUtils getCurrentTimestamp] }];
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
    if ([eventName isEqualToString:componentDidDisappear] && self.bridge == nil) {
        return;
    }
	[self sendEventWithName:eventName body:body];
}

@end
