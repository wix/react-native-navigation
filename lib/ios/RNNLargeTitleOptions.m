#import "RNNLargeTitleOptions.h"
#import "RNNTitleViewHelper.h"


@implementation RNNLargeTitleOptions

- (void)applyOn:(UIViewController *)viewController {

	if (@available(iOS 11.0, *)) {
		
		BOOL prefersLargeTitles = viewController.navigationController.navigationBar.prefersLargeTitles;
		BOOL previousValue = prefersLargeTitles;
		long displayMode = viewController.navigationItem.largeTitleDisplayMode;
		if ([self.visible boolValue]){
			prefersLargeTitles = YES;
			displayMode = UINavigationItemLargeTitleDisplayModeAlways;
		} else {
			prefersLargeTitles = NO;
			displayMode = UINavigationItemLargeTitleDisplayModeNever;
		}
                // only call setters for largeTitles if it's really changed
		if (prefersLargeTitles != previousValue) {
			viewController.navigationItem.largeTitleDisplayMode = displayMode;
			viewController.navigationController.navigationBar.prefersLargeTitles = prefersLargeTitles;
		}
		
		NSDictionary* fontAttributes = [self fontAttributes];
		viewController.navigationController.navigationBar.largeTitleTextAttributes = fontAttributes;
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

- (NSNumber *)fontSize {
	return _fontSize ? _fontSize : nil;
}

@end
