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
	self.screenBackgroundColor = [navigationOptions objectForKey:@"screenBackgroundColor"];
	self.setTabBadge = [navigationOptions objectForKey:@"setTabBadge"];
	self.topBarTextFontFamily = [navigationOptions objectForKey:@"topBarTextFontFamily"];
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
	if (self.topBarTextColor) {
		UIColor* textColor = [RCTConvert UIColor:self.topBarTextColor];
		NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary dictionaryWithDictionary:@{NSForegroundColorAttributeName: textColor}];
		if (self.topBarTextFontFamily) {
			[navigationBarTitleTextAttributes addEntriesFromDictionary:@{NSFontAttributeName: [UIFont fontWithName:self.topBarTextFontFamily size:20]}];
		}
		viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
	} else if (self.topBarTextFontFamily){
		viewController.navigationController.navigationBar.titleTextAttributes = @{NSFontAttributeName: [UIFont fontWithName:self.topBarTextFontFamily size:20]};
	}
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
  }
	if (self.setTabBadge) {
		NSString *badge = [RCTConvert NSString:self.setTabBadge];
		if (viewController.navigationController) {
			viewController.navigationController.tabBarItem.badgeValue = badge;
    } else {
			viewController.tabBarItem.badgeValue = badge;
		}
	}
}



@end
