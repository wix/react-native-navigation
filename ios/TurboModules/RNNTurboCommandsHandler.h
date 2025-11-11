#ifndef RNNTurboCommandsHandler_h
#define RNNTurboCommandsHandler_h

@class RNNCommandsHandler;

@interface RNNTurboCommandsHandler : NSObject

+(RNNCommandsHandler *) sharedInstance;
+(void)setSharedInstance:(RNNCommandsHandler *) shared;

@end

#endif
