#import <XCTest/XCTest.h>
#import "RNNBasePresenter.h"
#import <OCMock/OCMock.h>
#import "UIViewController+RNNOptions.h"
#import "RNNRootViewController.h"

@interface RNNBottomTabPresenterTest : XCTestCase

@property (nonatomic, strong) RNNBasePresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) RNNRootViewController* bindedViewController;
@property (nonatomic, strong) id mockBindedViewController;

@end

@implementation RNNBottomTabPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNBasePresenter alloc] init];
	  self.bindedViewController = [RNNRootViewController new];
    self.mockBindedViewController = [OCMockObject partialMockForObject:self.bindedViewController];
    [self.uut bindViewController:self.mockBindedViewController];
    self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)tearDown {
	[super tearDown];
	[self.mockBindedViewController stopMocking];
	self.bindedViewController = nil;
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeOnlyWhenParentIsUITabBarController {
	[[self.mockBindedViewController reject] rnn_setTabBarItemBadge:[OCMArg any]];
	[self.uut applyOptions:self.options];
	[self.mockBindedViewController verify];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithValue {
	OCMStub([self.mockBindedViewController parentViewController]).andReturn([UITabBarController new]);
	self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
	[[self.mockBindedViewController expect] rnn_setTabBarItemBadge:@"badge"];
	[self.uut applyOptions:self.options];
	[self.mockBindedViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldNotCalledOnUITabBarController {
	[self.uut bindViewController:self.mockBindedViewController];
	self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
	[[self.mockBindedViewController reject] rnn_setTabBarItemBadge:@"badge"];
	[self.uut applyOptions:self.options];
	[self.mockBindedViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldWhenNoValue {
	[self.uut bindViewController:self.mockBindedViewController];
	self.options.bottomTab.badge = nil;
	[[self.mockBindedViewController reject] rnn_setTabBarItemBadge:[OCMArg any]];
	[self.uut applyOptions:self.options];
	[self.mockBindedViewController verify];
}

@end
