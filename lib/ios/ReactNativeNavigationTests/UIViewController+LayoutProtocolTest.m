#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"
#import "RNNComponentPresenter.h"
#import "RCTConvert+Modal.h"
#import "RNNBottomTabsController.h"
#import "RNNStackController.h"

@interface UIViewController_LayoutProtocolTest : XCTestCase

@property (nonatomic, retain) UIViewController * uut;

@end

@implementation UIViewController_LayoutProtocolTest

- (void)setUp {
	[super setUp];
	self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
    _uut.layoutInfo = [[RNNLayoutInfo alloc] init];
    _uut.layoutInfo.componentId = @"componentId";
}

- (void)testInitWithLayoutApplyDefaultOptions {
    RNNComponentPresenter* presenter = [[RNNComponentPresenter alloc] init];
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

    [uut setBackButtonIcon:nil withColor:color title:nil];
	XCTAssertEqual(color, uut.navigationItem.backBarButtonItem.tintColor);
}

- (void)testSetBackButtonIcon_withColor_shouldSetTitle {
	UIViewController* uut = [UIViewController new];
    UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:uut];
    NSString* title = @"Title";

    [uut setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, uut.navigationItem.backBarButtonItem.title);
}

- (void)testSetBackButtonIcon_withColor_shouldSetIcon {
	UIViewController* uut = [UIViewController new];
    UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:uut];
    UIImage* icon = [UIImage new];

    [uut setBackButtonIcon:icon withColor:nil title:nil];
	XCTAssertEqual(icon, uut.navigationItem.backBarButtonItem.image);
}

- (void)testSetBackButtonIcon_shouldSetTitleOnPreviousViewControllerIfExists {
	UIViewController* uut = [UIViewController new];
	UIViewController* viewController2 = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] init];
	[nav setViewControllers:@[uut, viewController2]];
	NSString* title = @"Title";

    [uut setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, uut.navigationItem.backBarButtonItem.title);
}

- (void)testResolveOptions {
	RNNComponentPresenter* presenter = [[RNNComponentPresenter alloc] init];

	RNNNavigationOptions* childOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	RNNNavigationOptions* parentOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
		parentOptions.bottomTab.text =  [[Text alloc] initWithValue:@"text"];
		parentOptions.bottomTab.selectedIconColor =  [[Color alloc] initWithValue:UIColor.redColor];
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
		defaultOptions.bottomTab.text = [[Text alloc] initWithValue:@"default text"];
		defaultOptions.bottomTab.selectedIconColor = [[Color alloc] initWithValue:UIColor.blueColor];

	UIViewController* child = [[UIViewController alloc] initWithLayoutInfo:nil creator:nil options:childOptions defaultOptions:defaultOptions presenter:presenter eventEmitter:nil childViewControllers:nil];
    RNNStackController* parent = [[RNNStackController alloc] initWithLayoutInfo:nil creator:nil options:parentOptions defaultOptions:defaultOptions presenter:presenter eventEmitter:nil childViewControllers:@[child]];

    XCTAssertEqual([parent getCurrentChild], child);
	XCTAssertEqual([[parent resolveOptions].bottomTab.text get], @"text");
	XCTAssertEqual([[parent resolveOptions].bottomTab.selectedIconColor get], UIColor.redColor);
}

- (void)testMergeOptions_invokedOnParentViewController {
    id parent = [OCMockObject partialMockForObject:[RNNStackController new]];
    RNNNavigationOptions * toMerge = [[RNNNavigationOptions alloc] initEmptyOptions];
    [(UIViewController *) [parent expect] mergeChildOptions:toMerge];

    RNNStackController* uut = [[RNNStackController alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:nil eventEmitter:nil childViewControllers:nil];
    [parent addChildViewController:uut];

    [uut mergeOptions:toMerge];
    [parent verify];
}

- (void)testMergeOptions_presenterIsInvokedWithResolvedOptions {
    id parent = [OCMockObject partialMockForObject:[RNNStackController new]];
    id presenter = [OCMockObject partialMockForObject:[RNNStackPresenter new]];
    RNNNavigationOptions * toMerge = [[RNNNavigationOptions alloc] initEmptyOptions];
    toMerge.topBar.title.color = [[Color alloc] initWithValue:[UIColor redColor]];

    [[presenter expect] mergeOptions:toMerge resolvedOptions:[OCMArg checkWithBlock:^(id value) {
        RNNNavigationOptions *options = (RNNNavigationOptions *) value;
        XCTAssertEqual([options.topBar.title.text get], @"Initial title");
        XCTAssertEqual([options.bottomTab.text get], @"Child tab text");
        return YES;
    }]];

    RNNNavigationOptions * childOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    childOptions.bottomTab.text = [[Text alloc] initWithValue:@"Child tab text"];
    UIViewController* child = [[UIViewController alloc] initWithLayoutInfo:nil creator:nil options:childOptions defaultOptions:nil presenter:presenter eventEmitter:nil childViewControllers:nil];
    RNNNavigationOptions * initialOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"Initial title"];
    RNNStackController* uut = [[RNNStackController alloc] initWithLayoutInfo:nil creator:nil options:initialOptions defaultOptions:nil presenter:presenter eventEmitter:nil childViewControllers:@[child]];
    [parent addChildViewController:uut];

	[uut mergeOptions:toMerge];
    [presenter verify];
}

- (void)testMergeOptions_mergedIntoCurrentOptions {
	UIViewController* uut = [[UIViewController alloc] initWithLayoutInfo:nil creator:nil options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:nil presenter:nil eventEmitter:nil childViewControllers:nil];
	RNNNavigationOptions * toMerge = [[RNNNavigationOptions alloc] initEmptyOptions];
	toMerge.topBar.title.text = [[Text alloc] initWithValue:@"merged"];

	[uut mergeOptions:toMerge];
	XCTAssertEqual(uut.resolveOptions.topBar.title.text.get, @"merged");
}

@end
