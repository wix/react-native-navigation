#import <XCTest/XCTest.h>
#import "RNNBasePresenter.h"
#import <OCMock/OCMock.h>
#import "UIViewController+RNNOptions.h"
#import "RNNRootViewController.h"

@interface RNNBottomTabPresenterTest : XCTestCase

@property(nonatomic, strong) RNNBasePresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNRootViewController *boundViewController;
@property(nonatomic, strong) id mockBoundViewController;

@end

@implementation RNNBottomTabPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNBasePresenter alloc] init];
    self.boundViewController = [RNNRootViewController new];
    self.mockBoundViewController = [OCMockObject partialMockForObject:self.boundViewController];
    [self.uut bindViewController:self.mockBoundViewController];
    self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)tearDown {
    [super tearDown];
    [self.mockBoundViewController stopMocking];
    self.boundViewController = nil;
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeOnlyWhenParentIsUITabBarController {
    [[self.mockBoundViewController reject] rnn_setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithValue {
    OCMStub([self.mockBoundViewController parentViewController]).andReturn([UITabBarController new]);
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [[self.mockBoundViewController expect] rnn_setTabBarItemBadge:self.options.bottomTab.badge.get];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldNotCalledOnUITabBarController {
    [self.uut bindViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [[self.mockBoundViewController reject] rnn_setTabBarItemBadge:[[RNNBottomTabOptions alloc] initWithDict:@{@"badge": @"badge"}]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldWhenNoValue {
    [self.uut bindViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = nil;
    [[self.mockBoundViewController reject] rnn_setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

@end
