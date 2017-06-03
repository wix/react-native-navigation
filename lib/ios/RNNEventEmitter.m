#import "RNNEventEmitter.h"

@implementation RNNEventEmitter

RCT_EXPORT_MODULE();

static NSString* const onAppLaunched	= @"RNN.appLaunched";
static NSString* const containerStart	= @"RNN.containerStart";
static NSString* const containerStop	= @"RNN.containerStop";


-(NSArray<NSString *> *)supportedEvents {
	return @[onAppLaunched, containerStart, containerStop];
}

# pragma mark public

-(void)sendOnAppLaunched {
	[self send:onAppLaunched body:nil];
}

-(void)sendContainerStart:(NSString *)containerId {
	[self send:containerStart body:containerId];
}

-(void)sendContainerStop:(NSString *)containerId {
	[self send:containerStop body:containerId];
}

# pragma mark private

-(void)send:(NSString *)eventName body:(id)body {
	[self sendEventWithName:eventName body:body];
}

@end
