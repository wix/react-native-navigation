#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import "RNNBridgeManagerDelegate.h"
#import "RNNStore.h"

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface RNNBridgeManager : NSObject <RCTBridgeDelegate>

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions bridgeManagerDelegate:(id<RNNBridgeManagerDelegate>)delegate;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

@property (readonly, nonatomic, strong) RCTBridge *bridge;
@property (readonly, nonatomic, strong) RNNStore *store;

@end
