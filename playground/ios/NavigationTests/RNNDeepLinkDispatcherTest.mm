#import "RNNDeepLinkDispatcher.h"
#import "RNNReactNativeVersion.h"
#import <OCMock/OCMock.h>
#import <React/RCTLinkingManager.h>
#import <React/RCTRootView.h>
#import <React/RCTUtils.h>
#import <XCTest/XCTest.h>

@interface RNNDeepLinkDispatcherTest : XCTestCase
@property(nonatomic, strong) RNNDeepLinkDispatcher *uut;
@property(nonatomic, strong) id linkingManager;
@property(nonatomic, strong) UIScene *scene;
@end

@implementation RNNDeepLinkDispatcherTest

- (void)setUp {
    [super setUp];
    _uut = [RNNDeepLinkDispatcher new];
    _linkingManager = OCMClassMock([RCTLinkingManager class]);
    OCMStub(ClassMethod([_linkingManager application:OCMOCK_ANY
                                             openURL:OCMOCK_ANY
                                             options:OCMOCK_ANY]))
        .andReturn(YES);
    _scene = OCMClassMock([UIScene class]);
}

- (void)tearDown {
    [_linkingManager stopMocking];
    _uut = nil;
    [super tearDown];
}

- (void)reactRuntimeReady {
    [[NSNotificationCenter defaultCenter] postNotificationName:RCTContentDidAppearNotification
                                                        object:nil];
}

- (BOOL)replaysThroughSceneAPI {
#if RNN_RN_VERSION_88_OR_NEWER
    return RCTIsSceneDelegateApp();
#else
    return NO;
#endif
}

- (UIOpenURLContext *)contextWithURL:(NSURL *)url {
    id context = OCMClassMock([UIOpenURLContext class]);
    OCMStub([context URL]).andReturn(url);
    return context;
}

- (void)testQueuesURLUntilReactRuntimeIsReady {
    NSURL *url = [NSURL URLWithString:@"rnnplayground://pushed/1"];
    [_uut dispatchURL:url];
    OCMVerify(never(), ClassMethod([_linkingManager application:OCMOCK_ANY
                                                        openURL:url
                                                        options:OCMOCK_ANY]));

    [self reactRuntimeReady];
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY openURL:url options:OCMOCK_ANY]));
}

- (void)testDeliversURLImmediatelyOnceReady {
    [self reactRuntimeReady];
    NSURL *url = [NSURL URLWithString:@"rnnplayground://pushed/2"];
    [_uut dispatchURL:url];
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY openURL:url options:OCMOCK_ANY]));
}

- (void)testFlushesQueueInOrder {
    NSURL *first = [NSURL URLWithString:@"rnnplayground://first"];
    NSURL *second = [NSURL URLWithString:@"rnnplayground://second"];
    NSMutableArray<NSURL *> *delivered = [NSMutableArray array];
    [_linkingManager stopMocking];
    _linkingManager = OCMClassMock([RCTLinkingManager class]);
    OCMStub(ClassMethod([_linkingManager application:OCMOCK_ANY
                                             openURL:OCMOCK_ANY
                                             options:OCMOCK_ANY]))
        .andDo(^(NSInvocation *invocation) {
          __unsafe_unretained NSURL *url;
          [invocation getArgument:&url atIndex:3];
          [delivered addObject:url];
          BOOL result = YES;
          [invocation setReturnValue:&result];
        });

    [_uut dispatchURL:first];
    [_uut dispatchURL:second];
    [self reactRuntimeReady];

    NSArray *expected = @[ first, second ];
    XCTAssertEqualObjects(delivered, expected);
}

- (void)testIgnoresNilURL {
    [self reactRuntimeReady];
    [_uut dispatchURL:nil];
    OCMVerify(never(), ClassMethod([_linkingManager application:OCMOCK_ANY
                                                        openURL:OCMOCK_ANY
                                                        options:OCMOCK_ANY]));
}

- (void)testReplaysURLContextsThroughMatchingAPI {
    NSURL *url = [NSURL URLWithString:@"rnnplayground://pushed/3"];
    UIOpenURLContext *context = [self contextWithURL:url];
    [_uut dispatchOpenURLContexts:[NSSet setWithObject:context] scene:_scene];
    [self reactRuntimeReady];

#if RNN_RN_VERSION_88_OR_NEWER
    if ([self replaysThroughSceneAPI]) {
        OCMVerify(ClassMethod([_linkingManager scene:_scene
                                     openURLContexts:[NSSet setWithObject:context]]));
        OCMVerify(never(), ClassMethod([_linkingManager application:OCMOCK_ANY
                                                            openURL:OCMOCK_ANY
                                                            options:OCMOCK_ANY]));
        return;
    }
#endif
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY openURL:url options:OCMOCK_ANY]));
}

- (void)testReplaysEachURLContextSeparately {
    NSURL *first = [NSURL URLWithString:@"rnnplayground://first"];
    NSURL *second = [NSURL URLWithString:@"rnnplayground://second"];
    UIOpenURLContext *firstContext = [self contextWithURL:first];
    UIOpenURLContext *secondContext = [self contextWithURL:second];
    [self reactRuntimeReady];
    [_uut dispatchOpenURLContexts:[NSSet setWithObjects:firstContext, secondContext, nil]
                            scene:_scene];

#if RNN_RN_VERSION_88_OR_NEWER
    if ([self replaysThroughSceneAPI]) {
        OCMVerify(ClassMethod([_linkingManager scene:_scene
                                     openURLContexts:[NSSet setWithObject:firstContext]]));
        OCMVerify(ClassMethod([_linkingManager scene:_scene
                                     openURLContexts:[NSSet setWithObject:secondContext]]));
        return;
    }
#endif
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY
                                               openURL:first
                                               options:OCMOCK_ANY]));
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY
                                               openURL:second
                                               options:OCMOCK_ANY]));
}

- (void)testReplaysBrowsingWebUserActivityThroughMatchingAPI {
    NSURL *url = [NSURL URLWithString:@"https://example.com/pushed/4"];
    NSUserActivity *activity =
        [[NSUserActivity alloc] initWithActivityType:NSUserActivityTypeBrowsingWeb];
    activity.webpageURL = url;
    [_uut dispatchUserActivity:activity scene:_scene];
    [self reactRuntimeReady];

#if RNN_RN_VERSION_88_OR_NEWER
    if ([self replaysThroughSceneAPI]) {
        OCMVerify(ClassMethod([_linkingManager scene:_scene continueUserActivity:activity]));
        return;
    }
#endif
    OCMVerify(ClassMethod([_linkingManager application:OCMOCK_ANY openURL:url options:OCMOCK_ANY]));
}

- (void)testIgnoresOtherUserActivities {
    NSUserActivity *activity = [[NSUserActivity alloc] initWithActivityType:@"com.rnn.other"];
    [self reactRuntimeReady];
    [_uut dispatchUserActivity:activity scene:_scene];
    OCMVerify(never(), ClassMethod([_linkingManager application:OCMOCK_ANY
                                                        openURL:OCMOCK_ANY
                                                        options:OCMOCK_ANY]));
}

@end
