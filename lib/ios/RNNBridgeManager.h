#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import "ReactNativeNavigation.h"
#import "RNNStore.h"

@interface RNNBridgeManager : NSObject <RCTBridgeDelegate>

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

@property (readonly, nonatomic, strong) RCTBridge *bridge;
@property (readonly, nonatomic, strong) RNNStore *store;

@end
