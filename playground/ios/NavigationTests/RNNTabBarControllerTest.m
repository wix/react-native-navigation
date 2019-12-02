#import <XCTest/XCTest.h>
#import "RNNBottomTabsController.h"
#import "RNNComponentViewController.h"
#import "RNNStackController.h"
#import <OCMock/OCMock.h>

@interface RNNTabBarControllerTest : XCTestCase

@property(nonatomic, strong) RNNBottomTabsController * uut;
@property(nonatomic, strong) id mockChildViewController;
@property(nonatomic, strong) id mockEventEmitter;
@property(nonatomic, strong) id mockTabBarPresenter;

@end

@implementation RNNTabBarControllerTest

- (void)setUp {
    [super setUp];

    id tabBarClassMock = OCMClassMock([RNNBottomTabsController class]);
    OCMStub([tabBarClassMock parentViewController]).andReturn([OCMockObject partialMockForObject:[RNNBottomTabsController new]]);

    self.mockTabBarPresenter = [OCMockObject partialMockForObject:[[RNNBottomTabsPresenter alloc] init]];
    self.mockChildViewController = [OCMockObject partialMockForObject:[RNNComponentViewController new]];
    self.mockEventEmitter = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
    self.uut = [OCMockObject partialMockForObject:[[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:self.mockTabBarPresenter eventEmitter:self.mockEventEmitter childViewControllers:@[[[UIViewController alloc] init]]]];
    OCMStub([self.uut selectedViewController]).andReturn(self.mockChildViewController);
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
    XCTAssertNotNil([self.uut presenter]);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
    UIViewController *vc1 = [[UIViewController alloc] init];
    UIViewController *vc2 = [[UIViewController alloc] init];

    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNComponentPresenter alloc] init] eventEmitter:nil childViewControllers:@[vc1, vc2]];
    XCTAssertTrue(uut.viewControllers.count == 2);
}

- (void)testInitWithLayoutInfo_shouldInitializeDependencies {
    RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];
    RNNBottomTabsPresenter *presenter = [[RNNBottomTabsPresenter alloc] init];
    NSArray *childViewControllers = @[[UIViewController new]];

    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:layoutInfo creator:nil options:options defaultOptions:nil presenter:presenter eventEmitter:nil childViewControllers:childViewControllers];
    XCTAssertTrue(uut.layoutInfo == layoutInfo);
    XCTAssertTrue(uut.options == options);
    XCTAssertTrue(uut.presenter == presenter);
    XCTAssertTrue(uut.childViewControllers.count == childViewControllers.count);
}

- (void)testInitWithEventEmmiter_shouldInitializeDependencies {
    RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];
    RNNBottomTabsPresenter *presenter = [[RNNBottomTabsPresenter alloc] init];
    RNNEventEmitter *eventEmmiter = [RNNEventEmitter new];

    NSArray *childViewControllers = @[[UIViewController new]];

    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:layoutInfo creator:nil options:options defaultOptions:nil presenter:presenter eventEmitter:eventEmmiter childViewControllers:childViewControllers];
    XCTAssertTrue(uut.layoutInfo == layoutInfo);
    XCTAssertTrue(uut.options == options);
    XCTAssertTrue(uut.presenter == presenter);
    XCTAssertTrue(uut.childViewControllers.count == childViewControllers.count);
    XCTAssertTrue(uut.eventEmitter == eventEmmiter);
}

- (void)testInitWithLayoutInfo_shouldSetDelegate {
    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNBasePresenter alloc] init] eventEmitter:nil childViewControllers:nil];

    XCTAssertTrue(uut.delegate == uut);
}

- (void)testWillMoveToParent_shouldNotInvokePresenterApplyOptionsOnWillMoveToNilParent {
    [[self.mockTabBarPresenter reject] applyOptionsOnWillMoveToParentViewController:[self.uut options]];
    [self.uut willMoveToParentViewController:nil];
    [self.mockTabBarPresenter verify];
}

- (void)testOnChildAppear_shouldInvokePresenterApplyOptionsWithResolvedOptions {
    [[self.mockTabBarPresenter expect] applyOptions:[OCMArg any]];
    [self.uut onChildWillAppear];
    [self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokePresenterMergeOptions {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];

    [(RNNBottomTabsPresenter *) [self.mockTabBarPresenter expect] mergeOptions:options resolvedOptions:[self.uut options]];
    [self.uut mergeOptions:options];
    [self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokeParentMergeOptions {
    id parentMock = [OCMockObject partialMockForObject:[RNNComponentViewController new]];
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];

    OCMStub([self.uut parentViewController]).andReturn(parentMock);
    [((RNNComponentViewController *) [parentMock expect]) mergeChildOptions:options];
    [self.uut mergeOptions:options];
    [parentMock verify];
}

- (void)testOnChildAppear_shouldInvokeParentOnChildAppear {
    id parentMock = [OCMockObject partialMockForObject:[RNNStackController new]];

    OCMStub([self.uut parentViewController]).andReturn(parentMock);

    [[parentMock expect] onChildWillAppear];
    [self.uut onChildWillAppear];
    [parentMock verify];
}

- (void)testViewDidLayoutSubviews_delegateToPresenter {
    [[[self mockTabBarPresenter] expect] viewDidLayoutSubviews];
    [[self uut] viewDidLayoutSubviews];
    [[self mockTabBarPresenter] verify];
}

- (void)testGetCurrentChild_shouldReturnSelectedViewController {
    XCTAssertEqual([self.uut getCurrentChild], [(RNNBottomTabsController *) self.uut selectedViewController]);
}

- (void)testPreferredStatusBarStyle_shouldInvokeSelectedViewControllerPreferredStatusBarStyle {
    [[self.mockTabBarPresenter expect] getStatusBarStyle:[OCMArg any]];
    [self.uut preferredStatusBarStyle];
    [self.mockTabBarPresenter verify];
}

- (void)testTabBarControllerDidSelectViewControllerDelegate_shouldInvokeSendBottomTabSelectedEvent {
    NSUInteger selectedIndex = 2;
    OCMStub([self.uut selectedIndex]).andReturn(selectedIndex);

    [[self.mockEventEmitter expect] sendBottomTabSelected:@(selectedIndex) unselected:@(0)];
    [self.uut tabBarController:self.uut didSelectViewController:[UIViewController new]];
    [self.mockEventEmitter verify];
}

- (void)testSetSelectedIndexByComponentID_ShouldSetSelectedIndexWithCorrectIndex {
    RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
    layoutInfo.componentId = @"componentId";

    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:nil presenter:nil options:nil defaultOptions:nil];

    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:[RNNBottomTabsPresenter new] eventEmitter:nil childViewControllers:@[[UIViewController new], vc]];
    [uut setSelectedIndexByComponentID:@"componentId"];
    XCTAssertTrue(uut.selectedIndex == 1);
}

- (void)testSetSelectedIndex_ShouldSetSelectedIndexWithCurrentTabIndex {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initEmptyOptions];
    options.bottomTabs.currentTabIndex = [[IntNumber alloc] initWithValue:@(1)];

    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:nil options:nil defaultOptions:nil];
    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:options defaultOptions:nil presenter:[RNNBottomTabsPresenter new] eventEmitter:nil childViewControllers:@[[UIViewController new], vc]];

    XCTAssertTrue(uut.selectedIndex == 1);
}

@end
