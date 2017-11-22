#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

#define GLOBAL_SCREEN_ACTION_COMMAND_TYPE       @"commandType"
#define GLOBAL_SCREEN_ACTION_TIMESTAMP          @"timestamp"
#define COMMAND_TYPE_PUSH                       @"Push"
#define COMMAND_TYPE_SHOW_MODAL                 @"ShowModal"
#define COMMAND_TYPE_BOTTOME_TAB_SELECTED       @"BottomTabSelected"
#define COMMAND_TYPE_INITIAL_SCREEN             @"InitialScreen"


extern NSString* const RCCViewControllerCancelReactTouchesNotification;

@interface RCCViewController : UIViewController

@property (nonatomic) NSMutableDictionary *navigatorStyle;
@property (nonatomic) BOOL navBarHidden;
@property (nonatomic, strong) NSString *controllerId;
@property (nonatomic, strong) NSString *commandType;
@property (nonatomic, strong) NSString *timestamp;
@property (nonatomic) RCCViewController *previewController;
@property (nonatomic) UIView *previewView;
@property (nonatomic) NSArray *previewActions;
@property (nonatomic) BOOL previewCommit;

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
