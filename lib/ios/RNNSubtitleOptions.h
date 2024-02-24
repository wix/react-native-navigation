#import "RNNOptions.h"

@interface RNNSubtitleOptions : RNNOptions

@property(nonatomic, strong) Text *text;
@property(nonatomic, strong) RNNNumber *fontSize;
@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Text *fontFamily;
@property(nonatomic, strong) Text *fontWeight;
@property(nonatomic, strong) Text *alignment;

@end
