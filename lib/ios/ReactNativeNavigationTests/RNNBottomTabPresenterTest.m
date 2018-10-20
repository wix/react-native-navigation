#import <XCTest/XCTest.h>
#import "RNNBottomTabPresenter.h"
#import <OCMock/OCMock.h>
#import "UIViewController+RNNOptions.h"

@interface RNNBottomTabPresenterTest : XCTestCase

@property (nonatomic, strong) RNNBottomTabPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) id bindedViewController;

@end

@implementation RNNBottomTabPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNBottomTabPresenter alloc] init];
    self.bindedViewController = [OCMockObject partialMockForObject:[UIViewController new]];
    [self.uut bindViewController:self.bindedViewController];
    self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithDefaultWhenParentIsUITabBarController {
	UITabBarController* tabBarController = [[UITabBarController alloc] init];
	[tabBarController setViewControllers:@[self.bindedViewController]];
	[[self.bindedViewController expect] rnn_setTabBarItemBadge:nil];
	[self.uut applyOptions:self.options];
	[self.bindedViewController verify];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeOnlyWhenParentIsUITabBarController {
	[[self.bindedViewController reject] rnn_setTabBarItemBadge:[OCMArg any]];
	[self.uut applyOptions:self.options];
	[self.bindedViewController verify];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithValue {
	UITabBarController* tabBarController = [[UITabBarController alloc] init];
	[tabBarController setViewControllers:@[self.bindedViewController]];
	self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
	[[self.bindedViewController expect] rnn_setTabBarItemBadge:@"badge"];
	[self.uut applyOptions:self.options];
	[self.bindedViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldNotCalledOnUITabBarController {
	self.bindedViewController = [OCMockObject partialMockForObject:[UITabBarController new]];
	[self.uut bindViewController:self.bindedViewController];
	self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
	[[self.bindedViewController reject] rnn_setTabBarItemBadge:@"badge"];
	[self.uut applyOptions:self.options];
	[self.bindedViewController verify];
}

//- (void)test_tabBarTextFontFamily_validFont {
//	UIViewController* viewController = [UIViewController new];
//
//	NSString* inputFont = @"HelveticaNeue";
//	UIFont* expectedFont = [UIFont fontWithName:inputFont size:10];
//
//	self.uut.fontFamily = inputFont;
//	self.uut.text = @"Tab 1";
//
//	[self.uut rnn_set];
//
//	NSDictionary* attributes = [viewController.tabBarItem titleTextAttributesForState:UIControlStateNormal];
//	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
//}

//- (void)test_tabBarTextFontSize_withoutTextFontFamily_withoutTextColor {
//	UIViewController* viewController = [UIViewController new];
//
//	UIFont* expectedFont = [UIFont systemFontOfSize:15];
//
//	self.uut.fontSize = @(15);
//	self.uut.text = @"Tab 1";
//
//	[self.uut applyOn:viewController];
//
//	NSDictionary* attributes = [viewController.tabBarItem titleTextAttributesForState:UIControlStateNormal];
//	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
//}
//
//- (void)test_tabBarTextFontSize_withoutTextFontFamily {
//	UIViewController* viewController = [UIViewController new];
//
//	UIFont* expectedFont = [UIFont systemFontOfSize:15];
//
//	self.uut.fontSize = @(15);
//	self.uut.text = @"Tab 1";
//
//	[self.uut applyOn:viewController];
//
//	NSDictionary* attributes = [viewController.tabBarItem titleTextAttributesForState:UIControlStateNormal];
//	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
//}
//
//- (void)test_tabBarTextFontSize_withTextFontFamily_withTextColor {
//	UIViewController* viewController = [UIViewController new];
//
//	NSString* inputFont = @"HelveticaNeue";
//	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
//
//	self.uut.fontSize = @(15);
//	self.uut.text = @"Tab 1";
//	self.uut.fontFamily = inputFont;
//	self.uut.textColor = @(4279979127);
//	[self.uut applyOn:viewController];
//
//	NSDictionary* attributes = [viewController.tabBarItem titleTextAttributesForState:UIControlStateNormal];
//	UIColor* color = attributes[NSForegroundColorAttributeName];
//	UIColor* expectedColor = [RCTConvert UIColor:@(4279979127)];
//
//	XCTAssertTrue([color isEqual:expectedColor]);
//	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
//}

@end
