#import <UIKit/UIKit.h>

#if __has_include("RCTBridge.h")
#import "RCTBridge.h"
#elif __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#elif __has_include("React/RCTBridge.h")
#import "React/RCTBridge.h"   // Required when used as a Pod in a Swift project
#endif

@interface RCCNavigationController : UINavigationController <UINavigationControllerDelegate>

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge;

@end
