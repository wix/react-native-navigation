#import "RNNOptions.h"

@class RNNDotIndicatorOptions;

@interface RNNBottomTabOptions : RNNOptions

@property(nonatomic) NSUInteger tag;
@property(nonatomic, strong) RNNText *text;
@property(nonatomic, strong) RNNText *badge;
@property(nonatomic, strong) RNNColor *badgeColor;
@property(nonatomic, strong) RNNDotIndicatorOptions *dotIndicator;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNText *fontWeight;
@property(nonatomic, strong) RNNText *testID;
@property(nonatomic, strong) RNNText *accessibilityLabel;
@property(nonatomic, strong) RNNImage *icon;
@property(nonatomic, strong) RNNImage *selectedIcon;
@property(nonatomic, strong) RNNColor *iconColor;
@property(nonatomic, strong) RNNColor *selectedIconColor;
@property(nonatomic, strong) RNNColor *selectedTextColor;
@property(nonatomic, strong) RNNDictionary *iconInsets;
@property(nonatomic, strong) RNNColor *textColor;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *selectTabOnPress;
@property(nonatomic, strong) RNNText *sfSymbol;
@property(nonatomic, strong) RNNText *sfSelectedSymbol;

- (BOOL)hasValue;

@end
