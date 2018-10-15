#import "RNNOptions.h"
#import "RNNSubtitleOptions.h"
#import "RNNComponentOptions.h"

@interface RNNTitleOptions : RNNOptions

@property (nonatomic, strong) String* text;
@property (nonatomic, strong) Number* fontSize;
@property (nonatomic, strong) Color* color;
@property (nonatomic, strong) String* fontFamily;

@property (nonatomic, strong) RNNComponentOptions* component;
@property (nonatomic, strong) String* componentAlignment;

@property (nonatomic, strong) NSDictionary* fontAttributes;

@end
