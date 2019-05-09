#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNNavigationController.h"
#import "RNNRootViewController.h"
#import "RNNTestRootViewCreator.h"

@interface RNNNavigationControllerTest : XCTestCase

@property (nonatomic, strong) RNNNavigationController *uut;

@end

@implementation RNNNavigationControllerTest {
	RNNRootViewController* _vc1;
	id _vc2Mock;
	RNNRootViewController* _vc2;
	UIViewController* _vc3;
	RNNNavigationOptions* _options;
	RNNTestRootViewCreator* _creator;
}

- (void)setUp {
    [super setUp];
	_creator = [[RNNTestRootViewCreator alloc] init];
	_vc1 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[OCMockObject partialMockForObject:[[RNNViewControllerPresenter alloc] init]] options:[[RNNNavigationOptions alloc] initEmptyOptions]  defaultOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
	_vc2 = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[[RNNViewControllerPresenter alloc] init] options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
	_vc2Mock = [OCMockObject partialMockForObject:_vc2];
	_vc3 = [UIViewController new];
	_options = [OCMockObject partialMockForObject:[[RNNNavigationOptions alloc] initEmptyOptions]];
	self.uut = [[RNNNavigationController alloc] initWithLayoutInfo:nil creator:_creator options:_options defaultOptions:nil presenter:[OCMockObject partialMockForObject:[[RNNNavigationControllerPresenter alloc] init]] eventEmitter:nil childViewControllers:@[_vc1, _vc2]];
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
	XCTAssertNotNil(self.uut.presenter);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
	self.uut = [[RNNNavigationController alloc] initWithLayoutInfo:nil creator:_creator options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNViewControllerPresenter alloc] init] eventEmitter:nil childViewControllers:@[_vc1, _vc2]];
	XCTAssertTrue(self.uut.viewControllers.count == 2);
}

- (void)testChildViewControllerForStatusBarStyle_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.childViewControllerForStatusBarStyle == self.uut.topViewController);
}

- (void)testCurrentChild_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.getCurrentChild == self.uut.topViewController);
}

- (void)testGetLeafViewController_shouldReturnTopViewController {
	XCTAssertTrue(self.uut.getCurrentChild == self.uut.topViewController);
}

- (void)testPreferredStatusBarStyle_shouldReturnLeafPreferredStatusBarStyle {
	self.uut.getCurrentChild.resolveOptions.statusBar.style = [[Text alloc] initWithValue:@"light"];
	XCTAssertTrue(self.uut.preferredStatusBarStyle == self.uut.getCurrentChild.preferredStatusBarStyle);
}

- (void)testPopGestureEnabled_false {
	NSNumber* popGestureEnabled = @(0);
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.popGesture = [[Bool alloc] initWithValue:popGestureEnabled];
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testPopGestureEnabled_true {
	NSNumber* popGestureEnabled = @(1);
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.popGesture = [[Bool alloc] initWithValue:popGestureEnabled];
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut onChildWillAppear];
	
	XCTAssertTrue(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testRootBackgroundImage {
	UIImage* rootBackgroundImage = [[UIImage alloc] init];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.rootBackgroundImage = [[Image alloc] initWithValue:rootBackgroundImage];
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut onChildWillAppear];
	
	XCTAssertTrue([[(UIImageView*)self.uut.view.subviews[0] image] isEqual:rootBackgroundImage]);
}

- (void)testTopBarBackgroundClipToBounds_true {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.topBar.background.clipToBounds = [[Bool alloc] initWithValue:@(1)];
	
	self.uut = [self createNavigationControllerWithOptions:options];
	[self.uut onChildWillAppear];
	
	XCTAssertTrue(self.uut.navigationBar.clipsToBounds);
}

- (void)testTopBarBackgroundClipToBounds_false {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
	options.topBar.background.clipToBounds = [[Bool alloc] initWithValue:@(0)];
	
	self.uut = [self createNavigationControllerWithOptions:options];
	
	XCTAssertFalse(self.uut.navigationController.navigationBar.clipsToBounds);
}

- (void)testSupportedOrientationsShouldReturnCurrentChildSupportedOrientations {
	XCTAssertEqual(self.uut.supportedInterfaceOrientations, self.uut.getCurrentChild.supportedInterfaceOrientations);
}

- (void)testPopViewControllerReturnLastChildViewController {
	RNNNavigationController* uut = [RNNNavigationController new];
	[uut setViewControllers:@[_vc1, _vc2]];
	XCTAssertEqual([uut popViewControllerAnimated:NO], _vc2);
}

- (void)testPopViewControllerSetTopBarBackgroundForPoppingViewController {
	_options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];
	[_vc1 overrideOptions:_options];
	
	[self.uut popViewControllerAnimated:NO];
	XCTAssertEqual(_vc1.resolveOptions.topBar.background.color.get, self.uut.navigationBar.barTintColor);
}

- (void)testPopViewControllerSetDefaultTopBarBackgroundForPoppingViewController {
	_options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];
	[_vc1 setDefaultOptions:_options];

	[self.uut popViewControllerAnimated:NO];
	XCTAssertEqual(_vc1.resolveOptions.topBar.background.color.get, self.uut.navigationBar.barTintColor);
}

- (void)testPopViewControllerShouldInvokeApplyOptionsBeforePoppingForDestinationViewController {
	RNNNavigationController* uut = [RNNNavigationController new];
	[uut setViewControllers:@[_vc1, _vc2]];
	
	[[(id)uut.presenter expect] applyOptionsBeforePopping:[OCMArg any]];
	
	[uut popViewControllerAnimated:NO];
	
	[(id)uut.presenter verify];
}

- (void)testOverrideOptionsShouldOverrideOptionsState {
	RNNNavigationOptions* overrideOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	[(RNNNavigationOptions*)[(id)self.uut.options expect] overrideOptions:overrideOptions];
	[self.uut overrideOptions:overrideOptions];
	[(id)self.uut.options verify];
}

- (RNNNavigationController *)createNavigationControllerWithOptions:(RNNNavigationOptions *)options {
	RNNNavigationController* nav = [[RNNNavigationController alloc] initWithLayoutInfo:nil creator:_creator options:options defaultOptions:nil presenter:[[RNNNavigationControllerPresenter alloc] init] eventEmitter:nil childViewControllers:@[_vc1]];
	return nav;
}

@end
