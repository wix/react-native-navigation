#import <XCTest/XCTest.h>
#import "RNNTabBarController.h"
#import "RNNNavigationOptions.h"
#import "RNNTabBarPresenter.h"
#import "RNNRootViewController.h"
#import "RNNNavigationController.h"
#import <OCMock/OCMock.h>

@interface RNNTabBarControllerTest : XCTestCase

@property (nonatomic, strong) id mockUut;
@property (nonatomic, strong) id mockChildViewController;
@property (nonatomic, strong) id mockEventEmmiter;
@property (nonatomic, strong) id mockTabBarPresenter;

@end

@implementation RNNTabBarControllerTest

- (void)setUp {
	[super setUp];
	
	id tabBarClassMock = OCMClassMock([RNNTabBarController class]);
	OCMStub([tabBarClassMock parentViewController]).andReturn([OCMockObject partialMockForObject:[RNNTabBarController new]]);

	self.mockTabBarPresenter = [OCMockObject partialMockForObject:[[RNNTabBarPresenter alloc] init]];
	self.mockChildViewController = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	self.mockEventEmmiter = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
	self.mockUut = [OCMockObject partialMockForObject:[[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[[UIViewController alloc] init]] options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:self.mockTabBarPresenter eventEmitter:self.mockEventEmmiter]];
	OCMStub([self.mockUut selectedViewController]).andReturn(self.mockChildViewController);
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
	XCTAssertNotNil([self.mockUut presenter]);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
	UIViewController* vc1 = [[UIViewController alloc] init];
	UIViewController* vc2 = [[UIViewController alloc] init];
	
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[vc1, vc2] options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNViewControllerPresenter alloc] init]];
	XCTAssertTrue(uut.viewControllers.count == 2);
}

- (void)testInitWithLayoutInfo_shouldInitializeDependencies {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];
	NSArray* childViewControllers = @[[UIViewController new]];
	
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options defaultOptions:nil presenter:presenter];
	XCTAssertTrue(uut.layoutInfo == layoutInfo);
	XCTAssertTrue(uut.options == options);
	XCTAssertTrue(uut.presenter == presenter);
	XCTAssertTrue(uut.childViewControllers.count == childViewControllers.count);
}

- (void)testInitWithEventEmmiter_shouldInitializeDependencies {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	RNNTabBarPresenter* presenter = [[RNNTabBarPresenter alloc] init];
	RNNEventEmitter* eventEmmiter = [RNNEventEmitter new];

	NSArray* childViewControllers = @[[UIViewController new]];
	
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:layoutInfo childViewControllers:childViewControllers options:options defaultOptions:nil presenter:presenter eventEmitter:eventEmmiter];
	XCTAssertTrue(uut.layoutInfo == layoutInfo);
	XCTAssertTrue(uut.options == options);
	XCTAssertTrue(uut.presenter == presenter);
	XCTAssertTrue(uut.childViewControllers.count == childViewControllers.count);
	XCTAssertTrue(uut.eventEmitter == eventEmmiter);
}

- (void)testInitWithLayoutInfo_shouldSetDelegate {
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNViewControllerPresenter alloc] init]];
	
	XCTAssertTrue(uut.delegate == uut);
}

- (void)testWillMoveToParent_invokePresenterApplyOptionsOnWillMoveToParent {
	[[self.mockTabBarPresenter expect] applyOptionsOnWillMoveToParentViewController:[(RNNTabBarController *)self.mockUut resolveOptions]];
	[self.mockUut willMoveToParentViewController:[UIViewController new]];
	[self.mockTabBarPresenter verify];
}

- (void)testWillMoveToParent_shouldNotInvokePresenterApplyOptionsOnWillMoveToNilParent {
	[[self.mockTabBarPresenter reject] applyOptionsOnWillMoveToParentViewController:[(RNNTabBarController *)self.mockUut options]];
	[self.mockUut willMoveToParentViewController:nil];
	[self.mockTabBarPresenter verify];
}

- (void)testOnChildAppear_shouldInvokePresenterApplyOptionsWithResolvedOptions {
	[[self.mockTabBarPresenter expect] applyOptions:[OCMArg any]];
	[self.mockUut onChildWillAppear];
	[self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokePresenterMergeOptions {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	
	[(RNNTabBarPresenter *)[self.mockTabBarPresenter expect] mergeOptions:options currentOptions:[(RNNTabBarController *)self.mockUut options] defaultOptions:nil];
	[(RNNTabBarController *)self.mockUut mergeOptions:options];
	[self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokeParentMergeOptions {
	id parentMock = [OCMockObject partialMockForObject:[RNNRootViewController new]];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];

	OCMStub([self.mockUut parentViewController]).andReturn(parentMock);
	[((RNNRootViewController *)[parentMock expect]) mergeOptions:options];
	[(RNNTabBarController *)self.mockUut mergeOptions:options];
	[parentMock verify];
}

- (void)testOnChildAppear_shouldInvokeParentOnChildAppear {
	id parentMock = [OCMockObject partialMockForObject:[RNNNavigationController new]];

	OCMStub([self.mockUut parentViewController]).andReturn(parentMock);
	
	[[parentMock expect] onChildWillAppear];
	[self.mockUut onChildWillAppear];
	[parentMock verify];
}

- (void)testGetCurrentChild_shouldInvokeSelectedViewControllerGetCurrentChild {
	[[self.mockChildViewController expect] getCurrentChild];
	[self.mockUut getCurrentChild];
	[self.mockChildViewController verify];
}

- (void)testGetCurrentChild_shouldInvokeOnSelectedViewController {
	[[self.mockChildViewController expect] getCurrentChild];
	[self.mockUut getCurrentChild];
	[self.mockChildViewController verify];
}

- (void)testPreferredStatusBarStyle_shouldInvokeSelectedViewControllerPreferredStatusBarStyle {
	[[self.mockChildViewController expect] preferredStatusBarStyle];
	[self.mockUut preferredStatusBarStyle];
	[self.mockChildViewController verify];
}

- (void)testPreferredStatusBarStyle_shouldInvokeOnSelectedViewController {
	[[self.mockChildViewController expect] preferredStatusBarStyle];
	[self.mockUut preferredStatusBarStyle];
	[self.mockChildViewController verify];
}

- (void)testTabBarControllerDidSelectViewControllerDelegate_shouldInvokeSendBottomTabSelectedEvent {
	NSUInteger selectedIndex = 2;
	OCMStub([self.mockUut selectedIndex]).andReturn(selectedIndex);

	[[self.mockEventEmmiter expect] sendBottomTabSelected:@(selectedIndex) unselected:@(0)];
	[self.mockUut tabBarController:self.mockUut didSelectViewController:[UIViewController new]];
	[self.mockEventEmmiter verify];
}

- (void)testSetSelectedIndexByComponentID_ShouldSetSelectedIndexWithCorrectIndex {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	layoutInfo.componentId = @"componentId";
	
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:nil presenter:nil options:nil defaultOptions:nil];
	
	RNNTabBarController* uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:@[[UIViewController new], vc] options:nil defaultOptions:nil presenter:[RNNTabBarPresenter new]];
	[uut setSelectedIndexByComponentID:@"componentId"];
	XCTAssertTrue(uut.selectedIndex == 1);
}

@end
