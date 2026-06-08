#import "RNNScreenTransition.h"
#import "RNNUtils.h"
#import <XCTest/XCTest.h>

@interface RNNScreenTransitionTest : XCTestCase

@end

@implementation RNNScreenTransitionTest

- (void)testParsesZoomFromId {
    RNNScreenTransition *transition =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-1"}}];

    XCTAssertEqualObjects(transition.zoomFromId.get, @"thumb-1");
    XCTAssertTrue(transition.hasZoomTransition);
}

- (void)testZoomEnabledDefaultsToTrue {
    RNNScreenTransition *transition =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-1"}}];

    XCTAssertTrue(transition.hasZoomTransition);
}

- (void)testZoomEnabledFalseDisablesZoomTransition {
    RNNScreenTransition *transition = [[RNNScreenTransition alloc]
        initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-1", @"enabled" : @NO}}];

    XCTAssertFalse(transition.hasZoomTransition);
}

- (void)testZoomTransitionRequiresNonEmptyFromId {
    RNNScreenTransition *missingFromId =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{}}];
    RNNScreenTransition *emptyFromId =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @""}}];

    XCTAssertFalse(missingFromId.hasZoomTransition);
    XCTAssertFalse(emptyFromId.hasZoomTransition);
}

- (void)testCustomContentAnimationTakesPrecedenceOverZoomTransition {
    RNNScreenTransition *transition = [[RNNScreenTransition alloc] initWithDict:@{
        @"zoom" : @{@"fromId" : @"thumb-1"},
        @"content" : @{@"enter" : @{@"alpha" : @{@"from" : @0, @"to" : @1}}}
    }];

    XCTAssertTrue(transition.hasCustomAnimation);
    XCTAssertFalse(transition.hasZoomTransition);
}

- (void)testSharedElementTransitionTakesPrecedenceOverZoomTransition {
    RNNScreenTransition *transition = [[RNNScreenTransition alloc] initWithDict:@{
        @"zoom" : @{@"fromId" : @"thumb-1"},
        @"sharedElementTransitions" : @[ @{@"fromId" : @"image-1", @"toId" : @"image-2"} ]
    }];

    XCTAssertTrue(transition.hasCustomAnimation);
    XCTAssertFalse(transition.hasZoomTransition);
}

- (void)testMergeOptionsUpdatesZoomFromIdAndEnabled {
    RNNScreenTransition *transition =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-1"}}];
    RNNScreenTransition *updatedFromId =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-2"}}];
    RNNScreenTransition *disabled =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"enabled" : @NO}}];

    [transition mergeOptions:updatedFromId];
    XCTAssertEqualObjects(transition.zoomFromId.get, @"thumb-2");
    XCTAssertTrue(transition.hasZoomTransition);

    [transition mergeOptions:disabled];
    XCTAssertFalse(transition.hasZoomTransition);
}

- (void)testShouldWaitForRenderUsesDefaultForZoomOnlyAndCustomAnimationsWait {
    RNNScreenTransition *zoomOnly =
        [[RNNScreenTransition alloc] initWithDict:@{@"zoom" : @{@"fromId" : @"thumb-1"}}];
    RNNScreenTransition *customAnimation = [[RNNScreenTransition alloc]
        initWithDict:@{@"content" : @{@"enter" : @{@"alpha" : @{@"from" : @0, @"to" : @1}}}}];

    XCTAssertEqual(zoomOnly.shouldWaitForRender, [RNNUtils getDefaultWaitForRender]);
    XCTAssertTrue(customAnimation.shouldWaitForRender);
}

@end
