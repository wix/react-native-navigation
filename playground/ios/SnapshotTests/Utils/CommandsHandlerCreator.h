#import <Foundation/Foundation.h>
#import "RNNCommandsHandler.h"

@interface CommandsHandlerCreator : NSObject

+ (RNNCommandsHandler *)createWithWindow:(UIWindow *)window;

@end
