#import "RNNModalHostViewManagerHandler.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNModalHostViewManagerHandlerTest : XCTestCase
@end

@implementation RNNModalHostViewManagerHandlerTest {
    RNNModalHostViewManagerHandler *_uut;
    RNNModalManager *_modalManager;
    RCTModalHostViewManager *_modalHostViewManager;
}

- (void)setUp {
    _modalManager = [OCMockObject partialMockForObject:[[RNNModalManager alloc] init]];
    _uut = [[RNNModalHostViewManagerHandler alloc] initWithModalManager:_modalManager];
    _modalHostViewManager = [RCTModalHostViewManager new];
    [_uut connectModalHostViewManager:_modalHostViewManager];
}

- (void)testPresentationBlock_shouldNotPresentModalTwice {
    [[(id)_modalManager reject] showModal:OCMArg.any animated:NO completion:OCMArg.any];
    _modalHostViewManager.presentationBlock(nil, _modalManager.topPresentedVC, NO, nil);
}

- (void)testPresentationBlock_shouldShowModal {
    UIViewController *vc = UIViewController.new;
    _modalHostViewManager.presentationBlock(nil, vc, NO, nil);
    XCTAssertEqual(vc, _modalManager.topPresentedVC);
}

- (void)testPresentationBlock_shouldShowAndDismissModal {
    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
    UIViewController *vc = UIViewController.new;
    _modalHostViewManager.presentationBlock(nil, vc, NO, nil);
    XCTAssertEqual(vc, _modalManager.topPresentedVC);
    _modalHostViewManager.dismissalBlock(nil, vc, NO, ^{
      XCTAssertNotEqual(vc, self->_modalManager.topPresentedVC);
      [expectation fulfill];
    });
    [self waitForExpectationsWithTimeout:5 handler:nil];
}

@end
