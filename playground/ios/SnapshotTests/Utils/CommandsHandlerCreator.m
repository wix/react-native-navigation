#import "CommandsHandlerCreator.h"
#import "RNNTestRootViewCreator.h"
#import <ReactNativeNavigation/RNNControllerFactory.h>
#import <ReactNativeNavigation/RNNEventEmitter.h>
#import <ReactNativeNavigation/RNNLayoutManager.h>
#import <ReactNativeNavigation/RNNModalManager.h>
#import <ReactNativeNavigation/RNNOverlayManager.h>
#import <ReactNativeNavigation/RNNSetRootAnimator.h>

@implementation CommandsHandlerCreator

+ (RNNCommandsHandler *)createWithWindow:(UIWindow *)window {
    RNNTestRootViewCreator *creator = [RNNTestRootViewCreator new];
    RNNLayoutManager *layoutManager = [[RNNLayoutManager alloc] init];
    RNNEventEmitter *eventEmmiter = [RNNEventEmitter new];
    RNNOverlayManager *overlayManager = [RNNOverlayManager new];
    RNNModalManager *modalManager = [RNNModalManager new];
    RNNControllerFactory *controllerFactory =
        [[RNNControllerFactory alloc] initWithRootViewCreator:creator
                                                 eventEmitter:eventEmmiter
                                                        store:nil
                                            componentRegistry:nil
                                                    andBridge:nil
                                  bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
    RNNCommandsHandler *commandsHandler =
        [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory
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
