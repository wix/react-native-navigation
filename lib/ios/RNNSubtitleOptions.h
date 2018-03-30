#import "RNNOptions.h"

@interface RNNSubtitleOptions : RNNOptions

@property (nonatomic, strong) NSString* text;
@property (nonatomic, strong) NSNumber* fontSize;
@property (nonatomic, strong) NSNumber* color;
@property (nonatomic, strong) NSString* fontFamily;
@property (nonatomic, strong) NSString* alignment;

@property (nonatomic, strong) NSDictionary* fontAttributes;

@end
