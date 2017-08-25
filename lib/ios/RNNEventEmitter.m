#import "RNNEventEmitter.h"

@implementation RNNEventEmitter

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
	[self send:onAppLaunched body:nil];
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

# pragma mark private

-(void)send:(NSString *)eventName body:(id)body {
	[self sendEventWithName:eventName body:body];
}

@end
