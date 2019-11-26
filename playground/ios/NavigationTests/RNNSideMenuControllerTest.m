#import <XCTest/XCTest.h>
#import "RNNSideMenuController.h"
#import "RNNComponentViewController.h"

@interface RNNSideMenuControllerTest : XCTestCase
@property (nonatomic, strong) RNNSideMenuController *uut;
@property (nonatomic, strong) RNNSideMenuChildVC *centerVC;
@property (nonatomic, strong) RNNSideMenuChildVC *leftVC;
@property (nonatomic, strong) RNNSideMenuChildVC *rightVC;
@end

@implementation RNNSideMenuControllerTest

- (void)setUp {
    [super setUp];
	_leftVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:nil eventEmitter:nil childViewController:[RNNComponentViewController new] type:RNNSideMenuChildTypeLeft];
	_rightVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:nil eventEmitter:nil childViewController:[RNNComponentViewController new] type:RNNSideMenuChildTypeRight];
	_centerVC =[[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil creator:nil options:nil defaultOptions:nil presenter:nil eventEmitter:nil childViewController:[RNNComponentViewController new] type:RNNSideMenuChildTypeCenter];
	self.uut = [[RNNSideMenuController alloc] initWithLayoutInfo:nil creator:nil childViewControllers:@[_leftVC, _centerVC, _rightVC] options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:nil presenter:nil eventEmitter:nil];
}

- (void)testSetSideMenuWidthShouldUpdateLeftReactViewFrameWidth {
	[self.uut side:MMDrawerSideLeft width:100];
	XCTAssertEqual(self.uut.left.child.view.frame.size.width, 100.f);
}

- (void)testSetSideMenuWidthShouldUpdateRightReactViewFrameWidth {
	[self.uut side:MMDrawerSideRight width:150];
	XCTAssertEqual(self.uut.right.child.view.frame.size.width, 150.f);
}

- (void)testGetCurrentChild {
    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	XCTAssertEqual(_uut.getCurrentChild, _centerVC);

	[_uut openDrawerSide:MMDrawerSideLeft animated:NO completion:(void (^)(BOOL)) ^{
        XCTAssertEqual(_uut.getCurrentChild, _leftVC);
	}];

    [_uut closeDrawerAnimated:NO completion:nil];
    [_uut openDrawerSide:MMDrawerSideRight animated:NO completion:(void (^)(BOOL)) ^{
        XCTAssertEqual(_uut.getCurrentChild, _rightVC);
        [expectation fulfill];
    }];

    [self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testResolveOptions {
    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];

    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initEmptyOptions];
    options.sideMenu.left.visible = [[Bool alloc] initWithBOOL:YES];
    [_centerVC overrideOptions:options];

    XCTAssertTrue(_uut.resolveOptions.sideMenu.left.visible);

    [_uut openDrawerSide:MMDrawerSideLeft animated:NO completion:^(BOOL finished) {
        XCTAssertTrue(_uut.resolveOptions.sideMenu.left.visible);
        [expectation fulfill];
    }];

    [self waitForExpectationsWithTimeout:1 handler:nil];
}

@end
