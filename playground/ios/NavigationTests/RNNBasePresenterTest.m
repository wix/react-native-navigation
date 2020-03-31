#import <XCTest/XCTest.h>
#import "RNNBasePresenter.h"
#import <OCMock/OCMock.h>
#import "UIViewController+RNNOptions.h"
#import "RNNComponentViewController+Utils.h"

@interface RNNBasePresenterTest : XCTestCase

@property(nonatomic, strong) RNNBasePresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNComponentViewController *boundViewController;
@property(nonatomic, strong) id mockBoundViewController;

@end

@implementation RNNBasePresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNBasePresenter alloc] initWithDefaultOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
    self.boundViewController = [RNNComponentViewController createWithComponentId:@"componentId" initialOptions:[RNNNavigationOptions emptyOptions]];
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
    [[self.mockBoundViewController reject] setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldNotCalledOnUITabBarController {
    [self.uut bindViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [[self.mockBoundViewController reject] setTabBarItemBadge:[[RNNBottomTabOptions alloc] initWithDict:@{@"badge": @"badge"}]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testApplyOptions_setTabBarItemBadgeShouldWhenNoValue {
    [self.uut bindViewController:self.mockBoundViewController];
    self.options.bottomTab.badge = nil;
    [[self.mockBoundViewController reject] setTabBarItemBadge:[OCMArg any]];
    [self.uut applyOptions:self.options];
    [self.mockBoundViewController verify];
}

- (void)testGetPreferredStatusBarStyle_returnLightIfLight {
	self.boundViewController.options.statusBar.style = [[Text alloc] initWithValue:@"light"];
	
    XCTAssertEqual([_uut getStatusBarStyle], UIStatusBarStyleLightContent);
}

- (void)testGetPreferredStatusBarStyle_returnDark {
    self.boundViewController.options.statusBar.style = [[Text alloc] initWithValue:@"dark"];

    XCTAssertEqual([_uut getStatusBarStyle], UIStatusBarStyleDarkContent);
}

- (void)testGetPreferredStatusBarStyle_returnDefaultIfNil {
	self.boundViewController.options.statusBar.style = nil;
    XCTAssertEqual([_uut getStatusBarStyle], UIStatusBarStyleDefault);
}

- (void)testGetPreferredStatusBarStyle_considersDefaultOptions {
    RNNNavigationOptions * lightOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    lightOptions.statusBar.style = [[Text alloc] initWithValue:@"light"];
    [_uut setDefaultOptions:lightOptions];

    XCTAssertEqual([_uut getStatusBarStyle], UIStatusBarStyleLightContent);
}

- (void)testApplyOptionsOnInit_setSwipeToDismiss {
    self.options.modal.swipeToDismiss = [[Bool alloc] initWithBOOL:NO];
	XCTAssertFalse(_boundViewController.modalInPresentation);
    [self.uut applyOptionsOnInit:self.options];
	XCTAssertTrue(_boundViewController.modalInPresentation);
}

@end
