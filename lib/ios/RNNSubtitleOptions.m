#import "RNNSubtitleOptions.h"

@implementation RNNSubtitleOptions

- (NSDictionary *)fontAttributes {
	NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
	if (self.fontFamily || self.fontSize || self.color) {
//		if (self.color) {
//			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.color];
//		}
		if (self.fontFamily){
			if (self.fontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:[self.fontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:14];
			}
		} else if (self.fontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.fontSize floatValue]];
		}
	}
	
	return navigationBarTitleTextAttributes;
}

- (NSNumber *)fontSize {
	return _fontSize ? _fontSize : @(14);
}


@end
