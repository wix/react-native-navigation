#import "RNNEventEmitter.h"

@implementation RNNEventEmitter

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.appLaunched";
static NSString* const containerDidAppear	= @"RNN.containerDidAppear";
static NSString* const containerDidDisappear	= @"RNN.containerDidDisappear";


-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, containerDidAppear, containerDidDisappear];
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

# pragma mark private

-(void)send:(NSString *)eventName body:(id)body {
	[self sendEventWithName:eventName body:body];
}

@end
