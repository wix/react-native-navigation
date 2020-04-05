#import <Foundation/Foundation.h>
#import <ReactNativeNavigation/RNNCommandsHandler.h>

@interface CommandsHandlerCreator : NSObject

+ (RNNCommandsHandler *)createWithWindow:(UIWindow *)window;

@end
