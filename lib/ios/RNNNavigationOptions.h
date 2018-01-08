#import <Foundation/Foundation.h>
#import "RNNTopBarOptions.h"
#import "RNNBottomTabsOptions.h"
#import "RNNBottomTabOptions.h"
#import "RNNSideMenuOptions.h"
#import "RNNTopTabOptions.h"
#import "RNNTopTabsOptions.h"
#import "RNNScreenOptions.h"

@interface RNNNavigationOptions : RNNOptions

@property (nonatomic, strong) RNNTopBarOptions* topBar;
@property (nonatomic, strong) RNNBottomTabsOptions* bottomTabs;
@property (nonatomic, strong) RNNBottomTabOptions* bottomTab;
@property (nonatomic, strong) RNNTopTabsOptions* topTabs;
@property (nonatomic, strong) RNNTopTabOptions* topTab;
@property (nonatomic, strong) RNNSideMenuOptions* sideMenu;
@property (nonatomic, strong) RNNScreenOptions* screen;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;


@end
