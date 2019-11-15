#import "RNNFontAttributesCreator.h"
#import "RCTConvert+UIFontWeight.h"

@implementation RNNFontAttributesCreator

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize defaultFontSize:(NSNumber *)defaultFontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color defaultColor:(UIColor *)defaultColor {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary new];
	return [self createWithDictionary:titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:defaultFontSize fontWeight:fontWeight color:color defaultColor:defaultColor];
}

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary new];
	return [self createWithDictionary:titleTextAttributes fontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
}

+ (NSDictionary *)createWithDictionary:(NSDictionary *)attributesDictionary fontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize defaultFontSize:(NSNumber *)defaultFontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color defaultColor:(UIColor *)defaultColor {
	return [self createWithDictionary:attributesDictionary fontFamily:fontFamily fontSize:fontSize ?: defaultFontSize fontWeight:fontWeight color:color ?: defaultColor];
}

+ (NSDictionary *)createWithDictionary:(NSDictionary *)attributesDictionary fontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary dictionaryWithDictionary:attributesDictionary];
	if (fontFamily || fontSize || color || fontWeight) {
		CGFloat resolvedFontSize = fontSize ? fontSize.floatValue : 17;
		if (color) {
			titleTextAttributes[NSForegroundColorAttributeName] = color;
		}
		if (fontWeight) {
			titleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:resolvedFontSize weight:[RCTConvert UIFontWeight:fontWeight]];
		} else if (fontFamily){
			titleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:fontFamily size:resolvedFontSize];
		} else {
			titleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:resolvedFontSize];
		}
	}
	
	return titleTextAttributes;
}

@end
