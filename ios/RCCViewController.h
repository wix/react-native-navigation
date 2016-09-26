#import <UIKit/UIKit.h>
#import "RCTBridge.h"

extern NSString* const RCCViewControllerCancelReactTouchesNotification;

@interface RCCViewController : UIViewController

@property (nonatomic) NSMutableDictionary *navigatorStyle;
@property (nonatomic) BOOL navBarHidden;

+ (UIViewController*)controllerWithLayout:(NSDictionary *)layout globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge;
- (instancetype)initWithComponent:(NSString *)component passProps:(NSDictionary *)passProps navigatorStyle:(NSDictionary*)navigatorStyle globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge;
- (void)setStyleOnAppear;
- (void)setStyleOnInit;
- (void)updateStyle;
- (void)setNavBarVisibilityChange:(BOOL)animated;

@end

@protocol RCCViewControllerDelegate <NSObject>
-(void)setStyleOnAppearForViewController:(UIViewController*)viewController;
@end
