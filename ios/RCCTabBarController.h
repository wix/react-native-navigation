#import <UIKit/UIKit.h>
#import "RCTBridge.h"

@interface RCCTabBarController : UITabBarController

extern CGFloat const DEFAULT_ICON_WIDTH;
extern CGFloat const DEFAULT_ICON_HEIGHT;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge completion:(void (^)(void))completion;

@end
