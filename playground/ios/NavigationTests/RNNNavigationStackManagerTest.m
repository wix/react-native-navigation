#import <XCTest/XCTest.h>
#import "RNNNavigationStackManager.h"
#import "RNNComponentViewController.h"
#import "RNNStackController.h"

@interface RNNNavigationStackManagerTest : XCTestCase

@property (nonatomic, strong) RNNStackController *nvc;
@property (nonatomic, strong) UIViewController *vc1;
@property (nonatomic, strong) UIViewController *vc2;
@property (nonatomic, strong) UIViewController *vc3;
@property (nonatomic, strong) RNNNavigationStackManager *stackManager;

@end

@implementation RNNNavigationStackManagerTest

- (void)setUp {
    [super setUp];
	
	self.nvc = [[RNNStackController alloc] init];
	self.vc1 = [RNNComponentViewController new];
	self.vc2 = [RNNComponentViewController new];
	self.vc3 = [RNNComponentViewController new];
	self.stackManager = [RNNNavigationStackManager new];
	
	NSArray *vcArray = @[self.vc1, self.vc2, self.vc3];
	[self.nvc setViewControllers:vcArray];
}


- (void)testPop_removeTopVCFromStack {
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	XCTAssertTrue([self.nvc.topViewController isEqual:self.vc3]);
	[_stackManager popTo:self.vc2 animated:YES completion:^(NSArray *poppedViewControllers) {
		XCTAssertTrue([self.nvc.topViewController isEqual:self.vc2]);
		[expectation fulfill];
	} rejection:nil];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testPopToSpecificVC_removeAllPopedVCFromStack {
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	XCTAssertFalse([self.nvc.topViewController isEqual:self.vc1]);
	[_stackManager popTo:self.vc1 animated:NO completion:^(NSArray *poppedViewControllers) {
		XCTAssertTrue([self.nvc.topViewController isEqual:self.vc1]);
		[expectation fulfill];
	} rejection:nil];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testPopToRoot_removeAllTopVCsFromStack {
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	[_stackManager popToRoot:self.vc3 animated:NO completion:^(NSArray *poppedViewControllers) {
		XCTAssertTrue(self.nvc.childViewControllers.count == 1);
		XCTAssertTrue([self.nvc.topViewController isEqual:self.vc1]);
		[expectation fulfill];
	} rejection:nil];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testStackRoot_shouldUpdateNavigationControllerChildrenViewControllers {
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	[_stackManager setStackChildren:@[self.vc2] fromViewController:self.vc1 animated:NO completion:^{
		XCTAssertTrue(self.nvc.childViewControllers.count == 1);
		XCTAssertTrue([self.nvc.topViewController isEqual:self.vc2]);
		[expectation fulfill];
	} rejection:nil];

	[self waitForExpectationsWithTimeout:1 handler:nil];
}

@end
