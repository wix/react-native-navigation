#import "TestingAppDelegate.h"

@implementation TestingAppDelegate

- (void)applicationDidFinishLaunching:(UIApplication *)application {
    _window = [UIWindow new];
    _window.rootViewController = [UIViewController new];
    [_window makeKeyAndVisible];
}

@end
