#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>

@interface RNNBridgeManager : NSObject <RCTBridgeDelegate>

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions;

@property (readonly, nonatomic, strong) RCTBridge *bridge;

@end
