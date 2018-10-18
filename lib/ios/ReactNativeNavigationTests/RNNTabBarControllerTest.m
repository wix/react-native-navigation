#import <XCTest/XCTest.h>
#import "RNNTabBarController.h"
#import "RNNNavigationOptions.h"
#import "RNNTabBarPresenter.h"
#import "RNNRootViewController.h"
#import <OCMock/OCMock.h>

@interface RNNTabBarControllerTest : XCTestCase

@property (nonatomic, strong) RNNTabBarController *uut;

@end

@implementation RNNTabBarControllerTest

- (void)setUp {
	[super setUp];
	
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[[UIViewController alloc] init]] options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:[[RNNViewControllerPresenter alloc] init]];
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
	XCTAssertNotNil(self.uut.presenter);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
	UIViewController* vc1 = [[UIViewController alloc] init];
	UIViewController* vc2 = [[UIViewController alloc] init];
	
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[vc1, vc2] options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:[[RNNViewControllerPresenter alloc] init]];
	XCTAssertTrue(self.uut.viewControllers.count == 2);
}

- (void)testInitWithLayoutInfo_shouldInitializeDependencies {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];
	NSArray* childViewControllers = @[[UIViewController new]];
	
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter];
	XCTAssertTrue(self.uut.layoutInfo == layoutInfo);
	XCTAssertTrue(self.uut.options == options);
	XCTAssertTrue(self.uut.presenter == presenter);
	XCTAssertTrue(self.uut.childViewControllers.count == childViewControllers.count);
}

- (void)testInitWithEventEmmiter_shouldInitializeDependencies {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];
	RNNEventEmitter* eventEmmiter = [RNNEventEmitter new];

	NSArray* childViewControllers = @[[UIViewController new]];
	
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options presenter:presenter eventEmitter:eventEmmiter];
	XCTAssertTrue(self.uut.layoutInfo == layoutInfo);
	XCTAssertTrue(self.uut.options == options);
	XCTAssertTrue(self.uut.presenter == presenter);
	XCTAssertTrue(self.uut.childViewControllers.count == childViewControllers.count);
	XCTAssertTrue(self.uut.eventEmitter == eventEmmiter);
}

- (void)testInitWithLayoutInfo_shouldSetDelegate {
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:[[RNNViewControllerPresenter alloc] init]];
	
	XCTAssertTrue(self.uut.delegate == self.uut);
}

- (void)testWillMoveToParent_invokePresenterApplyOptionsOnWillMoveToParent {
	id presenterMock = [OCMockObject partialMockForObject:[RNNTabBarPresenter new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:presenterMock];

	[[presenterMock expect] applyOptionsOnWillMoveToParentViewController:options];
	[self.uut willMoveToParentViewController:[UIViewController new]];
	[presenterMock verify];
}

- (void)testWillMoveToParent_shouldNotInvokePresenterApplyOptionsOnWillMoveToNilParent {
	id presenterMock = [OCMockObject partialMockForObject:[RNNTabBarPresenter new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:presenterMock];
	
	[[presenterMock reject] applyOptionsOnWillMoveToParentViewController:options];
	[self.uut willMoveToParentViewController:nil];
	[presenterMock verify];
}

- (void)testOnChildAppear_shouldInvokePresenterApplyOptionsWithResolvedOptions {
	id presenterMock = [OCMockObject partialMockForObject:[RNNTabBarPresenter new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:presenterMock];
	
	[[presenterMock expect] applyOptions:[OCMArg any]];
	[uut onChildWillAppear];
	[presenterMock verify];
}

- (void)testMergeOptions_shouldInvokePresenterMergeOptions {
	id presenterMock = [OCMockObject partialMockForObject:[RNNTabBarPresenter new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:presenterMock];
	
	[(RNNTabBarPresenter *)[presenterMock expect] mergeOptions:options resolvedOptions:nil];
	[uut mergeOptions:options];
	[presenterMock verify];
}

- (void)testMergeOptions_shouldInvokeParentMergeOptions {
	id parentMock = [OCMockObject partialMockForObject:[RNNTabBarController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:[RNNTabBarPresenter new]];
	[parentMock addChildViewController:uut];
	
	[(RNNTabBarController *)[parentMock expect] mergeOptions:options];
	[uut mergeOptions:options];
	[parentMock verify];
}

- (void)testOnChildAppear_shouldInvokeParentOnChildAppear {
	id parentMock = [OCMockObject partialMockForObject:[RNNTabBarController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:options presenter:[RNNTabBarPresenter new]];
	[parentMock addChildViewController:uut];
	
	[[parentMock expect] onChildWillAppear];
	[uut onChildWillAppear];
	[parentMock verify];
}

- (void)testGetCurrentChild_shouldInvokeSelectedViewControllerGetCurrentChild {
	id childMock = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[childMock] options:options presenter:[RNNTabBarPresenter new]];
	
	[[childMock expect] getCurrentChild];
	[uut getCurrentChild];
	[childMock verify];
}

- (void)testGetCurrentChild_shouldInvokeOnSelectedViewController {
	id childMock = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[UIViewController new], childMock] options:options presenter:[RNNTabBarPresenter new]];
	[uut setSelectedIndex:1];
	
	[[childMock expect] getCurrentChild];
	[uut getCurrentChild];
	[childMock verify];
}

- (void)testPreferredStatusBarStyle_shouldInvokeSelectedViewControllerPreferredStatusBarStyle {
	id childMock = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[childMock] options:options presenter:[RNNTabBarPresenter new]];
	
	[[childMock expect] preferredStatusBarStyle];
	[uut preferredStatusBarStyle];
	[childMock verify];
}

- (void)testPreferredStatusBarStyle_shouldInvokeOnSelectedViewController {
	id childMock = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[UIViewController new], childMock] options:options presenter:[RNNTabBarPresenter new]];
	[uut setSelectedIndex:1];
	
	[[childMock expect] preferredStatusBarStyle];
	[uut preferredStatusBarStyle];
	[childMock verify];
}

- (void)testTabBarControllerDidSelectViewControllerDelegate_shouldInvokeSendBottomTabSelectedEvent {
	id eventEmmiterMock = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	UIViewController* vc = [UIViewController new];
	UIViewController* vc2 = [UIViewController new];
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[vc, vc2] options:options presenter:[RNNTabBarPresenter new] eventEmitter:eventEmmiterMock];
	[uut setSelectedIndex:1];
	[[eventEmmiterMock expect] sendBottomTabSelected:[OCMArg any] unselected:[OCMArg any]];
	[uut tabBarController:uut didSelectViewController:vc2];
	[eventEmmiterMock verify];
}

- (void)testSetSelectedIndexByComponentID_ShouldSetSelectedIndexWithCorrectIndex {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	layoutInfo.componentId = @"componentId";
	
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:nil presenter:nil options:nil];
	
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[UIViewController new], vc] options:nil presenter:[RNNTabBarPresenter new]];
	[uut setSelectedIndexByComponentID:@"componentId"];
	XCTAssertTrue(uut.selectedIndex == 1);
}

@end
