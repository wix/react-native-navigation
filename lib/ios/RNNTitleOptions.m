#import "RNNTitleOptions.h"

@implementation RNNTitleOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.text) {
		viewController.navigationItem.title = self.text;
	}
	
	if (self.fontFamily || self.fontSize || self.color) {
		NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.color) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:[self valueForKey:@"color"]];
		}
		if (self.fontFamily){
			if(self.fontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:[self.fontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.fontFamily size:20];
			}
		} else if (self.fontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.fontSize floatValue]];
		}
		viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
		if (@available(iOS 11.0, *)){
			viewController.navigationController.navigationBar.largeTitleTextAttributes = navigationBarTitleTextAttributes;
		}
		
	}
}

@end
