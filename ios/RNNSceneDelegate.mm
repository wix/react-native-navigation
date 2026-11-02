#import "RNNSceneDelegate.h"
#import "ReactNativeNavigation.h"

@interface RNNSceneDelegate ()
@property(nonatomic, copy, readwrite) NSSet<UIOpenURLContext *> *launchURLContexts;
@property(nonatomic, copy, readwrite) NSSet<NSUserActivity *> *launchUserActivities;
@end

@implementation RNNSceneDelegate

- (instancetype)init {
    self = [super init];
    if (self) {
        _launchURLContexts = [NSSet set];
        _launchUserActivities = [NSSet set];
    }
    return self;
}

#pragma mark - UISceneDelegate

- (void)scene:(UIScene *)scene
    willConnectToSession:(UISceneSession *)session
                 options:(UISceneConnectionOptions *)connectionOptions {
    if (![scene isKindOfClass:[UIWindowScene class]]) {
        return;
    }

    [ReactNativeNavigation attachMainWindowToScene:(UIWindowScene *)scene];
    self.window = [ReactNativeNavigation mainWindow];

    self.launchURLContexts = connectionOptions.URLContexts ?: [NSSet set];
    self.launchUserActivities = connectionOptions.userActivities ?: [NSSet set];
    [self dispatchLaunchLinksFromConnectionOptions:connectionOptions scene:scene];
}

- (void)scene:(UIScene *)scene openURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts {
    [ReactNativeNavigation dispatchOpenURLContexts:URLContexts inScene:scene];
}

- (void)scene:(UIScene *)scene continueUserActivity:(NSUserActivity *)userActivity {
    [ReactNativeNavigation dispatchUserActivity:userActivity inScene:scene];
}

#pragma mark - Deep linking

- (void)dispatchLaunchLinksFromConnectionOptions:(UISceneConnectionOptions *)connectionOptions
                                           scene:(UIScene *)scene {
    if (connectionOptions.URLContexts.count > 0) {
        [ReactNativeNavigation dispatchOpenURLContexts:connectionOptions.URLContexts inScene:scene];
    }
    for (NSUserActivity *userActivity in connectionOptions.userActivities) {
        [ReactNativeNavigation dispatchUserActivity:userActivity inScene:scene];
    }
}

- (void)dispatchDeepLinkURL:(NSURL *)url {
    [ReactNativeNavigation dispatchDeepLinkURL:url];
}

- (NSURL *)launchURL {
    NSURL *url = self.launchURLContexts.anyObject.URL;
    if (url) {
        return url;
    }
    for (NSUserActivity *userActivity in self.launchUserActivities) {
        if ([userActivity.activityType isEqualToString:NSUserActivityTypeBrowsingWeb] &&
            userActivity.webpageURL) {
            return userActivity.webpageURL;
        }
    }
    return nil;
}

@end
