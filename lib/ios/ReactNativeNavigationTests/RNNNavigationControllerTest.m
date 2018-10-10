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
}

- (void)setUp {
    [super setUp];
	
	_vc1 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[[RNNViewControllerPresenter alloc] init] options:nil];
	_vc2 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[[RNNViewControllerPresenter alloc] init] options:nil];
	_vc3 = [UIViewController new];
	
	self.uut = [[RNNNavigationController alloc] initWithLayoutInfo:nil childViewControllers:@[_vc1] options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:[[RNNViewControllerPresenter alloc] init]];
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
	XCTAssertNotNil(self.uut.presenter);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
	self.uut = [[RNNNavigationController alloc] initWithLayoutInfo:nil childViewControllers:@[_vc1, _vc2] options:[[RNNNavigationOptions alloc] initWithDict:@{}] presenter:[[RNNViewControllerPresenter alloc] init]];
	XCTAssertTrue(self.uut.viewControllers.count == 2);
}

- (void)testChildViewControllerForStatusBarStyle_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.childViewControllerForStatusBarStyle == self.uut.topViewController);
}

- (void)testGetLeafViewController_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.getLeafViewController == self.uut.topViewController);
}

- (void)testPreferredStatusBarStyle_shouldReturnLeafPreferredStatusBarStyle {
	self.uut.getLeafViewController.options.statusBar.style = @"light";
	XCTAssertTrue(self.uut.preferredStatusBarStyle == self.uut.getLeafViewController.preferredStatusBarStyle);
}

- (void)testPopGestureEnabled_false {
	NSNumber* popGestureEnabled = @(0);
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.popGesture = popGestureEnabled;
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testPopGestureEnabled_true {
	NSNumber* popGestureEnabled = @(1);
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.popGesture = popGestureEnabled;
	
	self.uut = [self createNavigationControllerWithOptions:options];
	XCTAssertTrue(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testRootBackgroundImage {
	UIImage* rootBackgroundImage = [[UIImage alloc] init];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.rootBackgroundImage = rootBackgroundImage;
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut viewWillAppear:false];
	
	XCTAssertTrue([[(UIImageView*)self.uut.view.subviews[0] image] isEqual:rootBackgroundImage]);
}

- (void)testTopBarBackgroundClipToBounds_true {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.topBar.background.clipToBounds = @(1);
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut viewWillAppear:false];
	
	XCTAssertTrue(self.uut.navigationBar.clipsToBounds);
}

- (void)testTopBarBackgroundClipToBounds_false {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.topBar.background.clipToBounds = @(0);
	
	self.uut = [self createNavigationControllerWithOptions:options];
	
	XCTAssertFalse(self.uut.navigationController.navigationBar.clipsToBounds);
}

- (RNNNavigationController *)createNavigationControllerWithOptions:(RNNNavigationOptions *)options {
	return [[RNNNavigationController alloc] initWithLayoutInfo:nil childViewControllers:@[_vc1] options:options presenter:[[RNNViewControllerPresenter alloc] init]];
}

@end
