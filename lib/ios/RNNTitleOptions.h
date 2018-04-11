#import "RNNOptions.h"
#import "RNNSubtitleOptions.h"
#import "RNNComponentOptions.h"

@interface RNNTitleOptions : RNNOptions

@property (nonatomic, strong) NSString* text;
@property (nonatomic, strong) NSNumber* fontSize;
@property (nonatomic, strong) NSNumber* color;
@property (nonatomic, strong) NSString* fontFamily;

@property (nonatomic, strong) RNNComponentOptions* component;
@property (nonatomic, strong) NSString* componentAlignment;

@property (nonatomic, strong) RNNSubtitleOptions* subtitle;

@property (nonatomic, strong) NSDictionary* fontAttributes;

@end
