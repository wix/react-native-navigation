#import "RNNOptions.h"

@interface RNNTitleOptions : RNNOptions

@property (nonatomic, strong) NSString* text;
@property (nonatomic, strong) NSNumber* fontSize;
@property (nonatomic, strong) NSNumber* color;
@property (nonatomic, strong) NSString* fontFamily;

@property (nonatomic, strong) NSString* subtitle;
@property (nonatomic, strong) NSString* subtitleFontFamily;
@property (nonatomic, strong) NSString* subtitleFontSize;
@property (nonatomic, strong) NSString* subtitleColor;

@property (nonatomic, strong) NSString* component;
@property (nonatomic, strong) NSString* componentAlignment;

@property (nonatomic, strong) NSDictionary* fontAttributes;
@property (nonatomic, strong) NSDictionary* subtitleFontAttributes;

@end
