#import "RNNFontAttributesCreator.h"

@implementation RNNFontAttributesCreator

+ (NSDictionary *)createFontAttributesWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color {
	NSMutableDictionary* titleTextAttributes = [NSMutableDictionary new];
	if (fontFamily || fontSize || color) {
		if (color) {
			titleTextAttributes[NSForegroundColorAttributeName] = color;
		}
		if (fontFamily){
			if (fontSize) {
				titleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:fontFamily size:[fontSize floatValue]];
			} else {
				titleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:fontFamily size:17];
			}
		} else if (fontSize) {
			titleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[fontSize floatValue]];
		}
	}
	
	return titleTextAttributes;
}

@end
