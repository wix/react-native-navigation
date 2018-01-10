#import "RNNEventEmitter.h"

@implementation RNNEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.appLaunched";
static NSString* const containerDidAppear	= @"RNN.containerDidAppear";
static NSString* const containerDidDisappear	= @"RNN.containerDidDisappear";
static NSString* const onNavigationButtonPressed	= @"RNN.navigationButtonPressed";

-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, containerDidAppear, containerDidDisappear, onNavigationButtonPressed];
}

# pragma mark public

-(void)sendOnAppLaunched {
	if (_appLaunchedListenerCount > 0) {
		[self send:onAppLaunched body:nil];
	} else {
		_appLaunchedEventDeferred = TRUE;
	}
}

-(void)sendContainerDidAppear:(NSString *)containerId {
	[self send:containerDidAppear body:containerId];
}

-(void)sendContainerDidDisappear:(NSString *)containerId {
	[self send:containerDidDisappear body:containerId];
}

-(void)sendOnNavigationButtonPressed:(NSString *)containerId buttonId:(NSString*)buttonId {
	[self send:onNavigationButtonPressed body:@{@"containerId":containerId , @"buttonId": buttonId }];
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
