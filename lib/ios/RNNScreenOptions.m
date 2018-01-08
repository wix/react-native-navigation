#import "RNNScreenOptions.h"

const NSInteger BLUR_STATUS_TAG = 78264801;

@implementation RNNScreenOptions

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([key isEqualToString:@"backgroundImage"]) {
			self.backgroundImage = [RCTConvert UIImage:otherOptions[key]];
		} else if ([key isEqualToString:@"rootBackgroundImage"]) {
			self.rootBackgroundImage = [RCTConvert UIImage:otherOptions[key]];
		} else {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

- (void)applyOn:(UIViewController *)viewController {
	if (self.popGesture) {
		viewController.navigationController.interactivePopGestureRecognizer.enabled = [self.popGesture boolValue];
	}
	
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
	}
	
	if (self.statusBarBlur) {
		UIView* curBlurView = [viewController.view viewWithTag:BLUR_STATUS_TAG];
		if ([self.statusBarBlur boolValue]) {
			if (!curBlurView) {
				UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
				blur.frame = [[UIApplication sharedApplication] statusBarFrame];
				blur.tag = BLUR_STATUS_TAG;
				[viewController.view insertSubview:blur atIndex:0];
			}
		} else {
			if (curBlurView) {
				[curBlurView removeFromSuperview];
			}
		}
	}
	
	if (self.backgroundImage) {
		UIImageView* backgroundImageView = (viewController.view.subviews.count > 0) ? viewController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = self.backgroundImage;
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
	
	if (self.rootBackgroundImage) {
		UIImageView* backgroundImageView = (viewController.navigationController.view.subviews.count > 0) ? viewController.navigationController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.navigationController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = self.rootBackgroundImage;
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
}

- (UIInterfaceOrientationMask)supportedOrientations {
	NSArray* orientationsArray = [self.orientation isKindOfClass:[NSString class]] ? @[self.orientation] : self.orientation;
	NSUInteger supportedOrientationsMask = 0;
	if (!orientationsArray || [self.orientation isEqual:@"default"]) {
		return [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	} else {
		for (NSString* orientation in orientationsArray) {
			if ([orientation isEqualToString:@"all"]) {
				supportedOrientationsMask = UIInterfaceOrientationMaskAll;
				break;
			}
			if ([orientation isEqualToString:@"landscape"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskLandscape);
			}
			if ([orientation isEqualToString:@"portrait"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortrait);
			}
			if ([orientation isEqualToString:@"upsideDown"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortraitUpsideDown);
			}
		}
	}
	
	return supportedOrientationsMask;
}

@end
