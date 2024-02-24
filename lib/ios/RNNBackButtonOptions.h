#import "RNNOptions.h"

@interface RNNBackButtonOptions : RNNOptions

@property(nonatomic, strong) RNNImage *icon;
@property(nonatomic, strong) RNNText *sfSymbol;
@property(nonatomic, strong) RNNText *title;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNText *transition;
@property(nonatomic, strong) RNNText *testID;
@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNBool *showTitle;
@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *enableMenu;
@property(nonatomic, strong) RNNText *displayMode;
@property(nonatomic, strong) RNNText *identifier;
@property(nonatomic, strong) RNNBool *popStackOnPress;

- (BOOL)hasValue;

@end
