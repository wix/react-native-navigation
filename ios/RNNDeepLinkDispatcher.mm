#import "RNNDeepLinkDispatcher.h"
#import "RNNReactNativeVersion.h"

#import <React/RCTLinkingManager.h>
#import <React/RCTLog.h>
#import <React/RCTRootView.h>
#import <React/RCTUtils.h>

@interface RNNPendingDeepLink : NSObject
@property(nonatomic, strong, nullable) NSURL *url;
@property(nonatomic, copy, nullable) NSSet<UIOpenURLContext *> *URLContexts;
@property(nonatomic, strong, nullable) NSUserActivity *userActivity;
@property(nonatomic, strong, nullable) UIScene *scene;
@end

@implementation RNNPendingDeepLink
@end

@implementation RNNDeepLinkDispatcher {
    NSMutableArray<RNNPendingDeepLink *> *_pendingLinks;
    BOOL _reactRuntimeReady;
}

+ (instancetype)sharedDispatcher {
    static RNNDeepLinkDispatcher *dispatcher = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      dispatcher = [[RNNDeepLinkDispatcher alloc] init];
    });
    return dispatcher;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _pendingLinks = [NSMutableArray array];
        // `RCTContentDidAppearNotification` is posted by Fabric's root view once content has
        // rendered. RNN forces bridgeless/new-arch, so the legacy
        // `RCTJavaScriptDidLoadNotification` never fires; we rely on this Fabric signal
        // exclusively.
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(handleReactRuntimeReady:)
                                                     name:RCTContentDidAppearNotification
                                                   object:nil];
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

#pragma mark - Public

- (void)dispatchURL:(NSURL *)url {
    if (url == nil) {
        return;
    }
    RNNPendingDeepLink *link = [RNNPendingDeepLink new];
    link.url = url;
    [self deliverOrEnqueue:link];
}

- (void)dispatchOpenURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts scene:(UIScene *)scene {
    if (URLContexts.count == 0) {
        return;
    }
    RNNPendingDeepLink *link = [RNNPendingDeepLink new];
    link.URLContexts = URLContexts;
    link.scene = scene;
    [self deliverOrEnqueue:link];
}

- (void)dispatchUserActivity:(NSUserActivity *)userActivity scene:(UIScene *)scene {
    if (![userActivity.activityType isEqualToString:NSUserActivityTypeBrowsingWeb] ||
        userActivity.webpageURL == nil) {
        return;
    }
    RNNPendingDeepLink *link = [RNNPendingDeepLink new];
    link.userActivity = userActivity;
    link.scene = scene;
    [self deliverOrEnqueue:link];
}

#pragma mark - Queue

- (void)deliverOrEnqueue:(RNNPendingDeepLink *)link {
    if (_reactRuntimeReady) {
        [self deliver:link];
        return;
    }
    [_pendingLinks addObject:link];
}

- (void)handleReactRuntimeReady:(NSNotification *)notification {
    _reactRuntimeReady = YES;
    NSArray<RNNPendingDeepLink *> *pending = [_pendingLinks copy];
    [_pendingLinks removeAllObjects];
    for (RNNPendingDeepLink *link in pending) {
        [self deliver:link];
    }
}

#pragma mark - Delivery

- (void)deliver:(RNNPendingDeepLink *)link {
    if (link.URLContexts) {
#if RNN_RN_VERSION_88_OR_NEWER
        if (RCTIsSceneDelegateApp()) {
            // RCTLinkingManager only forwards the first context of the set it is given.
            for (UIOpenURLContext *context in link.URLContexts) {
                [RCTLinkingManager scene:link.scene openURLContexts:[NSSet setWithObject:context]];
            }
            return;
        }
#endif
        for (UIOpenURLContext *context in link.URLContexts) {
            [self deliverThroughAppDelegateAPI:context.URL];
        }
        return;
    }

    if (link.userActivity) {
#if RNN_RN_VERSION_88_OR_NEWER
        if (RCTIsSceneDelegateApp()) {
            [RCTLinkingManager scene:link.scene continueUserActivity:link.userActivity];
            return;
        }
#endif
        [self deliverThroughAppDelegateAPI:link.userActivity.webpageURL];
        return;
    }

    [self deliverThroughAppDelegateAPI:link.url];
}

- (void)deliverThroughAppDelegateAPI:(NSURL *)url {
    if (url == nil) {
        return;
    }
    BOOL delivered = [RCTLinkingManager application:[UIApplication sharedApplication]
                                            openURL:url
                                            options:@{}];
    if (!delivered) {
        RCTLogWarn(@"[RNN] Deep link %@ was not delivered to Linking: this React Native version "
                   @"ignores app-delegate links in apps that use the UIScene life cycle. Under "
                   @"scenes, RNN can only replay links that iOS delivered to the scene delegate. "
                   @"See 'Adopting UIScene' in the RNN installation docs.",
                   url.absoluteString);
    }
}

@end
