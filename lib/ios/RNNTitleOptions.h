#import "RNNComponentOptions.h"
#import "RNNOptions.h"
#import "RNNSubtitleOptions.h"

@interface RNNTitleOptions : RNNOptions

@property(nonatomic, strong) RNNText *text;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNText *fontWeight;
@property(nonatomic, strong) RNNComponentOptions *component;
@property(nonatomic, strong) RNNText *componentAlignment;

- (BOOL)hasValue;

@end
