#import "RNNFontAttributesCreator.h"
#import "RCTConvert+UIFontWeight.h"
#import <React/RCTFont.h>

#define DEFAULT_FONT_SIZE @(17.0f)

@implementation RNNFontAttributesCreator

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color
                              centered:(BOOL)centered {
    NSMutableDictionary *titleTextAttributes = [NSMutableDictionary new];
    return [self createFromDictionary:titleTextAttributes
                           fontFamily:fontFamily
                             fontSize:fontSize
                           fontWeight:fontWeight
                                color:color
                             centered:centered];
}

+ (NSDictionary *)createFromDictionary:(NSDictionary *)attributesDictionary
                            fontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color
                              centered:(BOOL)centered {
    NSMutableDictionary *titleTextAttributes =
        [NSMutableDictionary dictionaryWithDictionary:attributesDictionary];
    UIFont *currentFont = attributesDictionary[NSFontAttributeName];
    NSNumber *resolvedFontSize = [self resolveFontSize:currentFont fontSize:fontSize];

    titleTextAttributes[NSForegroundColorAttributeName] = color;
    titleTextAttributes[NSFontAttributeName] = [RCTFont updateFont:currentFont
                                                        withFamily:fontFamily
                                                              size:resolvedFontSize
                                                            weight:fontWeight
                                                             style:nil
                                                           variant:nil
                                                   scaleMultiplier:1.0];
    NSMutableParagraphStyle *paragraphStyle = [NSMutableParagraphStyle new];
    if (centered)
        paragraphStyle.alignment = NSTextAlignmentCenter;

    titleTextAttributes[NSParagraphStyleAttributeName] = paragraphStyle;

    return titleTextAttributes;
}

+ (NSNumber *)resolveFontSize:(UIFont *)currentFont fontSize:(NSNumber *)fontSize {
    if (fontSize) {
        return fontSize;
    } else if (currentFont) {
        return @(currentFont.fontDescriptor.pointSize);
    } else {
        return DEFAULT_FONT_SIZE;
    }
}

@end
