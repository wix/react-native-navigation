#import <Foundation/Foundation.h>
#import "RNNTopBarOptions.h"
#import "RNNBottomTabsOptions.h"
#import "RNNBottomTabOptions.h"
#import "RNNSideMenuOptions.h"
#import "RNNTopTabOptions.h"
#import "RNNTopTabsOptions.h"
#import "RNNOverlayOptions.h"
#import "RNNAnimationOptions.h"
#import "RNNTransitionsOptions.h"
#import "RNNStatusBarOptions.h"
#import "RNNPreviewOptions.h"
#import "RNNLayoutOptions.h"

extern const NSInteger BLUR_TOPBAR_TAG;
extern const NSInteger TOP_BAR_TRANSPARENT_TAG;

@interface RNNNavigationOptions : RNNOptions

@property (nonatomic, strong) RNNTopBarOptions* topBar;
@property (nonatomic, strong) RNNBottomTabsOptions* bottomTabs;
@property (nonatomic, strong) RNNBottomTabOptions* bottomTab;
@property (nonatomic, strong) RNNTopTabsOptions* topTabs;
@property (nonatomic, strong) RNNTopTabOptions* topTab;
@property (nonatomic, strong) RNNSideMenuOptions* sideMenu;
@property (nonatomic, strong) RNNOverlayOptions* overlay;
@property (nonatomic, strong) RNNAnimationOptions* customTransition;
@property (nonatomic, strong) RNNTransitionsOptions* animations;
@property (nonatomic, strong) RNNStatusBarOptions* statusBar;
@property (nonatomic, strong) RNNPreviewOptions* preview;
@property (nonatomic, strong) RNNLayoutOptions* layout;

@property (nonatomic, strong) RNNOptions* defaultOptions;

@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;
@property (nonatomic, strong) NSNumber* popGesture;
@property (nonatomic, strong) NSDictionary* backgroundImage;
@property (nonatomic, strong) NSDictionary* rootBackgroundImage;
@property (nonatomic, strong) NSString* modalPresentationStyle;
@property (nonatomic, strong) NSString* modalTransitionStyle;

- (void)applyModalOptions:(UIViewController*)viewController;

@end
