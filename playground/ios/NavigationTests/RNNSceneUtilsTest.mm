#import "RNNSceneUtils.h"
#import "RNNOverlayWindow.h"
#import "ReactNativeNavigation.h"
#import <XCTest/XCTest.h>

@interface RNNSceneUtilsTest : XCTestCase
@end

@implementation RNNSceneUtilsTest

- (UIWindowScene *)connectedWindowScene {
    for (UIScene *scene in UIApplication.sharedApplication.connectedScenes) {
        if ([scene isKindOfClass:[UIWindowScene class]]) {
            return (UIWindowScene *)scene;
        }
    }
    return nil;
}

- (void)testWindowWithoutSceneUsesFrame {
    // Created with initWithFrame:. In app-delegate apps UIKit may still attach it to the app's
    // implicit scene by itself, so only the frame is asserted here.
    CGRect frame = CGRectMake(0, 0, 100, 200);
    UIWindow *window = [UIWindow rnn_windowWithFrame:frame windowScene:nil];
    XCTAssertTrue(CGRectEqualToRect(window.frame, frame));
}

- (void)testWindowWithSceneIsAttachedToScene {
    UIWindowScene *scene = [self connectedWindowScene];
    if (scene == nil) {
        XCTSkip(@"The test host has no connected window scene");
    }
    CGRect frame = CGRectMake(0, 0, 100, 200);
    UIWindow *window = [UIWindow rnn_windowWithFrame:frame windowScene:scene];
    XCTAssertEqual(window.windowScene, scene);
    XCTAssertTrue(CGRectEqualToRect(window.frame, frame));
}

- (void)testWindowKeepsReceiverClass {
    UIWindow *withoutScene = [RNNOverlayWindow rnn_windowWithFrame:CGRectZero windowScene:nil];
    XCTAssertTrue([withoutScene isKindOfClass:[RNNOverlayWindow class]]);

    UIWindowScene *scene = [self connectedWindowScene];
    if (scene == nil) {
        XCTSkip(@"The test host has no connected window scene");
    }
    UIWindow *withScene = [RNNOverlayWindow rnn_windowWithFrame:CGRectZero windowScene:scene];
    XCTAssertTrue([withScene isKindOfClass:[RNNOverlayWindow class]]);
}

- (void)testWindowInMainWindowSceneFollowsLifeCycle {
    UIWindow *window = [UIWindow rnn_windowInMainWindowSceneWithFrame:CGRectZero];
    if (RNNUsesSceneLifecycle()) {
        // Scene-hosted test bundle: the scene delegate attached the main window to its scene.
        XCTAssertNotNil(window.windowScene);
        XCTAssertEqual(window.windowScene, [ReactNativeNavigation mainWindow].windowScene);
    } else {
        // App-delegate host: no behavior change, RNN passes no scene and uses initWithFrame:.
        XCTAssertNil(RNNMainWindowScene());
        XCTAssertNotNil(window);
    }
}

- (void)testMainWindowIsTheAppDelegateWindow {
    XCTAssertEqual([ReactNativeNavigation mainWindow],
                   UIApplication.sharedApplication.delegate.window);
}

- (void)testAttachMainWindowToSceneKeepsAppDelegateWindow {
    UIWindowScene *scene = [self connectedWindowScene];
    if (scene == nil) {
        XCTSkip(@"The test host has no connected window scene");
    }
    UIWindow *mainWindow = [ReactNativeNavigation mainWindow];
    [ReactNativeNavigation attachMainWindowToScene:scene];
    XCTAssertEqual([ReactNativeNavigation mainWindow], mainWindow);
    XCTAssertEqual(UIApplication.sharedApplication.delegate.window, mainWindow);
    XCTAssertEqual(mainWindow.windowScene, scene);
    XCTAssertTrue(mainWindow.isKeyWindow);
}

- (void)testKeyWindowFallsBackToMainWindow {
    UIWindow *keyWindow = RNNKeyWindow();
    XCTAssertNotNil(keyWindow);
    UIWindow *mainWindow = [ReactNativeNavigation mainWindow];
    XCTAssertTrue(keyWindow == mainWindow || keyWindow == mainWindow.windowScene.keyWindow);
}

@end
