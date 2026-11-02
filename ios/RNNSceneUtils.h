#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/// YES when the app's Info.plist declares `UIApplicationSceneManifest`, i.e. UIKit runs the app on
/// the UIScene life cycle. Apps without a manifest keep today's app-delegate behavior everywhere.
FOUNDATION_EXPORT BOOL RNNUsesSceneLifecycle(void);

/// The window scene of RNN's main window, or nil for app-delegate apps and before a scene has
/// been attached with `+[ReactNativeNavigation attachMainWindowToScene:]`.
FOUNDATION_EXPORT UIWindowScene *_Nullable RNNMainWindowScene(void);

/// The key window of the main window's scene, falling back to the main window.
FOUNDATION_EXPORT UIWindow *RNNKeyWindow(void);

/// The status bar frame. Read from the main window scene's status bar manager under the UIScene
/// life cycle, from `UIApplication` otherwise.
FOUNDATION_EXPORT CGRect RNNStatusBarFrame(void);

@interface UIWindow (RNNScene)

/// Creates a window of the receiver's class. With a scene it uses `initWithWindowScene:` and then
/// sets `frame`; without one it uses `initWithFrame:`.
+ (instancetype)rnn_windowWithFrame:(CGRect)frame windowScene:(nullable UIWindowScene *)windowScene;

/// Creates a window of the receiver's class in the main window's scene (see `RNNMainWindowScene`).
/// Windows created with `initWithFrame:` never appear under the UIScene life cycle.
+ (instancetype)rnn_windowInMainWindowSceneWithFrame:(CGRect)frame;

@end

NS_ASSUME_NONNULL_END
