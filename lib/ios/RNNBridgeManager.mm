#import "RNNBridgeManager.h"

#import "RNNBridgeModule.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNLayoutManager.h"
#import "RNNModalHostViewManagerHandler.h"
#import "RNNReactComponentRegistry.h"
#import "RNNReactRootViewCreator.h"
#import "RNNSplashScreen.h"
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

@interface RNNBridgeManager ()

@property(nonatomic, strong, readwrite) RCTBridge *bridge;
@property(nonatomic, strong, readwrite) RNNExternalComponentStore *store;
@property(nonatomic, strong, readwrite) RNNReactComponentRegistry *componentRegistry;
@property(nonatomic, strong, readonly) RNNLayoutManager *layoutManager;
@property(nonatomic, strong, readonly) RNNOverlayManager *overlayManager;
@property(nonatomic, strong, readonly) RNNModalManager *modalManager;
@property(nonatomic, strong, readonly) RNNModalHostViewManagerHandler *modalHostViewHandler;

@end

@implementation RNNBridgeManager {
    RCTBridge *_bridge;
    UIWindow *_mainWindow;

    RNNExternalComponentStore *_store;

    RNNCommandsHandler *_commandsHandler;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge mainWindow:(UIWindow *)mainWindow {
    if (self = [super init]) {
        _bridge = bridge;
        _mainWindow = mainWindow;

        _overlayManager = [RNNOverlayManager new];

        _store = [RNNExternalComponentStore new];

        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(onJavaScriptLoaded)
                                                     name:RCTJavaScriptDidLoadNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(onJavaScriptWillLoad)
                                                     name:RCTJavaScriptWillStartLoadingNotification
                                                   object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(onBridgeWillReload)
                                                     name:RCTBridgeWillReloadNotification
                                                   object:nil];
    }
    return self;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
    [_store registerExternalComponent:name callback:callback];
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];
    RNNModalManagerEventHandler *modalManagerEventHandler =
        [[RNNModalManagerEventHandler alloc] initWithEventEmitter:eventEmitter];
    _modalManager = [[RNNModalManager alloc] initWithBridge:bridge
                                               eventHandler:modalManagerEventHandler];
    _modalHostViewHandler =
        [[RNNModalHostViewManagerHandler alloc] initWithModalManager:_modalManager];
    _layoutManager = [[RNNLayoutManager alloc] init];

    id<RNNComponentViewCreator> rootViewCreator =
        [[RNNReactRootViewCreator alloc] initWithBridge:bridge eventEmitter:eventEmitter];
    _componentRegistry = [[RNNReactComponentRegistry alloc] initWithCreator:rootViewCreator];
    RNNControllerFactory *controllerFactory =
        [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator
                                                 eventEmitter:eventEmitter
                                                        store:_store
                                            componentRegistry:_componentRegistry
                                                    andBridge:bridge
                                  bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
    RNNSetRootAnimator *setRootAnimator = [RNNSetRootAnimator new];
    _commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory
                                                               layoutManager:_layoutManager
                                                                eventEmitter:eventEmitter
                                                                modalManager:_modalManager
                                                              overlayManager:_overlayManager
                                                             setRootAnimator:setRootAnimator
                                                                  mainWindow:_mainWindow];
    RNNBridgeModule *bridgeModule =
        [[RNNBridgeModule alloc] initWithCommandsHandler:_commandsHandler];

    return @[ bridgeModule, eventEmitter ];
}

- (UIViewController *)findComponentForId:(NSString *)componentId {
    return [_layoutManager findComponentForId:componentId];
}

#pragma mark - JavaScript & Bridge Notifications

- (void)onJavaScriptWillLoad {
    [_componentRegistry clear];
}

- (void)onJavaScriptLoaded {
    [_commandsHandler setReadyToReceiveCommands:true];
    [_modalHostViewHandler
        connectModalHostViewManager:[self.bridge moduleForClass:RCTModalHostViewManager.class]];
    [[_bridge moduleForClass:[RNNEventEmitter class]] sendOnAppLaunched];
}

- (void)onBridgeWillReload {
    dispatch_async(dispatch_get_main_queue(), ^{
      [self->_overlayManager dismissAllOverlays];
      [self->_componentRegistry clear];
      UIApplication.sharedApplication.delegate.window.rootViewController = nil;
    });
}

@end
