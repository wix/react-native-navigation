#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

@interface RCCNavigationController : UINavigationController <UINavigationControllerDelegate>

@property (nonatomic, strong) RCTRootView *reactView;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge;

@end
