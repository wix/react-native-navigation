#import "RNNOptions.h"

@class DotIndicatorOptions;

@interface RNNBottomTabOptions : RNNOptions

@property(nonatomic) NSUInteger tag;
@property(nonatomic, strong) Text *text;
@property(nonatomic, strong) Text *badge;
@property(nonatomic, strong) Color *badgeColor;
@property(nonatomic, strong) DotIndicatorOptions *dotIndicator;
@property(nonatomic, strong) Text *fontFamily;
@property(nonatomic, strong) Text *fontWeight;
@property(nonatomic, strong) Text *testID;
@property(nonatomic, strong) Text *accessibilityLabel;
@property(nonatomic, strong) Image *icon;
@property(nonatomic, strong) Image *selectedIcon;
@property(nonatomic, strong) Color *iconColor;
@property(nonatomic, strong) Color *selectedIconColor;
@property(nonatomic, strong) Color *selectedTextColor;
@property(nonatomic, strong) Dictionary *iconInsets;
@property(nonatomic, strong) Color *textColor;
@property(nonatomic, strong) Number *fontSize;
@property(nonatomic, strong) Bool *visible;
@property(nonatomic, strong) Bool *selectTabOnPress;
@property(nonatomic, strong) Text *sfSymbol;
@property(nonatomic, strong) Text *sfSelectedSymbol;

- (BOOL)hasValue;

@end
