#import <XCTest/XCTest.h>
#import "UITabBarController+RNNOptions.h"

@interface UITabBarController_RNNOptionsTest : XCTestCase

@property (nonatomic, retain) UITabBarController* uut;

@end

@implementation UITabBarController_RNNOptionsTest

- (void)setUp {
    [super setUp];
	self.uut = [UITabBarController new];
}

- (void)test_tabBarTranslucent_true {
	[self.uut rnn_setTabBarTranslucent:YES];
	XCTAssertTrue(self.uut.tabBar.translucent);
}

- (void)test_tabBarTranslucent_false {
	[self.uut rnn_setTabBarTranslucent:NO];
	XCTAssertFalse(self.uut.tabBar.translucent);
}

- (void)test_tabBarHideShadow_default {
	XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_true {
	[self.uut rnn_setTabBarHideShadow:YES];
	XCTAssertTrue(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_false {
	[self.uut rnn_setTabBarHideShadow:NO];
	XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarBackgroundColor {
	UIColor* tabBarBackgroundColor = [UIColor redColor];

	[self.uut rnn_setTabBarBackgroundColor:tabBarBackgroundColor];
	XCTAssertTrue([self.uut.tabBar.barTintColor isEqual:tabBarBackgroundColor]);
}

@end
