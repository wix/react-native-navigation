#import <XCTest/XCTest.h>
#import "RNNNavigationController.h"
#import "RNNRootViewController.h"

@interface RNNNavigationControllerTest : XCTestCase

@property (nonatomic, strong) RNNNavigationController *uut;

@end

@implementation RNNNavigationControllerTest {
	RNNRootViewController* _vc1;
	RNNRootViewController* _vc2;
	UIViewController* _vc3;
	RNNViewControllerPresenter* _presenter;
}

- (void)setUp {
    [super setUp];
	
	_presenter = [[RNNViewControllerPresenter alloc] initWithOptions:[[RNNNavigationOptions alloc] initWithDict:@{}]];
	self.uut = [[RNNNavigationController alloc] init];
	_vc1 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil isExternalComponent:NO presenter:_presenter];
	_vc2 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil isExternalComponent:NO presenter:_presenter];
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
	self.uut.getLeafViewController.presenter.options.statusBar.style = @"light";
	XCTAssertTrue(self.uut.preferredStatusBarStyle == self.uut.getLeafViewController.preferredStatusBarStyle);
}


@end
