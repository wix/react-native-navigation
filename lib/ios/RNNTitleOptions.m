#import "RNNTitleOptions.h"
#import "RNNTitleViewHelper.h"


@implementation RNNTitleOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.text) {
		viewController.navigationItem.title = self.text;
	}
	
	NSDictionary* fontAttributes = [self fontAttributes];
	
	viewController.navigationController.navigationBar.titleTextAttributes = fontAttributes;
	if (@available(iOS 11.0, *)){
		viewController.navigationController.navigationBar.largeTitleTextAttributes = fontAttributes;
	}
	
	if (self.subtitle) {
		RNNTitleViewHelper* titleViewHelper = [[RNNTitleViewHelper alloc] init:viewController title:self.text subtitle:self.subtitle titleImageData:nil isSetSubtitle:NO];
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
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:20];
			}
		} else if (self.fontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.fontSize floatValue]];
		}
	}
	
	return navigationBarTitleTextAttributes;
}

- (NSDictionary *)subtitleFontAttributes {
	NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
	if (self.subtitleFontFamily || self.subtitleFontSize || self.subtitleColor) {
		if (self.subtitleColor) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.subtitleColor];
		}
		if (self.subtitleFontFamily){
			if (self.subtitleFontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.subtitleFontFamily size:[self.subtitleFontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.subtitleFontFamily size:14];
			}
		} else if (self.subtitleFontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.subtitleFontSize floatValue]];
		}
	}
	
	return navigationBarTitleTextAttributes;
}

@end
