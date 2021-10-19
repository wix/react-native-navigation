#import "RNNCommandsHandler.h"
#import <React/RCTBridgeModule.h>

@interface SimpleJsi : NSObject <RCTBridgeModule>

@property(nonatomic, assign) BOOL setBridgeOnMainQueue;
@property(nonatomic, strong) RNNCommandsHandler *commandsHandler;

- (instancetype)initWithCommandsHandler:(RNNCommandsHandler *)commandsHandler;

- (void)setRoot:(NSDictionary *)layout commandId:(NSString *)commandId;

@end
