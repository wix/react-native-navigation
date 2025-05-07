#import "RNNComponentViewController+Utils.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNModalManagerEventHandler.h>
#import <ReactNativeNavigation/RNNStackController.h>
#import <XCTest/XCTest.h>

#import <ReactNativeNavigation/RNNTurboEventEmitter.h>

@interface RNNModalManagerEventHandlerTest : XCTestCase

@end

@implementation RNNModalManagerEventHandlerTest {
    RNNModalManagerEventHandler *_uut;
    id _eventEmitter;
}

- (void)setUp {
#ifdef RCT_NEW_ARCH_ENABLED
	_eventEmitter = [OCMockObject mockForClass:RNNTurboEventEmitter.class];
#else
	_eventEmitter = [OCMockObject mockForClass:RNNEventEmitter.class];
#endif
    _uut = [[RNNModalManagerEventHandler alloc] initWithEventEmitter:_eventEmitter];
}

- (void)testDismissedModal_shouldEmitEventWithTopMostComponentId {
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";
    RNNComponentViewController *child = [OCMockObject
        partialMockForObject:[RNNComponentViewController createWithComponentId:@"child"]];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    [[_eventEmitter expect] sendModalsDismissedEvent:@"stack" numberOfModalsDismissed:@(1)];
    [_uut dismissedModal:child];
    [_eventEmitter verify];
}

- (void)testAttemptToDismissModal_shouldEmitEventWithTopMostComponentId {
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";
    RNNComponentViewController *child = [OCMockObject
        partialMockForObject:[RNNComponentViewController createWithComponentId:@"child"]];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    [[_eventEmitter expect] sendModalAttemptedToDismissEvent:@"stack"];
    [_uut attemptedToDismissModal:child];
    [_eventEmitter verify];
}

- (void)testDismissedMultipleModals_shouldEmitEventWithTopMostComponentId {
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";
    RNNComponentViewController *child = [OCMockObject
        partialMockForObject:[RNNComponentViewController createWithComponentId:@"child"]];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    [[_eventEmitter expect] sendModalsDismissedEvent:@"stack" numberOfModalsDismissed:@(2)];
    [_uut dismissedMultipleModals:@[ [UIViewController new], child ]];
    [_eventEmitter verify];
}

@end
