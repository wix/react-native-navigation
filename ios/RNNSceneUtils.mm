#import "RNNSceneUtils.h"
#import "ReactNativeNavigation.h"

BOOL RNNUsesSceneLifecycle(void) {
    static BOOL usesSceneLifecycle = NO;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      usesSceneLifecycle =
          [NSBundle.mainBundle objectForInfoDictionaryKey:@"UIApplicationSceneManifest"] != nil;
    });
    return usesSceneLifecycle;
}

UIWindowScene *RNNMainWindowScene(void) {
    if (!RNNUsesSceneLifecycle()) {
        return nil;
    }
    return [ReactNativeNavigation mainWindow].windowScene;
}

UIWindow *RNNKeyWindow(void) {
    UIWindow *mainWindow = [ReactNativeNavigation mainWindow];
    return mainWindow.windowScene.keyWindow ?: mainWindow;
}

CGRect RNNStatusBarFrame(void) {
    UIWindowScene *scene = RNNMainWindowScene();
    if (scene) {
        return scene.statusBarManager.statusBarFrame;
    }
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    return UIApplication.sharedApplication.statusBarFrame;
#pragma clang diagnostic pop
}

@implementation UIWindow (RNNScene)

+ (instancetype)rnn_windowWithFrame:(CGRect)frame windowScene:(UIWindowScene *)windowScene {
    if (windowScene) {
        UIWindow *window = [[self alloc] initWithWindowScene:windowScene];
        window.frame = frame;
        return window;
    }
    return [[self alloc] initWithFrame:frame];
}

+ (instancetype)rnn_windowInMainWindowSceneWithFrame:(CGRect)frame {
    return [self rnn_windowWithFrame:frame windowScene:RNNMainWindowScene()];
}

@end
