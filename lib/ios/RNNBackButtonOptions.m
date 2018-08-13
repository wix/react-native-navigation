#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.icon) {
		UIImage *image = self.tintedIcon;
		[viewController.navigationController.navigationBar setBackIndicatorImage:[UIImage new]];
		[viewController.navigationController.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
		
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithImage:image style:UIBarButtonItemStylePlain target:nil action:nil];
		viewController.navigationItem.backBarButtonItem = backItem;
	} else {
		NSString *title;

		if(self.title) {
			title = self.title;
		} else {
			title = viewController.navigationItem.title;
		}

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
			UIFont *font = nil;

			if (!fontSize) {
				fontSize = [[NSNumber alloc] initWithInt: 18];
			}

			if (fontFamily) {
				font = [UIFont fontWithName:fontFamily size:[fontSize floatValue]];
			} else {
				font = [UIFont systemFontOfSize:[fontSize floatValue]];
			}
			[textAttributes setObject:font forKey:NSFontAttributeName];

			[backItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
			[backItem setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
		}
		
		viewController.navigationItem.backBarButtonItem = backItem;
	}
	
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
	
	if (self.showTitle && ![self.showTitle boolValue]) {
		self.title = @"";
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
