#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "UIViewController+LayoutProtocol.h"
#import "RNNViewControllerPresenter.h"
#import "RCTConvert+Modal.h"

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


@end
