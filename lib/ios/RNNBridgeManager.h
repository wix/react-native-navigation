#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary *props, RCTBridge *bridge);

@interface RNNBridgeManager : NSObject

- (instancetype)initWithLaunchOptions:(NSDictionary *)launchOptions
                    andBridgeDelegate:(id<RCTBridgeDelegate>)delegate
                           mainWindow:(UIWindow *)mainWindow;

- (void)initializeBridge;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

@property(readonly, nonatomic, strong) RCTBridge *bridge;

- (UIViewController *)findComponentForId:(NSString *)componentId;

@end
