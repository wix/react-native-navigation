#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"
#import "RNNViewControllerPresenter.h"
#import "RCTConvert+Modal.h"
#import "RNNTabBarController.h"
#import "RNNNavigationController.h"

@interface UIViewController_LayoutProtocolTest : XCTestCase

@property (nonatomic, retain) UIViewController* uut;

@end

@implementation UIViewController_LayoutProtocolTest

- (void)setUp {
	[super setUp];
	self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
    self.uut.layoutInfo = [[RNNLayoutInfo alloc] init];
    self.uut.layoutInfo.componentId = @"componentId";
}

- (void)testInitWithLayoutApplyDefaultOptions {
    RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
    RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
    RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    defaultOptions.modalPresentationStyle = [[Text alloc] initWithValue:@"fullScreen"];

    UIViewController* uut = [[UIViewController alloc] initWithLayoutInfo:nil creator:nil options:options defaultOptions:defaultOptions presenter:presenter eventEmitter:nil childViewControllers:nil];
    XCTAssertEqual(uut.modalPresentationStyle, [RCTConvert UIModalPresentationStyle:@"fullScreen"]);
}

- (void)testInitWithLayoutInfoShouldSetChildViewControllers {
	UIViewController* child1 = [UIViewController new];
	UIViewController* child2 = [UIViewController new];
	NSArray* childViewControllers = @[child1, child2];
	UINavigationController* uut = [[UINavigationController alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:nil eventEmitter:nil childViewControllers:childViewControllers];
	
	XCTAssertEqual(uut.viewControllers[0], child1);
	XCTAssertEqual(uut.viewControllers[1], child2);
}

- (void)testSetBackButtonIcon_withColor_shouldSetColor {
	UIViewController* uut = [UIViewController new];
	[[UINavigationController alloc] initWithRootViewController:uut];
	UIColor* color = [UIColor blackColor];
	
	[uut rnn_setBackButtonIcon:nil withColor:color title:nil];
	XCTAssertEqual(color, uut.navigationItem.backBarButtonItem.tintColor);
}

- (void)testSetBackButtonIcon_withColor_shouldSetTitle {
	UIViewController* uut = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:uut];
	NSString* title = @"Title";
	
	[uut rnn_setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, uut.navigationItem.backBarButtonItem.title);
}

- (void)testSetBackButtonIcon_withColor_shouldSetIcon {
	UIViewController* uut = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:uut];
	UIImage* icon = [UIImage new];
	
	[uut rnn_setBackButtonIcon:icon withColor:nil title:nil];
	XCTAssertEqual(icon, uut.navigationItem.backBarButtonItem.image);
}

- (void)testSetBackButtonIcon_shouldSetTitleOnPreviousViewControllerIfExists {
	UIViewController* uut = [UIViewController new];
	UIViewController* viewController2 = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] init];
	[nav setViewControllers:@[uut, viewController2]];
	NSString* title = @"Title";
	
	[uut rnn_setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, uut.navigationItem.backBarButtonItem.title);
}

- (void)testResolveOptions {
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];

	RNNNavigationOptions* childOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	RNNNavigationOptions* parentOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
		parentOptions.bottomTab.text =  [[Text alloc] initWithValue:@"text"];
		parentOptions.bottomTab.selectedIconColor =  [[Color alloc] initWithValue:UIColor.redColor];
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
		defaultOptions.bottomTab.text = [[Text alloc] initWithValue:@"default text"];
		defaultOptions.bottomTab.selectedIconColor = [[Color alloc] initWithValue:UIColor.blueColor];

	UIViewController* child = [[UIViewController alloc] initWithLayoutInfo:nil creator:nil options:childOptions defaultOptions:defaultOptions presenter:presenter eventEmitter:nil childViewControllers:nil];
    RNNNavigationController* parent = [[RNNNavigationController alloc] initWithLayoutInfo:nil creator:nil options:parentOptions defaultOptions:defaultOptions presenter:presenter eventEmitter:nil childViewControllers:@[child]];

    XCTAssertEqual([parent getCurrentChild], child);
	XCTAssertEqual([[parent resolveOptions].bottomTab.text get], @"text");
	XCTAssertEqual([[parent resolveOptions].bottomTab.selectedIconColor get], UIColor.redColor);
}

@end
