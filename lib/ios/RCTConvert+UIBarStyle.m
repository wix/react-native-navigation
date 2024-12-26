#import "RCTConvert+UIBarStyle.h"

@implementation RCTConvert (UIBarStyle)

RCT_ENUM_CONVERTER(
    UIBarStyle,
    (@{
      @"default" : @(UIBarStyleDefault),
      @"black" : @(UIBarStyleBlack),
    }),
    UIBarStyleDefault,
    integerValue)

@end
