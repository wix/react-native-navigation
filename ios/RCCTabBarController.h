#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

@class RCCNavigationController;

@interface RCCTabBarController : UIViewController

@property(nonatomic, assign) NSUInteger selectedIndex;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge completion:(void (^)(void))completion;

- (void)showScreen:(RCCNavigationController *)screen;

@end
