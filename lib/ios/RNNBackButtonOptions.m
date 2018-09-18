#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.showTitle && ![self.showTitle boolValue]) {
		self.title = @"";
	}
	
	if (self.icon) {
		UIImage *image = self.tintedIcon;
		[viewController.navigationController.navigationBar setBackIndicatorImage:[UIImage new]];
		[viewController.navigationController.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
		
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithImage:image style:UIBarButtonItemStylePlain target:nil action:nil];
		[self setBackItem:backItem onViewController:viewController];
	} else {
		NSString *title;
		
		title = self.title ? self.title : viewController.navigationItem.title;
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithTitle:title
																	 style:UIBarButtonItemStylePlain
																	target:nil
																	action:nil];

		if (self.color) {
			[backItem setTintColor:[RCTConvert UIColor:self.color]];
		}

		if(self.fontFamily || self.fontSize) {
			NSNumber* fontSize = self.fontSize;
			NSString* fontFamily = self.fontFamily;
			NSMutableDictionary* textAttributes = [[NSMutableDictionary alloc] init];
			UIFont *font;

			if (!fontSize) {
				fontSize = [[NSNumber alloc] initWithInt: 18];
			}
			font = fontFamily ? 
				[UIFont fontWithName:fontFamily size:[fontSize floatValue]] : 
				[UIFont systemFontOfSize:[fontSize floatValue]];
			[textAttributes setObject:font forKey:NSFontAttributeName];

			[backItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
			[backItem setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
		}
		
		[self setBackItem:backItem onViewController:viewController];
	}
	
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
}

- (void)setBackItem:(UIBarButtonItem *)backItem onViewController:(UIViewController *)viewController {
	UINavigationController* nvc = viewController.navigationController;
	if (nvc.viewControllers.count >= 2) {
		UIViewController* lastViewControllerInStack = nvc.viewControllers[nvc.viewControllers.count - 2];
		lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
	}
}

- (UIImage *)tintedIcon {
	UIImage *image = self.icon ? [RCTConvert UIImage:self.icon] : nil;
	if (self.color) {
		return [[image withTintColor:[RCTConvert UIColor:self.color]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	}
	
	return image;
}

@end
