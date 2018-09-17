#import <XCTest/XCTest.h>
#import "RNNNavigationController.h"

@interface RNNNavigationControllerTest : XCTestCase

@property (nonatomic, strong) RNNNavigationController *uut;

@end

@implementation RNNNavigationControllerTest {
	RNNRootViewController* _vc1;
	RNNRootViewController* _vc2;
	UIViewController* _vc3;
}

- (void)setUp {
    [super setUp];
	
	self.uut = [[RNNNavigationController alloc] initWithLayoutInfo:[self createLayoutInfo]];
	_vc1 = [[RNNRootViewController alloc] init];
	_vc1.layoutInfo = [self createLayoutInfo];
	_vc2 = [[RNNRootViewController alloc] init];
	_vc2.layoutInfo = [self createLayoutInfo];
	_vc3 = [UIViewController new];
}

- (void)testSetViewControllers_shouldPropogateOptionsToViewController {
	self.uut.layoutInfo.options.topBar.visible = @(1);
	[self.uut setViewControllers:@[_vc1]];
	XCTAssertTrue(_vc1.layoutInfo.options.topBar.visible.boolValue == self.uut.layoutInfo.options.topBar.visible.boolValue);
}

- (void)testSetViewControllers_externalViewControllerShouldNotCrash {
	NSArray* viewControllers = @[_vc1, _vc3];
	XCTAssertNoThrow([self.uut setViewControllers:viewControllers]);
}

- (void)testPushViewController_shouldPropogateOptionsToViewController {
	self.uut.layoutInfo.options.topBar.visible = @(1);
	[self.uut pushViewController:_vc2 animated:NO];
	XCTAssertTrue(_vc2.layoutInfo.options.topBar.visible.boolValue == self.uut.layoutInfo.options.topBar.visible.boolValue);
}

- (void)testPushViewController_externalViewControllerShouldNotCrash {
	XCTAssertNoThrow([self.uut pushViewController:_vc3 animated:NO]);
}

- (void)testChildViewControllerForStatusBarStyle_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.childViewControllerForStatusBarStyle == self.uut.topViewController);
}

- (void)testGetLeafViewController_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.getLeafViewController == self.uut.topViewController);
}

- (void)testPreferredStatusBarStyle_shouldReturnLeafPreferredStatusBarStyle {
	[self.uut setViewControllers:@[_vc1]];
	self.uut.getLeafViewController.layoutInfo.options.statusBar.style = @"dark";
	XCTAssertTrue(self.uut.preferredStatusBarStyle == self.uut.getLeafViewController.preferredStatusBarStyle);
}

- (RNNLayoutInfo *)createLayoutInfo {
	RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
	layoutInfo.options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	return layoutInfo;
}

@end
