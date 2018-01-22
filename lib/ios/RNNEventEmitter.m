#import "RNNEventEmitter.h"

@implementation RNNEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.appLaunched";
static NSString* const componentDidAppear	= @"RNN.componentDidAppear";
static NSString* const componentDidDisappear	= @"RNN.componentDidDisappear";
static NSString* const onNavigationButtonPressed	= @"RNN.navigationButtonPressed";
static NSString* const navigationCommands	= @"RNN.navigationCommands";

-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, componentDidAppear, componentDidDisappear, onNavigationButtonPressed, navigationCommands];
}

# pragma mark public

-(void)sendOnAppLaunched {
	if (_appLaunchedListenerCount > 0) {
		[self send:onAppLaunched body:nil];
	} else {
		_appLaunchedEventDeferred = TRUE;
	}
}

-(void)sendNavigationEvent:(RNNNavigationEvent *)navigationEvent {
	[self send:navigationCommands body:navigationEvent.body];
}

-(void)sendComponentDidAppear:(NSString *)componentId {
	[self send:componentDidAppear body:componentId];
}

-(void)sendComponentDidDisappear:(NSString *)componentId {
	[self send:componentDidDisappear body:componentId];
}

-(void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString*)buttonId {
	[self send:onNavigationButtonPressed body:@{@"componentId":componentId , @"buttonId": buttonId }];
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
	[self sendEventWithName:eventName body:body];
}

@end
