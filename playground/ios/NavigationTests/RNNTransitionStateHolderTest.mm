#import "RNNTransitionStateHolder.h"
#import <XCTest/XCTest.h>

@interface RNNTransitionStateHolderTest : XCTestCase

@end

@implementation RNNTransitionStateHolderTest

- (void)testDefaultObjectProperties {
    RNNTransitionStateHolder *defaultTransitionObject =
        [[RNNTransitionStateHolder alloc] initWithDict:@{@"fromId" : @"hello"}];
    XCTAssertEqual(defaultTransitionObject.springDamping, 0.85);
    XCTAssertEqual(defaultTransitionObject.springVelocity, 0.8);
    XCTAssertEqual(defaultTransitionObject.startDelay, 0);
    XCTAssertEqual(defaultTransitionObject.startAlpha, 1);
    XCTAssertEqual(defaultTransitionObject.endAlpha, 1);
    XCTAssertEqual(defaultTransitionObject.startX, 0);
    XCTAssertEqual(defaultTransitionObject.startY, 0);
    XCTAssertEqual(defaultTransitionObject.endX, 0);
    XCTAssertEqual(defaultTransitionObject.endY, 0);
    XCTAssertEqual(defaultTransitionObject.duration, 1);
    XCTAssertEqual(defaultTransitionObject.interactivePop, false);
}

@end
