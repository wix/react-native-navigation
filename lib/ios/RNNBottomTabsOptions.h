#import "BottomTabsAttachMode.h"
#import "RNNOptions.h"
#import "RNNShadowOptions.h"

@interface RNNBottomTabsOptions : RNNOptions

@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNIntNumber *currentTabIndex;
@property(nonatomic, strong) RNNBool *drawBehind;
@property(nonatomic, strong) RNNBool *animate;
@property(nonatomic, strong) RNNColor *tabColor;
@property(nonatomic, strong) RNNColor *selectedTabColor;
@property(nonatomic, strong) RNNBool *translucent;
@property(nonatomic, strong) RNNBool *hideShadow;
@property(nonatomic, strong) RNNColor *backgroundColor;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNText *testID;
@property(nonatomic, strong) RNNText *currentTabId;
@property(nonatomic, strong) RNNText *barStyle;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNText *titleDisplayMode;
@property(nonatomic, strong) RNNColor *borderColor;
@property(nonatomic, strong) RNNNumber *borderWidth;
@property(nonatomic, strong) RNNShadowOptions *shadow;
@property(nonatomic, strong) BottomTabsAttachMode *tabsAttachMode;

- (BOOL)shouldDrawBehind;

@end
