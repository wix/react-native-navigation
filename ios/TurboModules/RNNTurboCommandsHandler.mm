#import <Foundation/Foundation.h>
#import "RNNTurboCommandsHandler.h"

static RNNCommandsHandler *_sharedCommandsHandler = nil;

@implementation RNNTurboCommandsHandler

+ (void)setSharedInstance:(RNNCommandsHandler *)shared {
	_sharedCommandsHandler = shared;
}

+ (RNNCommandsHandler *)sharedInstance {
	return _sharedCommandsHandler;
}

@end
