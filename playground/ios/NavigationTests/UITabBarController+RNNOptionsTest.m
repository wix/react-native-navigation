#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "UITabBarController+RNNOptions.h"
#import "UITabBar+utils.h"

@interface UITabBarController_RNNOptionsTest : XCTestCase

@property (nonatomic, retain) UITabBarController* uut;

@end

@implementation UITabBarController_RNNOptionsTest

- (void)setUp {
    [super setUp];
	self.uut = [OCMockObject partialMockForObject:[UITabBarController new]];
	OCMStub([self.uut tabBar]).andReturn([OCMockObject partialMockForObject:[UITabBar new]]);
}

- (void)test_tabBarTranslucent_true {
	[self.uut setTabBarTranslucent:YES];
	XCTAssertTrue(self.uut.tabBar.translucent);
}

- (void)test_tabBarTranslucent_false {
	[self.uut setTabBarTranslucent:NO];
	XCTAssertFalse(self.uut.tabBar.translucent);
}

- (void)test_tabBarHideShadow_default {
	XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_true {
	[self.uut setTabBarHideShadow:YES];
	XCTAssertTrue(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_false {
	[self.uut setTabBarHideShadow:NO];
	XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_centerTabItems {
	[[(id)self.uut.tabBar expect] centerTabItems];
	[self.uut centerTabItems];
	[(id)self.uut.tabBar verify];
}

- (void)test_tabBarBackgroundColor {
	UIColor* tabBarBackgroundColor = [UIColor redColor];

	[self.uut setTabBarBackgroundColor:tabBarBackgroundColor];
	XCTAssertTrue([self.uut.tabBar.barTintColor isEqual:tabBarBackgroundColor]);
}

@end
