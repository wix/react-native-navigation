#import "RNNOptions.h"

@interface RNNSubtitleOptions : RNNOptions

@property(nonatomic, strong) RNNText *text;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNText *fontFamily;
@property(nonatomic, strong) RNNText *fontWeight;
@property(nonatomic, strong) RNNText *alignment;

@end
