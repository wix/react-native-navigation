#import "RNNComponentOptions.h"
#import "RNNIconBackgroundOptions.h"
#import "RNNInsetsOptions.h"
#import "RNNOptions.h"

@interface RNNButtonOptions : RNNOptions

@property(nonatomic, strong) Text *identifier;
@property(nonatomic, strong) Text *fontFamily;
@property(nonatomic, strong) Text *text;
@property(nonatomic, strong) Text *systemItem;
@property(nonatomic, strong) Text *accessibilityLabel;
@property(nonatomic, strong) Number *fontSize;
@property(nonatomic, strong) Text *fontWeight;
@property(nonatomic, strong) Text *testID;
@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Color *disabledColor;
@property(nonatomic, strong) Image *icon;
@property(nonatomic, strong) Text *sfSymbol;
@property(nonatomic, strong) Bool *enabled;
@property(nonatomic, strong) RNNInsetsOptions *iconInsets;
@property(nonatomic, strong) Bool *selectTabOnPress;
@property(nonatomic, strong) RNNComponentOptions *component;
@property(nonatomic, strong) RNNIconBackgroundOptions *iconBackground;

- (RNNButtonOptions *)withDefault:(RNNButtonOptions *)defaultOptions;

- (UIColor *)resolveColor;

- (RNNButtonOptions *)withDefaultColor:(Color *)color disabledColor:(Color *)disabledColor;

- (BOOL)shouldCreateCustomView;

- (BOOL)isEnabled;

@end
