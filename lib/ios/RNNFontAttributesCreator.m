#import "RNNFontAttributesCreator.h"
#import "RCTConvert+UIFontWeight.h"

@implementation RNNFontAttributesCreator

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize defaultFontSize:(NSNumber *)defaultFontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color defaultColor:(UIColor *)defaultColor {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary new];
	return [self createFromDictionary:titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:defaultFontSize fontWeight:fontWeight color:color defaultColor:defaultColor];
}

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary new];
	return [self createFromDictionary:titleTextAttributes fontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
}

+ (NSDictionary *)createFromDictionary:(NSDictionary *)attributesDictionary fontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize defaultFontSize:(NSNumber *)defaultFontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color defaultColor:(UIColor *)defaultColor {
	return [self createFromDictionary:attributesDictionary fontFamily:fontFamily fontSize:fontSize ?: defaultFontSize fontWeight:fontWeight color:color ?: defaultColor];
}

+ (NSDictionary *)createFromDictionary:(NSDictionary *)attributesDictionary fontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary dictionaryWithDictionary:attributesDictionary];\
    UIFont* currentFont = attributesDictionary[NSFontAttributeName];
    
	CGFloat resolvedFontSize = fontSize ? fontSize.floatValue : currentFont.fontDescriptor.pointSize;
    if (color) {
        titleTextAttributes[NSForegroundColorAttributeName] = color;
    }
    if (fontWeight) {
        titleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:resolvedFontSize weight:[RCTConvert UIFontWeight:fontWeight]];
    } else if (fontFamily){
        titleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:fontFamily size:resolvedFontSize];
    } else if (fontSize) {
        titleTextAttributes[NSFontAttributeName] = [UIFont fontWithDescriptor:currentFont.fontDescriptor size:resolvedFontSize];
    }
	
	return titleTextAttributes;
}

@end
