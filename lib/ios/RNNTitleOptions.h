#import "RNNOptions.h"
#import "RNNSubtitleOptions.h"
#import "RNNComponentOptions.h"

@interface RNNTitleOptions : RNNOptions

@property (nonatomic, strong) Text* text;
@property (nonatomic, strong) Number* fontSize;
@property (nonatomic, strong) Color* color;
@property (nonatomic, strong) Text* fontFamily;
@property (nonatomic, strong) Text* fontWeight;
@property (nonatomic, strong) RNNComponentOptions* component;
@property (nonatomic, strong) Text* componentAlignment;

- (BOOL)hasValue;

@end
