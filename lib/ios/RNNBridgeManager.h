#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import "RNNBridgeManagerDelegate.h"

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface RNNBridgeManager : NSObject <RCTBridgeDelegate>

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions bridgeManagerDelegate:(id<RNNBridgeManagerDelegate>)delegate mainWindow:(UIWindow *)mainWindow;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

@property (readonly, nonatomic, strong) RCTBridge *bridge;

@end
