#import "CommandsHandlerCreator.h"
#import "RNNTestRootViewCreator.h"
#import <ReactNativeNavigation/RNNViewControllerFactory.h>
#import <ReactNativeNavigation/RNNEventEmitter.h>
#import <ReactNativeNavigation/RNNLayoutManager.h>
#import <ReactNativeNavigation/RNNModalManager.h>
#import <ReactNativeNavigation/RNNOverlayManager.h>
#import <ReactNativeNavigation/RNNSetRootAnimator.h>
#import <ReactNativeNavigation/RNNTurboEventEmitter.h>

@implementation CommandsHandlerCreator

+ (RNNCommandsHandler *)createWithWindow:(UIWindow *)window {
    RNNTestRootViewCreator *creator = [RNNTestRootViewCreator new];
    RNNLayoutManager *layoutManager = [[RNNLayoutManager alloc] init];
    
#ifdef RCT_NEW_ARCH_ENABLED
	RNNTurboEventEmitter *eventEmmiter = [RNNTurboEventEmitter new];
#else
	RNNEventEmitter *eventEmmiter = [RNNEventEmitter new];
#endif
	
    RNNOverlayManager *overlayManager = [RNNOverlayManager new];
    RNNModalManager *modalManager = [RNNModalManager new];
	RNNViewControllerFactory *controllerFactory =
        [[RNNViewControllerFactory alloc] initWithRootViewCreator:creator
                                                 eventEmitter:eventEmmiter
                                                        store:nil
                                            componentRegistry:nil
                                                    andBridge:nil
                                  bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
    RNNCommandsHandler *commandsHandler =
        [[RNNCommandsHandler alloc] initWithViewControllerFactory:controllerFactory
                                                layoutManager:layoutManager
                                                 eventEmitter:eventEmmiter
                                                 modalManager:modalManager
                                               overlayManager:overlayManager
                                              setRootAnimator:[RNNSetRootAnimator new]
                                                   mainWindow:window];
    [commandsHandler setReadyToReceiveCommands:YES];
    [commandsHandler setDefaultOptions:@{
        @"animations" : @{@"push" : @{@"enabled" : @(0)}, @"pop" : @{@"enabled" : @(0)}},
        @"topBar" : @{@"drawBehind" : @(1)},
        @"layout" : @{@"componentBackgroundColor" : @(0xFF00FF00)}
    }
                            completion:^{
                            }];

    return commandsHandler;
}

@end
