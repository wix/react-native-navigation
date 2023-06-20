#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>

#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    self.initialProps = @{};

    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    if (@available(iOS 13.0, *)) {
        self.window.backgroundColor = [UIColor systemBackgroundColor];
    } else {
        self.window.backgroundColor = [UIColor whiteColor];
    }
    [self.window makeKeyWindow];

    if (!self.bridge) {
        self.bridge = [self createBridgeWithDelegate:self launchOptions:launchOptions];
    }

    [ReactNativeNavigation bootstrapWithBridge:self.bridge];
    [ReactNativeNavigation
        registerExternalComponent:@"RNNCustomComponent"
                         callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
                           return [[RNNCustomViewController alloc] initWithProps:props];
                         }];

    return YES;
}

#pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
#if DEBUG
    return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
    return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return [ReactNativeNavigation extraModulesForBridge:bridge];
}

@end
