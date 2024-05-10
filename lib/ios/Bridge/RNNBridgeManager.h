#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary *props, RCTBridge *bridge);

@interface RNNBridgeManager : NSObject

- (instancetype)initWithBridge:(RCTBridge *)bridge mainWindow:(UIWindow *)mainWindow;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

@property(readonly, nonatomic, strong) RCTBridge *bridge;

- (UIViewController *)findComponentForId:(NSString *)componentId;

@end
