#import "RNNEventEmitter.h"

@implementation RNNEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.onAppLaunched";
static NSString* const componentDidAppear	= @"RNN.componentDidAppear";
static NSString* const componentDidDisappear	= @"RNN.componentDidDisappear";
static NSString* const onNavigationButtonPressed	= @"RNN.onNavigationButtonPressed";
static NSString* const onNavigationCommand	= @"RNN.onNavigationCommand";
static NSString* const onNavigationEvent	= @"RNN.onNavigationEvent";

-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, componentDidAppear, componentDidDisappear, onNavigationButtonPressed, onNavigationCommand, onNavigationEvent];
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
	[self send:onNavigationButtonPressed body:@{@"componentId":componentId , @"buttonId": buttonId}];
}

-(void)sendOnNavigationComment:(NSString *)commandName params:(NSDictionary*)params {
	[self send:onNavigationButtonPressed body:@{@"commandName":commandName , @"params": params}];
}

-(void)sendOnNavigationEvent:(NSString *)commandName params:(NSDictionary*)params {
	[self send:onNavigationEvent body:@{@"commandName":commandName , @"params": params}];
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
