#import <Foundation/Foundation.h>
#import "RNNTurboManager.h"
#import "RNNCommandsHandler.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNLayoutManager.h"
#import "RNNModalHostViewManagerHandler.h"
#import "RNNReactComponentRegistry.h"
#import "RNNReactRootViewCreator.h"
#import "RNNTurboCommandsHandler.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#endif

@interface RNNTurboManager ()

@property(nonatomic, strong, readwrite) RCTHost *host;
@property(nonatomic, strong, readwrite) RNNExternalComponentStore *store;
@property(nonatomic, strong, readwrite) RNNReactComponentRegistry *componentRegistry;
@property(nonatomic, strong, readonly) RNNLayoutManager *layoutManager;
@property(nonatomic, strong, readonly) RNNOverlayManager *overlayManager;
@property(nonatomic, strong, readonly) RNNModalManager *modalManager;
@property(nonatomic, strong, readonly) RNNModalHostViewManagerHandler *modalHostViewHandler;
@property(nonatomic, strong, readonly) RNNCommandsHandler *commandsHandler;

@end

@implementation RNNTurboManager {
    UIWindow *_mainWindow;
}

- (instancetype)initWithHost:(RCTHost *)host mainWindow:(UIWindow *)mainWindow {
    if (self = [super init]) {
        _host = host;
        _mainWindow = mainWindow;
        _overlayManager = [RNNOverlayManager new];
        _store = [RNNExternalComponentStore new];
        
        RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];
        RNNModalManagerEventHandler *modalManagerEventHandler =
            [[RNNModalManagerEventHandler alloc] initWithEventEmitter:eventEmitter];
        
        _modalManager = [[RNNModalManager alloc] initWithHost:_host
                                                 eventHandler:modalManagerEventHandler];
        _modalHostViewHandler =
            [[RNNModalHostViewManagerHandler alloc] initWithModalManager:_modalManager];
        _layoutManager = [[RNNLayoutManager alloc] init];

        id<RNNComponentViewCreator> rootViewCreator =
            [[RNNReactRootViewCreator alloc] initWithHost:_host eventEmitter:eventEmitter];
        
        _componentRegistry = [[RNNReactComponentRegistry alloc] initWithCreator:rootViewCreator];
        
        RNNControllerFactory *controllerFactory =
            [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator
                                                     eventEmitter:eventEmitter
                                                            store:_store
                                                componentRegistry:_componentRegistry
                                                        andHost:_host
                                      bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
        
        RNNSetRootAnimator *setRootAnimator = [RNNSetRootAnimator new];
        _commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory
                                                                   layoutManager:_layoutManager
                                                                    eventEmitter:eventEmitter
                                                                    modalManager:_modalManager
                                                                  overlayManager:_overlayManager
                                                                 setRootAnimator:setRootAnimator
                                                                      mainWindow:_mainWindow];
        
        [RNNTurboCommandsHandler setSharedInstance:_commandsHandler];
    }
    
    return self;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback {
    [_store registerExternalHostComponent:name callback:callback];
}

- (UIViewController *)findComponentForId:(NSString *)componentId {
    return [_layoutManager findComponentForId:componentId];
}

@end
