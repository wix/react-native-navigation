#import <XCTest/XCTest.h>
#import "RNNBasePresenter.h"
#import <OCMock/OCMock.h>
#import "UIViewController+RNNOptions.h"
#import "RNNComponentViewController.h"

@interface RNNBottomTabPresenterTest : XCTestCase

@property(nonatomic, strong) RNNBasePresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNComponentViewController *boundViewController;
@property(nonatomic, strong) id mockBoundViewController;

@end

@implementation RNNBottomTabPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNBasePresenter alloc] init];
    self.boundViewController = [RNNComponentViewController new];
    self.mockBoundViewController = [OCMockObject partialMockForObject:self.boundViewController];
    [self.uut boundViewController:self.mockBoundViewController];
    self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)tearDown {
    [super tearDown];
    [self.mockBoundViewController stopMocking];
    self.boundViewController = nil;
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeOnlyWhenParentIsUITabBarController {
    [[self.mockBoundViewController reject] setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithValue {
    OCMStub([self.mockBoundViewController parentViewController]).andReturn([UITabBarController new]);
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [[self.mockBoundViewController expect] setTabBarItemBadge:self.options.bottomTab.badge.get];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldNotCalledOnUITabBarController {
    [self.uut boundViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [[self.mockBoundViewController reject] setTabBarItemBadge:[[RNNBottomTabOptions alloc] initWithDict:@{@"badge": @"badge"}]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldWhenNoValue {
    [self.uut boundViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = nil;
    [[self.mockBoundViewController reject] setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testGetPreferredStatusBarStyle_returnLightIfLight {
    RNNNavigationOptions * lightOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    lightOptions.statusBar.style = [[Text alloc] initWithValue:@"light"];

    XCTAssertEqual([_uut getStatusBarStyle:lightOptions], UIStatusBarStyleLightContent);
}

- (void)testGetPreferredStatusBarStyle_returnDefaultIfDark {
    RNNNavigationOptions * darkOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    darkOptions.statusBar.style = [[Text alloc] initWithValue:@"dark"];

    XCTAssertEqual([_uut getStatusBarStyle:darkOptions], UIStatusBarStyleDefault);
}

- (void)testGetPreferredStatusBarStyle_returnDefaultIfNil {
    RNNNavigationOptions * options = [[RNNNavigationOptions alloc] initEmptyOptions];

    XCTAssertEqual([_uut getStatusBarStyle:options], UIStatusBarStyleDefault);
}

- (void)testGetPreferredStatusBarStyle_considersDefaultOptions {
    RNNNavigationOptions * options = [[RNNNavigationOptions alloc] initEmptyOptions];
    RNNNavigationOptions * lightOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    lightOptions.statusBar.style = [[Text alloc] initWithValue:@"light"];
    [_uut setDefaultOptions:lightOptions];

    XCTAssertEqual([_uut getStatusBarStyle:options], UIStatusBarStyleLightContent);
}


@end
