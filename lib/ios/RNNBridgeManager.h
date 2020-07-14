#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface RNNBridgeManager : NSObject

- (instancetype)initWithlaunchOptions:(NSDictionary *)launchOptions andBridgeDelegate:(id<RCTBridgeDelegate>)delegate mainWindow:(UIWindow *)mainWindow;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

@property(readonly, nonatomic, strong) RCTBridge *bridge;

@end
