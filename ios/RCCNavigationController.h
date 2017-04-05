#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

FOUNDATION_EXPORT NSString *const NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY;
FOUNDATION_EXPORT NSString *const NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY;

@interface RCCNavigationController : UINavigationController <UINavigationControllerDelegate>

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge;

@end
