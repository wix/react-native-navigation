#import "RNNComponentOptions.h"
#import "RNNIconBackgroundOptions.h"
#import "RNNInsetsOptions.h"
#import "RNNOptions.h"

@interface RNNButtonOptions : RNNOptions

@property(nonatomic, strong) RNNText *identifier;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNText *text;
@property(nonatomic, strong) RNNText *systemItem;
@property(nonatomic, strong) RNNText *accessibilityLabel;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNText *fontWeight;
@property(nonatomic, strong) RNNText *testID;
@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNColor *disabledColor;
@property(nonatomic, strong) RNNImage *icon;
@property(nonatomic, strong) RNNText *sfSymbol;
@property(nonatomic, strong) RNNBool *enabled;
@property(nonatomic, strong) RNNInsetsOptions *iconInsets;
@property(nonatomic, strong) RNNBool *selectTabOnPress;
@property(nonatomic, strong) RNNComponentOptions *component;
@property(nonatomic, strong) RNNIconBackgroundOptions *iconBackground;
@property(nonatomic, strong) RNNBool *disableIconTint;

- (RNNButtonOptions *)withDefault:(RNNButtonOptions *)defaultOptions;

- (UIColor *)resolveColor;

- (RNNButtonOptions *)withDefaultColor:(RNNColor *)color disabledColor:(RNNColor *)disabledColor;

- (BOOL)shouldCreateCustomView;

- (BOOL)isEnabled;

- (UIControlState)state;

@end
