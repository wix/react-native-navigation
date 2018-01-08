#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNNavigationButtons.h"



@implementation RNNNavigationOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)navigationOptions {
	self = [super init];
	self.topBar = [[RNNTopBarOptions alloc] initWithDict:[navigationOptions objectForKey:@"topBar"]];
	self.topTab = [[RNNTopTabOptions alloc] initWithDict:[navigationOptions objectForKey:@"topTab"]];
	self.bottomTabs = [[RNNBottomTabsOptions alloc] initWithDict:[navigationOptions objectForKey:@"bottomTabs"]];
	self.sideMenu = [[RNNSideMenuOptions alloc] initWithDict:[navigationOptions objectForKey:@"sideMenu"]];
	self.bottomTab = [[RNNBottomTabOptions alloc] initWithDict:[navigationOptions objectForKey:@"bottomTab"]];
	self.screen = [[RNNScreenOptions alloc] initWithDict:[navigationOptions objectForKey:@"screen"]];
    
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([[self valueForKey:key] isKindOfClass:[RNNOptions class]]) {
			RNNOptions* options = [self valueForKey:key];
			[options mergeWith:[otherOptions objectForKey:key]];
		} else {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

-(void)applyOn:(UIViewController*)viewController {
	[_defaultOptions applyOn:viewController];
	
	[self.topBar applyOn:viewController];
	[self.bottomTabs applyOn:viewController];
	[self.topTab applyOn:viewController];
	[self.bottomTab applyOn:viewController];
	[self.sideMenu applyOn:viewController];
	[self.screen applyOn:viewController];
}

@end
