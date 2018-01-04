#import <Foundation/Foundation.h>
#import "RNNTopBarOptions.h"
#import "RNNTabBarOptions.h"
#import "RNNSideMenuOptions.h"
#import "RNNTabItemOptions.h"
#import "RNNTopTabOptions.h"

extern const NSInteger BLUR_STATUS_TAG;
extern const NSInteger BLUR_TOPBAR_TAG;
extern const NSInteger TOP_BAR_TRANSPARENT_TAG;

@interface RNNNavigationOptions : RNNOptions

@property (nonatomic, strong) RNNTopBarOptions* topBar;
@property (nonatomic, strong) RNNTabBarOptions* bottomTabs;
@property (nonatomic, strong) RNNTabItemOptions* bottomTab;
@property (nonatomic, strong) RNNTopTabOptions* topTab;
@property (nonatomic, strong) RNNSideMenuOptions* sideMenu;

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
