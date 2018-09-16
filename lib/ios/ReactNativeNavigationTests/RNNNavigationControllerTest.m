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
