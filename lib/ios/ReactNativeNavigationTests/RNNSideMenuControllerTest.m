#import <XCTest/XCTest.h>
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"

@interface RNNSideMenuControllerTest : XCTestCase
@property (nonatomic, strong) RNNSideMenuController *uut;
@property (nonatomic, strong) RNNSideMenuChildVC *centerVC;
@property (nonatomic, strong) RNNSideMenuChildVC *leftVC;
@property (nonatomic, strong) RNNSideMenuChildVC *rightVC;
@end

@implementation RNNSideMenuControllerTest

- (void)setUp {
    [super setUp];
	_leftVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil childViewControllers:@[[RNNRootViewController new]] options:nil defaultOptions:nil presenter:nil type:RNNSideMenuChildTypeLeft];
	_rightVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil childViewControllers:@[[RNNRootViewController new]] options:nil defaultOptions:nil presenter:nil type:RNNSideMenuChildTypeRight];
	_centerVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil childViewControllers:@[[RNNRootViewController new]] options:nil defaultOptions:nil presenter:nil type:RNNSideMenuChildTypeCenter];
	self.uut = [[RNNSideMenuController alloc] initWithLayoutInfo:nil childViewControllers:@[_leftVC, _centerVC, _rightVC] options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:nil presenter:nil];
}

- (void)testSetSideMenuWidthShouldUpdateLeftReactViewFrameWidth {
	[self.uut side:MMDrawerSideLeft width:100];
	XCTAssertEqual(self.uut.left.child.view.frame.size.width, 100.f);
}

- (void)testSetSideMenuWidthShouldUpdateRightReactViewFrameWidth {
	[self.uut side:MMDrawerSideRight width:150];
	XCTAssertEqual(self.uut.right.child.view.frame.size.width, 150.f);
}

@end
