#import "RCTConvert+UIFontWeight.h"

@implementation RCTConvert (UIFontWeight)

RCT_ENUM_CONVERTER(UIFontWeight, (@{
                       @"ultraLight" : @(UIFontWeightUltraLight),
                       @"thin" : @(UIFontWeightThin),
                       @"light" : @(UIFontWeightLight),
                       @"regular" : @(UIFontWeightRegular),
                       @"medium" : @(UIFontWeightMedium),
                       @"semibold" : @(UIFontWeightSemibold),
                       @"bold" : @(UIFontWeightBold),
                       @"heavy" : @(UIFontWeightHeavy),
                       @"black" : @(UIFontWeightBlack)
                   }),
                   UIFontWeightRegular, floatValue)

@end
