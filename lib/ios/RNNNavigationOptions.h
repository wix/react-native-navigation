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

extern const NSInteger BLUR_STATUS_TAG;
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


@property (nonatomic, strong) NSNumber* animated;
@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSNumber* screenBackgroundColor;
@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;
@property (nonatomic, strong) NSString* backButtonTransition;
@property (nonatomic, strong) id orientation;
@property (nonatomic, strong) NSNumber* statusBarBlur;
@property (nonatomic, strong) NSNumber* statusBarHideWithTopBar;
@property (nonatomic, strong) NSNumber* popGesture;
@property (nonatomic, strong) UIImage* backgroundImage;
@property (nonatomic, strong) UIImage* rootBackgroundImage;

- (UIInterfaceOrientationMask)supportedOrientations;


@end
