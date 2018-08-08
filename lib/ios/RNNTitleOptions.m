#import "RNNTitleOptions.h"
#import "RNNTitleViewHelper.h"


@implementation RNNTitleOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.text) {
		viewController.navigationItem.title = self.text;
	}
	
	NSDictionary* fontAttributes = [self fontAttributes];

	if (fontAttributes.allKeys.count > 0) {
		viewController.navigationController.navigationBar.titleTextAttributes = fontAttributes;
	}
	
	if (self.subtitle.text) {
		RNNTitleViewHelper* titleViewHelper = [[RNNTitleViewHelper alloc] init:viewController title:self.text subtitle:self.subtitle.text titleImageData:nil isSetSubtitle:NO];
		[titleViewHelper setup:self];
	}
}

- (NSDictionary *)fontAttributes {
	NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
	if (self.fontFamily || self.fontSize || self.color) {
		if (self.color) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.color];
		}
		if (self.fontFamily){
			if (self.fontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:[self.fontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:17];
			}
		} else if (self.fontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.fontSize floatValue]];
		}
	}
	
	return navigationBarTitleTextAttributes;
}

- (NSNumber *)fontSize {
	return _fontSize ? _fontSize : nil;
}

@end
