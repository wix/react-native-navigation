#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNTopBarOptions.h"
#import "RNNTabBarOptions.h"
#import "RNNSideMenuOptions.h"
#import "RNNTabItemOptions.h"

extern const NSInteger BLUR_STATUS_TAG;
extern const NSInteger BLUR_TOPBAR_TAG;
extern const NSInteger TOP_BAR_TRANSPARENT_TAG;

@interface RNNNavigationOptions : NSObject

@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSNumber* screenBackgroundColor;
@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;
@property (nonatomic, strong) NSString* backButtonTransition;
@property (nonatomic, strong) id orientation;
@property (nonatomic, strong) NSArray* leftButtons;
@property (nonatomic, strong) NSArray* rightButtons;
@property (nonatomic, strong) NSNumber* statusBarBlur;
@property (nonatomic, strong) NSNumber* statusBarHideWithTopBar;
@property (nonatomic, strong) NSNumber* popGesture;
@property (nonatomic, strong) RNNTopBarOptions* topBar;
@property (nonatomic, strong) RNNTabBarOptions* bottomTabs;
@property (nonatomic, strong) RNNSideMenuOptions* sideMenu;
@property (nonatomic, strong) UIImage* backgroundImage;
@property (nonatomic, strong) UIImage* rootBackgroundImage;
@property (nonatomic, strong) RNNTabItemOptions* tabItem;


- (UIInterfaceOrientationMask)supportedOrientations;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)navigationOptions;

-(void)applyOn:(UIViewController*)viewController;
-(void)mergeWith:(NSDictionary*)otherOptions;
-(void)storeOriginalTopBarImages:(UIViewController*)viewController;

- (void)applyTabBarItemOptions:(UIViewController*)viewController;

@end
