#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNStackController.h"
#import "RNNComponentViewController.h"
#import "RNNTestRootViewCreator.h"

@interface RNNNavigationControllerTest : XCTestCase

@property (nonatomic, strong) RNNStackController *uut;

@end

@implementation RNNNavigationControllerTest {
	RNNComponentViewController* _vc1;
	id _vc2Mock;
	RNNComponentViewController* _vc2;
	UIViewController* _vc3;
	RNNNavigationOptions* _options;
	RNNTestRootViewCreator* _creator;
}

- (void)setUp {
    [super setUp];
	_creator = [[RNNTestRootViewCreator alloc] init];
	_vc1 = [[RNNComponentViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[OCMockObject partialMockForObject:[[RNNComponentPresenter alloc] init]] options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
	_vc2 = [[RNNComponentViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[[RNNComponentPresenter alloc] init] options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
	_vc2Mock = [OCMockObject partialMockForObject:_vc2];
	_vc3 = [UIViewController new];
	_options = [OCMockObject partialMockForObject:[[RNNNavigationOptions alloc] initEmptyOptions]];
	self.uut = [[RNNStackController alloc] initWithLayoutInfo:nil creator:_creator options:_options defaultOptions:nil presenter:[OCMockObject partialMockForObject:[[RNNStackPresenter alloc] init]] eventEmitter:nil childViewControllers:@[_vc1, _vc2]];
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
	XCTAssertNotNil(self.uut.presenter);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
	self.uut = [[RNNStackController alloc] initWithLayoutInfo:nil creator:_creator options:[[RNNNavigationOptions alloc] initWithDict:@{}] defaultOptions:nil presenter:[[RNNComponentPresenter alloc] init] eventEmitter:nil childViewControllers:@[_vc1, _vc2]];
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
	RNNStackController* uut = [RNNStackController new];
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
	RNNStackController* uut = [RNNStackController new];
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

- (void)testSetTopBarBackgroundColor_ShouldSetBackgroundColor {
	UIColor* color = UIColor.redColor;
	[self.uut setTopBarBackgroundColor:color];
	XCTAssertEqual(self.uut.navigationBar.barTintColor, color);
}

- (void)testSetTopBarBackgroundColor_ShouldSetTransparentBackgroundColor {
	UIColor* transparentColor = UIColor.clearColor;
	[self.uut setTopBarBackgroundColor:transparentColor];

	XCTAssertEqual(self.uut.navigationBar.backgroundColor, transparentColor);
	XCTAssertTrue(self.uut.navigationBar.translucent);
	XCTAssertNotNil(self.uut.navigationBar.shadowImage);
	XCTAssertNotNil([self.uut.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault]);
}

- (void)testSetTopBarBackgroundColor_ShouldRemoveTransparentView {
	UIColor* transparentColor = UIColor.clearColor;
	UIColor* redColor = UIColor.redColor;
	
	[self.uut setTopBarBackgroundColor:transparentColor];
	XCTAssertNotNil([self.uut.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]);
	[self.uut setTopBarBackgroundColor:redColor];
	XCTAssertNil([self.uut.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]);
}

- (void)testSetTopBarBackgroundColor_NilColorShouldResetNavigationBar {
	UIColor* transparentColor = UIColor.clearColor;
	UIColor* redColor = UIColor.redColor;
	
	[self.uut setTopBarBackgroundColor:transparentColor];
	[self.uut setTopBarBackgroundColor:redColor];
	[self.uut setTopBarBackgroundColor:nil];
	
	XCTAssertNil([self.uut.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]);
	XCTAssertNil(self.uut.navigationBar.barTintColor);
}

- (RNNStackController *)createNavigationControllerWithOptions:(RNNNavigationOptions *)options {
	RNNStackController* nav = [[RNNStackController alloc] initWithLayoutInfo:nil creator:_creator options:options defaultOptions:nil presenter:[[RNNStackPresenter alloc] init] eventEmitter:nil childViewControllers:@[_vc1]];
	return nav;
}

@end
