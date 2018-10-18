#import <XCTest/XCTest.h>
#import "RNNBottomTabsOptions.h"

@interface RNNBottomTabsOptionsTest : XCTestCase

@property (nonatomic, retain) RNNBottomTabsOptions* uut;

@end

@implementation RNNBottomTabsOptionsTest

- (void)setUp {
    [super setUp];
//	self.uut = [[RNNBottomTabsOptions alloc] initEmptyOptions];
}

//- (void)test_tabBarTranslucent_true {
//	UITabBarController* tabBarController = [UITabBarController new];
//	
//	self.uut.translucent = @(1);
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	XCTAssertTrue(tabBarController.tabBar.translucent);
//}
//
//- (void)test_tabBarTranslucent_false {
//	UITabBarController* tabBarController = [UITabBarController new];
//	
//	self.uut.translucent = @(0);
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	XCTAssertFalse(tabBarController.tabBar.translucent);
//}
//
//- (void)test_tabBarHideShadow_default {
//	UITabBarController* tabBarController = [UITabBarController new];
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	XCTAssertFalse(tabBarController.tabBar.clipsToBounds);
//}
//
//- (void)test_tabBarHideShadow_true {
//	UITabBarController* tabBarController = [UITabBarController new];
//	
//	self.uut.hideShadow = @(1);
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	XCTAssertTrue(tabBarController.tabBar.clipsToBounds);
//}
//
//- (void)test_tabBarHideShadow_false {
//	UITabBarController* tabBarController = [UITabBarController new];
//	
//	self.uut.hideShadow = @(0);
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	XCTAssertFalse(tabBarController.tabBar.clipsToBounds);
//}
//
//- (void)test_tabBarBackgroundColor {
//	UITabBarController* tabBarController = [UITabBarController new];
//	
//	self.uut.backgroundColor = @(0xFFFF0000);
//	[self.uut applyOnTabBarController:tabBarController];
//	
//	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
//	XCTAssertTrue([tabBarController.tabBar.barTintColor isEqual:expectedColor]);
//}

@end
