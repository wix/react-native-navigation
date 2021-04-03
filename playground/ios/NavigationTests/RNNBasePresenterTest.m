#import "RNNBasePresenter.h"
#import "RNNComponentViewController+Utils.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNBasePresenterTest : XCTestCase

@property(nonatomic, strong) RNNBasePresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNComponentViewController *boundViewController;
@property(nonatomic, strong) id mockBoundViewController;

@end

@implementation RNNBasePresenterTest

- (void)setUp {
    [super setUp];
    self.uut =
        [[RNNBasePresenter alloc] initWithDefaultOptions:[RNNNavigationOptions emptyOptions]];
    self.boundViewController =
        [RNNComponentViewController createWithComponentId:@"componentId"
                                           initialOptions:[RNNNavigationOptions emptyOptions]];
    self.mockBoundViewController = [OCMockObject partialMockForObject:self.boundViewController];
    [self.uut bindViewController:self.mockBoundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
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
    [[self.mockBoundViewController reject]
        setTabBarItemBadge:[[RNNBottomTabOptions alloc] initWithDict:@{@"badge" : @"badge"}]];
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
    RNNNavigationOptions *lightOptions = [RNNNavigationOptions emptyOptions];
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

- (void)testHidesBottomBarWhenPushed_showsBar {
    _boundViewController.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    XCTAssertTrue(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testHidesBottomBarWhenPushed_hidesBar {
    _boundViewController.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:YES];
    XCTAssertFalse(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testHidesBottomBarWhenPushed_resolveParentShowsBar {
    UINavigationController *nvc =
        [[UINavigationController alloc] initWithRootViewController:self.boundViewController];
    nvc.options = [RNNNavigationOptions emptyOptions];
    nvc.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    XCTAssertTrue(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testHidesBottomBarWhenPushed_resolveParentHidesBar {
    UINavigationController *nvc =
        [[UINavigationController alloc] initWithRootViewController:self.boundViewController];
    nvc.options = [RNNNavigationOptions emptyOptions];
    nvc.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:YES];
    XCTAssertFalse(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testHidesBottomBarWhenPushed_resolveChildShowsBarBeforeParent {
    UINavigationController *nvc =
        [[UINavigationController alloc] initWithRootViewController:self.boundViewController];
    nvc.options = [RNNNavigationOptions emptyOptions];
    nvc.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    XCTAssertTrue(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testHidesBottomBarWhenPushed_resolveChildHidesBarBeforeParent {
    UINavigationController *nvc =
        [[UINavigationController alloc] initWithRootViewController:self.boundViewController];
    nvc.options = [RNNNavigationOptions emptyOptions];
    self.boundViewController.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    nvc.options.bottomTabs.visible = [[Bool alloc] initWithBOOL:YES];
    XCTAssertTrue(_boundViewController.hidesBottomBarWhenPushed);
}

- (void)testMergeOptions_updatesStatusBarVisibility {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    mergeOptions.statusBar.visible = [Bool withValue:NO];

    [[self.mockBoundViewController expect] setNeedsStatusBarAppearanceUpdate];
    [self.uut mergeOptions:mergeOptions resolvedOptions:nil];
    [self.mockBoundViewController verify];
}

@end
