#import <XCTest/XCTest.h>
#import "RNNOverlayManager.h"

@interface RNNOverlayManagerTest : XCTestCase

@property (nonatomic, retain) RNNOverlayManager* overlayManager;
@property (nonatomic, retain) UIViewController* overlayVC;

@end

@implementation RNNOverlayManagerTest

- (void)setUp {
    [super setUp];
	_overlayManager = [RNNOverlayManager new];
	_overlayVC = [UIViewController new];
}


//- (void)testShowOverlayShouldAddWindowWithVCAsRoot {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	XCTAssertTrue([window.rootViewController isEqual:_overlayVC]);
//}
//
//- (void)testShowOverlayShouldAddVisibleWindow {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	XCTAssertTrue(window.windowLevel == UIWindowLevelNormal);
//	XCTAssertFalse(window.hidden);
//}
//
//- (void)testDismissOverlayShouldCleanWindowRootVC {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	[_overlayManager dismissOverlay:_overlayVC];
//	XCTAssertNil(window.rootViewController);
//}
//
//- (void)testDismissOverlayShouldHideWindow {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	[_overlayManager dismissOverlay:_overlayVC];
//	XCTAssertTrue(window.hidden);
//}
//
//- (void)testDismissOverlayShouldRemoveOverlayWindow {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	[_overlayManager dismissOverlay:_overlayVC];
//	XCTAssertFalse([_overlayManager.overlayWindows containsObject:window]);
//}
//
//- (void)testDismissOverlayShouldNotRemoveWrongVC {
//	[_overlayManager showOverlay:_overlayVC];
//	UIWindow* window = _overlayManager.overlayWindows.lastObject;
//	[_overlayManager dismissOverlay:[UIViewController new]];
//	XCTAssertTrue([_overlayManager.overlayWindows containsObject:window]);
//}

@end
