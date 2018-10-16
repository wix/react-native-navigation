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

@property (nonatomic, strong) Bool* popGesture;
@property (nonatomic, strong) Image* backgroundImage;
@property (nonatomic, strong) Image* rootBackgroundImage;
@property (nonatomic, strong) Text* modalPresentationStyle;
@property (nonatomic, strong) Text* modalTransitionStyle;

- (instancetype)initEmptyOptions;

- (RNNNavigationOptions *)withDefault:(RNNNavigationOptions *)defaultOptions;

- (RNNNavigationOptions *)copy;

@end
