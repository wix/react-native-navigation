#import "DeprecationOptions.h"
#import "RNNAnimationsOptions.h"
#import "RNNBottomTabOptions.h"
#import "RNNBottomTabsOptions.h"
#import "RNNLayoutOptions.h"
#import "RNNModalOptions.h"
#import "RNNOverlayOptions.h"
#import "RNNPreviewOptions.h"
#import "RNNSharedElementAnimationOptions.h"
#import "RNNSideMenuOptions.h"
#import "RNNSplitViewOptions.h"
#import "RNNStatusBarOptions.h"
#import "RNNTopBarOptions.h"
#import "RNNTopTabOptions.h"
#import "RNNTopTabsOptions.h"
#import "WindowOptions.h"

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNNavigationOptions : RNNOptions

@property(nonatomic, strong) RNNTopBarOptions *topBar;
@property(nonatomic, strong) RNNBottomTabsOptions *bottomTabs;
@property(nonatomic, strong) RNNBottomTabOptions *bottomTab;
@property(nonatomic, strong) RNNTopTabsOptions *topTabs;
@property(nonatomic, strong) RNNTopTabOptions *topTab;
@property(nonatomic, strong) RNNSideMenuOptions *sideMenu;
@property(nonatomic, strong) RNNOverlayOptions *overlay;
@property(nonatomic, strong) RNNAnimationsOptions *animations;
@property(nonatomic, strong) RNNStatusBarOptions *statusBar;
@property(nonatomic, strong) RNNPreviewOptions *preview;
@property(nonatomic, strong) RNNLayoutOptions *layout;
@property(nonatomic, strong) RNNSplitViewOptions *splitView;
@property(nonatomic, strong) RNNModalOptions *modal;
@property(nonatomic, strong) DeprecationOptions *deprecations;
@property(nonatomic, strong) WindowOptions *window;

@property(nonatomic, strong) Bool *popGesture;
@property(nonatomic, strong) Image *backgroundImage;
@property(nonatomic, strong) Image *rootBackgroundImage;
@property(nonatomic, strong) Text *modalPresentationStyle;
@property(nonatomic, strong) Text *modalTransitionStyle;

+ (instancetype)emptyOptions;

- (RNNNavigationOptions *)withDefault:(RNNNavigationOptions *)defaultOptions;

- (RNNNavigationOptions *)copy;

- (RNNNavigationOptions *)mergeOptions:(RNNNavigationOptions *)options;

@end
