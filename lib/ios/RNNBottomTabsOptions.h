#import "RNNOptions.h"

@interface RNNBottomTabsOptions : RNNOptions

@property (nonatomic, strong) Bool* visible;
@property (nonatomic, strong) IntNumber* currentTabIndex;
@property (nonatomic, strong) Bool* drawBehind;
@property (nonatomic, strong) Color* tabColor;
@property (nonatomic, strong) Color* selectedTabColor;
@property (nonatomic, strong) Bool* translucent;
@property (nonatomic, strong) Bool* hideShadow;
@property (nonatomic, strong) Color* backgroundColor;
@property (nonatomic, strong) Number* fontSize;

@property (nonatomic, strong) String* testID;
@property (nonatomic, strong) String* currentTabId;
@property (nonatomic, strong) String* barStyle;
@property (nonatomic, strong) String* fontFamily;

@end
