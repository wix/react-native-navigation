#import <XCTest/XCTest.h>
#import "RNNTabBarController.h"
#import "RNNNavigationOptions.h"
#import "RNNTabBarPresenter.h"
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
	id presenterMock = OCMClassMock([RNNTabBarPresenter class]);
	self.uut = [[RNNTabBarController alloc] initWithLayoutInfo:nil childViewControllers:nil options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:presenterMock];
	[self.uut willMoveToParentViewController:[UIViewController new]];
	
	
}



@end
