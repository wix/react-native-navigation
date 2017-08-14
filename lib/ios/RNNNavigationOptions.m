#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>


@implementation RNNNavigationOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)navigationOptions {
	self = [super init];
	self.topBarBackgroundColor = [navigationOptions objectForKey:@"topBarBackgroundColor"];
	self.statusBarHidden = [navigationOptions objectForKey:@"statusBarHidden"];
	self.title = [navigationOptions objectForKey:@"title"];
	self.topBarTextColor = [navigationOptions objectForKey:@"topBarTextColor"];
	self.screenBackgroundColor = [navigationOptions objectForKey:@"screenBackgroundColor"];
	self.topBarTextFontFamily = [navigationOptions objectForKey:@"topBarTextFontFamily"];
	self.topBarHidden = [navigationOptions objectForKey:@"topBarHidden"];
	self.topBarHideOnScroll = [navigationOptions objectForKey:@"topBarHideOnScroll"];
	self.topBarButtonColor = [navigationOptions objectForKey:@"topBarButtonColor"];
	self.topBarTranslucent = [navigationOptions objectForKey:@"topBarTranslucent"];
	self.tabBadge = [navigationOptions objectForKey:@"tabBadge"];
	self.topBarTextFontSize = [navigationOptions objectForKey:@"topBarTextFontSize"];
	self.topBarNoBorder = [navigationOptions objectForKey:@"topBarNoBorder"];

	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

-(void)applyOn:(UIViewController*)viewController {
	if (self.topBarBackgroundColor) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.topBarBackgroundColor];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	} else {
		viewController.navigationController.navigationBar.barTintColor = nil;
	}
	
	if (self.title) {
		viewController.navigationItem.title = self.title;
	}
	
	if (self.topBarTextFontFamily || self.topBarTextColor || self.topBarTextFontSize){
		NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.topBarTextColor) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.topBarTextColor];
		}
		if (self.topBarTextFontFamily){
			if(self.topBarTextFontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.topBarTextFontFamily size:[self.topBarTextFontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.topBarTextFontFamily size:20];
			}
		} else if (self.topBarTextFontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.topBarTextFontSize floatValue]];
		}
		viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
	}
	
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
	}
	
	if (self.topBarHidden){
		if ([self.topBarHidden boolValue]) {
			[viewController.navigationController setNavigationBarHidden:YES animated:YES];
		} else {
			[viewController.navigationController setNavigationBarHidden:NO animated:YES];
		}
	}
	
	if (self.topBarHideOnScroll) {
		BOOL topBarHideOnScrollBool = [self.topBarHideOnScroll boolValue];
		if (topBarHideOnScrollBool) {
			viewController.navigationController.hidesBarsOnSwipe = YES;
		} else {
			viewController.navigationController.hidesBarsOnSwipe = NO;
		}
	}
	
	if (self.topBarButtonColor) {
		UIColor* buttonColor = [RCTConvert UIColor:self.topBarButtonColor];
		viewController.navigationController.navigationBar.tintColor = buttonColor;
	} else {
		viewController.navigationController.navigationBar.tintColor = nil;
	}
      
	if (self.tabBadge) {
		NSString *badge = [RCTConvert NSString:self.tabBadge];
		if (viewController.navigationController) {
			viewController.navigationController.tabBarItem.badgeValue = badge;
		} else {
			viewController.tabBarItem.badgeValue = badge;
	  }
	}
	
	if (self.topBarTranslucent) {
		if ([self.topBarTranslucent boolValue]) {
			viewController.navigationController.navigationBar.translucent = YES;
		} else {
			viewController.navigationController.navigationBar.translucent = NO;
		}		
	}

	if (self.topBarNoBorder) {
		if ([self.topBarNoBorder boolValue]) {
			viewController.navigationController.navigationBar
			.shadowImage = [[UIImage alloc] init];
		} else {
			viewController.navigationController.navigationBar
			.shadowImage = nil;
		}
	}

}
@end
