#import "RNNOverlayManager.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNOverlayManagerTest : XCTestCase

@property(nonatomic, retain) RNNOverlayManager *overlayManager;
@property(nonatomic, retain) UIViewController *overlayVC;
@property(nonatomic, retain) RNNOverlayWindow *overlayWindow;

@end

@implementation RNNOverlayManagerTest

- (void)setUp {
    [super setUp];
    _overlayManager = [RNNOverlayManager new];
    _overlayVC = [UIViewController new];
    _overlayWindow = [OCMockObject partialMockForObject:[RNNOverlayWindow new]];
    OCMStub([_overlayWindow makeKeyAndVisible]);
    _overlayWindow.rootViewController = _overlayVC;
}

- (void)testShowOverlayShouldAddWindowWithVCAsRoot {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    XCTAssertTrue([window.rootViewController isEqual:_overlayVC]);
}

- (void)testShowOverlayShouldSetKeyAndVisibleWindow {
    id window = _overlayManager.overlayWindows.lastObject;
    [[window expect] makeKeyAndVisible];
    [_overlayManager showOverlayWindow:_overlayWindow];
    [window verify];
}

- (void)testShowOverlayShouldCreateTransparentView {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    XCTAssertTrue(window.rootViewController.view.backgroundColor == [UIColor clearColor]);
}

- (void)testDismissOverlayShouldCleanWindowRootVC {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    [_overlayManager dismissOverlay:_overlayVC];
    XCTAssertNil(window.rootViewController);
}

- (void)testDismissOverlayShouldHideWindow {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    [_overlayManager dismissOverlay:_overlayVC];
    XCTAssertTrue(window.hidden);
}

- (void)testDismissOverlayShouldRemoveOverlayWindow {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    [_overlayManager dismissOverlay:_overlayVC];
    XCTAssertFalse([_overlayManager.overlayWindows containsObject:window]);
}

- (void)testDismissOverlayShouldNotRemoveWrongVC {
    [_overlayManager showOverlayWindow:_overlayWindow];
    UIWindow *window = _overlayManager.overlayWindows.lastObject;
    [_overlayManager dismissOverlay:[UIViewController new]];
    XCTAssertTrue([_overlayManager.overlayWindows containsObject:window]);
}

@end
