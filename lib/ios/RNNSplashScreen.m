
#import "RNNSplashScreen.h"
#import <UIKit/UIKit.h>

@implementation RNNSplashScreen

+(void)show {
	
	UIApplication.sharedApplication.delegate.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
	UIApplication.sharedApplication.delegate.window.backgroundColor = [UIColor whiteColor];
	
	CGRect screenBounds = [UIScreen mainScreen].bounds;
	UIView *splashView = nil;
	
	NSString* launchStoryBoard = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"UILaunchStoryboardName"];
	if (launchStoryBoard != nil) {//load the splash from the storyboard that's defined in the info.plist as the LaunchScreen
		@try
		{
			splashView = [[NSBundle mainBundle] loadNibNamed:launchStoryBoard owner:self options:nil][0];
			if (splashView != nil)
			{
				splashView.frame = CGRectMake(0, 0, screenBounds.size.width, screenBounds.size.height);
			}
		}
		@catch(NSException *e)
		{
			splashView = nil;
		}
	}
	else {//load the splash from the DEfault image or from LaunchImage in the xcassets
		CGFloat screenHeight = screenBounds.size.height;
		
		NSString* imageName = @"Default";
		if (screenHeight == 568)
			imageName = [imageName stringByAppendingString:@"-568h"];
		else if (screenHeight == 667)
			imageName = [imageName stringByAppendingString:@"-667h"];
		else if (screenHeight == 736)
			imageName = [imageName stringByAppendingString:@"-736h"];
		
		//xcassets LaunchImage files
		UIImage *image = [UIImage imageNamed:imageName];
		if (image == nil) {
			imageName = @"LaunchImage";
			
			if (screenHeight == 480)
				imageName = [imageName stringByAppendingString:@"-700"];
			if (screenHeight == 568)
				imageName = [imageName stringByAppendingString:@"-700-568h"];
			else if (screenHeight == 667)
				imageName = [imageName stringByAppendingString:@"-800-667h"];
			else if (screenHeight == 736)
				imageName = [imageName stringByAppendingString:@"-800-Portrait-736h"];
			else if (screenHeight == 812)
				imageName = [imageName stringByAppendingString:@"-1100-Portrait-2436h"];
			else if (screenHeight == 375)
				imageName = [imageName stringByAppendingString:@"-1100-Landscape-2436h"];
			
			image = [UIImage imageNamed:imageName];
		}
		
		if (image != nil) {
			splashView = [[UIImageView alloc] initWithImage:image];
		}
	}
	
	if (splashView != nil) {
		RNNSplashScreen *splashVC = [[RNNSplashScreen alloc] init];
		splashVC.view = splashView;
		
		id<UIApplicationDelegate> appDelegate = [UIApplication sharedApplication].delegate;
		appDelegate.window.rootViewController = splashVC;
		[appDelegate.window makeKeyAndVisible];
	}
}

@end
