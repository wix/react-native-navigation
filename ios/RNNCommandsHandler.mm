#import "RNNCommandsHandler.h"
#import "AnimationObserver.h"
#import "RNNAssert.h"
#import "RNNComponentViewController.h"
#import "RNNConvert.h"
#import "RNNDefaultOptionsHelper.h"
#import "RNNErrorHandler.h"
#import "React/RCTI18nUtil.h"
#import "UINavigationController+RNNCommands.h"
#import "UIViewController+RNNOptions.h"
#import "RNNUtils.h"
#import "RNNStackController.h"
#import "RNNBottomTabsController.h"
#import "RNNSideMenuViewController.h"
#import "RNNSideMenuChildViewController.h"
#import "RNNTopTabsViewController.h"
#import "RNNSplitViewController.h"
#import "RNNExternalViewController.h"
#import "RNNOverlayWindow.h"

static NSString *const setRoot = @"setRoot";
static NSString *const setStackRoot = @"setStackRoot";
static NSString *const push = @"push";
static NSString *const preview = @"preview";
static NSString *const pop = @"pop";
static NSString *const popTo = @"popTo";
static NSString *const popToRoot = @"popToRoot";
static NSString *const showModal = @"showModal";
static NSString *const dismissModal = @"dismissModal";
static NSString *const dismissAllModals = @"dismissAllModals";
static NSString *const showOverlay = @"showOverlay";
static NSString *const dismissOverlay = @"dismissOverlay";
static NSString *const dismissAllOverlays = @"dismissAllOverlays";
static NSString *const mergeOptions = @"mergeOptions";
static NSString *const setDefaultOptions = @"setDefaultOptions";

@interface RNNCommandsHandler ()

@end

@implementation RNNCommandsHandler {
	RNNViewControllerFactory *_controllerFactory;
    RNNLayoutManager *_layoutManager;
    RNNModalManager *_modalManager;
    RNNOverlayManager *_overlayManager;
    RNNEventEmitter *_eventEmitter;
    UIWindow *_mainWindow;
    RNNSetRootAnimator *_setRootAnimator;
}

- (instancetype)initWithViewControllerFactory:(RNNViewControllerFactory *)controllerFactory
                            layoutManager:(RNNLayoutManager *)layoutManager
                             eventEmitter:(RNNEventEmitter *)eventEmitter
                             modalManager:(RNNModalManager *)modalManager
                           overlayManager:(RNNOverlayManager *)overlayManager
                          setRootAnimator:(RNNSetRootAnimator *)setRootAnimator
                               mainWindow:(UIWindow *)mainWindow {
    self = [super init];
    _controllerFactory = controllerFactory;
    _layoutManager = layoutManager;
    _eventEmitter = eventEmitter;
    _modalManager = modalManager;
    _overlayManager = overlayManager;
    _setRootAnimator = setRootAnimator;
    _mainWindow = mainWindow;
    return self;
}

#pragma mark - public

- (void)setRoot:(NSDictionary *)layout
      commandId:(NSString *)commandId
     completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
    [self assertReady];
    RNNAssertMainQueue();

    if (_controllerFactory.defaultOptions.layout.direction.hasValue) {
        if ([_controllerFactory.defaultOptions.layout.direction.get isEqualToString:@"rtl"]) {
            [[RCTI18nUtil sharedInstance] allowRTL:YES];
            [[RCTI18nUtil sharedInstance] forceRTL:YES];
            [[UIView appearance]
                setSemanticContentAttribute:UISemanticContentAttributeForceRightToLeft];
            [[UINavigationBar appearance]
                setSemanticContentAttribute:UISemanticContentAttributeForceRightToLeft];
        } else {
            [[RCTI18nUtil sharedInstance] allowRTL:NO];
            [[RCTI18nUtil sharedInstance] forceRTL:NO];
            [[UIView appearance]
                setSemanticContentAttribute:UISemanticContentAttributeForceLeftToRight];
            [[UINavigationBar appearance]
                setSemanticContentAttribute:UISemanticContentAttributeForceLeftToRight];
        }
    }

    [_modalManager reset];

    UIViewController *vc = [_controllerFactory createLayout:layout[@"root"]];
    [_layoutManager addPendingViewController:vc];

    RNNNavigationOptions *optionsWithDefault = vc.resolveOptionsWithDefault;
    vc.waitForRender = [optionsWithDefault.animations.setRoot.waitForRender withDefault: [RNNUtils getDefaultWaitForRender]];

    __weak UIViewController *weakVC = vc;
    [vc setReactViewReadyCallback:^{
      [self->_mainWindow.rootViewController destroy];
      self->_mainWindow.rootViewController = weakVC;

      [self->_setRootAnimator
             animate:self->_mainWindow
            duration:[optionsWithDefault.animations.setRoot.alpha.duration withDefault:0]
          completion:^{
            [self->_layoutManager removePendingViewController:weakVC];
            [self->_eventEmitter sendOnNavigationCommandCompletion:setRoot commandId:commandId];
            [self emitStateChangedForCommand:setRoot commandId:commandId];
            completion(weakVC.layoutInfo.componentId);
          }];
    }];

    [vc render];
}

- (void)mergeOptions:(NSString *)componentId
             options:(NSDictionary *)mergeOptions
          completion:(RNNTransitionCompletionBlock)completion {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController<RNNLayoutProtocol> *vc = [_layoutManager findComponentForId:componentId];
    RNNNavigationOptions *newOptions = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
    if (vc && ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] ||
               [vc isKindOfClass:[RNNComponentViewController class]])) {
        [CATransaction begin];
        [CATransaction setCompletionBlock:completion];

        [vc mergeOptions:newOptions];

        [CATransaction commit];
    } else {
        if (completion) {
            completion();
        }
    }
}

- (void)setDefaultOptions:(NSDictionary *)optionsDict
               completion:(RNNTransitionCompletionBlock)completion {
    RNNAssertMainQueue();

    RNNNavigationOptions *defaultOptions = [[RNNNavigationOptions alloc] initWithDict:optionsDict];
    [_controllerFactory setDefaultOptions:defaultOptions];

    UIViewController *rootViewController =
        UIApplication.sharedApplication.delegate.window.rootViewController;
    [RNNDefaultOptionsHelper recursivelySetDefaultOptions:defaultOptions
                                     onRootViewController:rootViewController];

	if (completion != nil) {
		completion();
	}
}

- (void)push:(NSString *)componentId
     commandId:(NSString *)commandId
        layout:(NSDictionary *)layout
    completion:(RNNTransitionWithComponentIdCompletionBlock)completion
     rejection:(RCTPromiseRejectBlock)rejection {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController *newVc = [_controllerFactory createLayout:layout];
    [_layoutManager addPendingViewController:newVc];

    UIViewController *fromVC = [_layoutManager findComponentForId:componentId];
    RNNNavigationOptions *optionsWithDefault = newVc.resolveOptionsWithDefault;

    if ([[optionsWithDefault.preview.reactTag withDefault:@(0)] floatValue] > 0) {
        if ([fromVC isKindOfClass:[RNNComponentViewController class]]) {
            RNNComponentViewController *rootVc = (RNNComponentViewController *)fromVC;
            rootVc.previewController = newVc;
            [newVc render];

            rootVc.previewCallback = ^(UIViewController *vcc) {
              RNNComponentViewController *rvc = (RNNComponentViewController *)vcc;
              [self->_eventEmitter sendOnPreviewCompleted:componentId
                                       previewComponentId:newVc.layoutInfo.componentId];
              if ([newVc.resolveOptionsWithDefault.preview.commit withDefault:NO]) {
                  [CATransaction begin];
                  [CATransaction setCompletionBlock:^{
                    [self->_layoutManager removePendingViewController:newVc];
                    [self->_eventEmitter sendOnNavigationCommandCompletion:push
                                                                 commandId:commandId];
                    [self emitStateChangedForCommand:push commandId:commandId];
                    completion(newVc.layoutInfo.componentId);
                  }];
                  [rvc.navigationController pushViewController:newVc animated:YES];
                  [CATransaction commit];
              }
            };

            CGSize size = CGSizeMake(rootVc.view.frame.size.width, rootVc.view.frame.size.height);

            if (optionsWithDefault.preview.width.hasValue) {
                size.width = [newVc.resolveOptionsWithDefault.preview.width.get floatValue];
            }

            if (optionsWithDefault.preview.height.hasValue) {
                size.height = [newVc.resolveOptionsWithDefault.preview.height.get floatValue];
            }

            if (optionsWithDefault.preview.width.hasValue ||
                optionsWithDefault.preview.height.hasValue) {
                newVc.preferredContentSize = size;
            }
            RCTExecuteOnMainQueue(^{
				UIView *view = nil;
#ifdef RCT_NEW_ARCH_ENABLED
				RCTHost *host = [ReactNativeNavigation getHost];

				if (host != nil) {
					view = [host.surfacePresenter.mountingManager.componentViewRegistry
						findComponentViewWithTag: [optionsWithDefault.preview.reactTag.get integerValue]];
				} else {
					view = [[ReactNativeNavigation getBridge].uiManager
						viewForReactTag:optionsWithDefault.preview.reactTag.get];
				}
#else
				view = [[ReactNativeNavigation getBridge].uiManager
					viewForReactTag:optionsWithDefault.preview.reactTag.get];
#endif
				[rootVc registerForPreviewingWithDelegate:(id)rootVc sourceView:view];
            });
        }
    } else {
        BOOL animated = [optionsWithDefault.animations.push.enable withDefault:YES];
        BOOL waitForRender = optionsWithDefault.animations.push.shouldWaitForRender;
        newVc.waitForRender = waitForRender;
        __weak UIViewController *weakNewVC = newVc;
        [newVc setReactViewReadyCallback:^{
          if (animated && !waitForRender)
              [[AnimationObserver sharedObserver] beginAnimation];
          [fromVC.stack push:weakNewVC
                       onTop:fromVC
                    animated:animated
                  completion:^{
                    [self->_layoutManager removePendingViewController:weakNewVC];
                    [self->_eventEmitter sendOnNavigationCommandCompletion:push
                                                                 commandId:commandId];
                    [self emitStateChangedForCommand:push commandId:commandId];
                    completion(weakNewVC.layoutInfo.componentId);
                  }
                   rejection:rejection];
        }];

        [newVc render];
    }
}

- (void)setStackRoot:(NSString *)componentId
           commandId:(NSString *)commandId
            children:(NSArray *)children
          completion:(RNNTransitionCompletionBlock)completion
           rejection:(RCTPromiseRejectBlock)rejection {
    [self assertReady];
    RNNAssertMainQueue();

    NSArray<UIViewController *> *childViewControllers =
        [_controllerFactory createChildrenLayout:children];
    for (UIViewController<RNNLayoutProtocol> *viewController in childViewControllers) {
        if (![viewController isEqual:childViewControllers.lastObject]) {
            [viewController render];
        }
    }

    UIViewController *newVC = childViewControllers.lastObject;
    [_layoutManager addPendingViewController:newVC];

    UIViewController *fromVC = [_layoutManager findComponentForId:componentId];

    RNNNavigationOptions *options = newVC.resolveOptionsWithDefault;
    newVC.waitForRender = ([options.animations.setStackRoot.waitForRender withDefault: [RNNUtils getDefaultWaitForRender]]);

    __weak RNNEventEmitter *weakEventEmitter = _eventEmitter;
    __weak UIViewController *weakNewVC = newVC;
    [newVC setReactViewReadyCallback:^{
      [fromVC.stack setStackChildren:childViewControllers
                  fromViewController:fromVC
                            animated:[options.animations.setStackRoot.enable withDefault: [RNNUtils getDefaultWaitForRender]]
                          completion:^{
                            [self->_layoutManager removePendingViewController:weakNewVC];
                            [weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot
                                                                      commandId:commandId];
                            [self emitStateChangedForCommand:setStackRoot commandId:commandId];
                            completion();
                          }
                           rejection:rejection];
    }];

    [newVC render];
}

- (void)pop:(NSString *)componentId
       commandId:(NSString *)commandId
    mergeOptions:(NSDictionary *)mergeOptions
      completion:(RNNTransitionCompletionBlock)completion
       rejection:(RCTPromiseRejectBlock)rejection {
    [self assertReady];
    RNNAssertMainQueue();

    RNNComponentViewController *vc =
        (RNNComponentViewController *)[_layoutManager findComponentForId:componentId];
    if (vc) {
        RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
        [vc mergeOptions:options];

        [vc.stack popAnimated:[vc.resolveOptionsWithDefault.animations.pop.enable withDefault:YES]
                   completion:^{
                     [self->_eventEmitter sendOnNavigationCommandCompletion:pop
                                                                  commandId:commandId];
                     [self emitStateChangedForCommand:pop commandId:commandId];
                     completion();
                   }
                    rejection:rejection];
    } else {
        [RNNErrorHandler
                      reject:rejection
               withErrorCode:1012
            errorDescription:
                [NSString stringWithFormat:@"Popping component failed - componentId '%@' not found",
                                           componentId]];
    }
}

- (void)popTo:(NSString *)componentId
       commandId:(NSString *)commandId
    mergeOptions:(NSDictionary *)mergeOptions
      completion:(RNNTransitionCompletionBlock)completion
       rejection:(RCTPromiseRejectBlock)rejection {
    [self assertReady];
    RNNAssertMainQueue();

    RNNComponentViewController *vc =
        (RNNComponentViewController *)[_layoutManager findComponentForId:componentId];
    if (vc) {
        RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
        [vc mergeOptions:options];

        [vc.stack popTo:vc
               animated:[vc.resolveOptionsWithDefault.animations.pop.enable withDefault:YES]
             completion:^(NSArray *poppedViewControllers) {
               [self->_eventEmitter sendOnNavigationCommandCompletion:popTo commandId:commandId];
               [self emitStateChangedForCommand:popTo commandId:commandId];
               completion();
             }
              rejection:rejection];
    } else {
        [RNNErrorHandler
                      reject:rejection
               withErrorCode:1012
            errorDescription:
                [NSString stringWithFormat:@"PopTo component failed - componentId '%@' not found",
                                           componentId]];
    }
}

- (void)popToRoot:(NSString *)componentId
        commandId:(NSString *)commandId
     mergeOptions:(NSDictionary *)mergeOptions
       completion:(RNNTransitionCompletionBlock)completion
        rejection:(RCTPromiseRejectBlock)rejection {
    [self assertReady];
    RNNAssertMainQueue();

    RNNComponentViewController *vc =
        (RNNComponentViewController *)[_layoutManager findComponentForId:componentId];
    if (vc) {
        RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
        [vc mergeOptions:options];

        [vc.stack popToRoot:vc
                   animated:[vc.resolveOptionsWithDefault.animations.pop.enable withDefault:YES]
                 completion:^(NSArray *poppedViewControllers) {
                   [self->_eventEmitter sendOnNavigationCommandCompletion:popToRoot
                                                                commandId:commandId];
                   [self emitStateChangedForCommand:popToRoot commandId:commandId];
                   completion();
                 }
                  rejection:rejection];
    } else {
        [RNNErrorHandler
                      reject:rejection
               withErrorCode:1012
            errorDescription:
                [NSString stringWithFormat:
                              @"PopToRoot component failed - componentId '%@' not found",
                              componentId]];
    }
}

- (void)showModal:(NSDictionary *)layout
        commandId:(NSString *)commandId
       completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController *newVc = [_controllerFactory createLayout:layout];
    RNNNavigationOptions *withDefault = newVc.resolveOptionsWithDefault;

    [_layoutManager addPendingViewController:newVc];

    __weak UIViewController *weakNewVC = newVc;
    BOOL animated = [withDefault.animations.showModal.enter.enable withDefault:YES];
    BOOL waitForRender = [withDefault.animations.showModal.enter shouldWaitForRender];
    newVc.waitForRender = waitForRender;
    newVc.modalPresentationStyle = [RNNConvert
        UIModalPresentationStyle:[withDefault.modalPresentationStyle withDefault:@"default"]];
    newVc.modalTransitionStyle = [RNNConvert
        UIModalTransitionStyle:[withDefault.modalTransitionStyle withDefault:@"coverVertical"]];

    if (animated && !waitForRender)
        [[AnimationObserver sharedObserver] beginAnimation];
    [newVc setReactViewReadyCallback:^{
      [self->_modalManager showModal:weakNewVC
                            animated:animated
                          completion:^(NSString *componentId) {
                            [self->_layoutManager removePendingViewController:weakNewVC];
                            [self->_eventEmitter sendOnNavigationCommandCompletion:showModal
                                                                         commandId:commandId];
                            [self emitStateChangedForCommand:showModal commandId:commandId];
                            completion(weakNewVC.layoutInfo.componentId);
                          }];
    }];
    [newVc render];
}

- (void)dismissModal:(NSString *)componentId
           commandId:(NSString *)commandId
        mergeOptions:(NSDictionary *)mergeOptions
          completion:(RNNTransitionWithComponentIdCompletionBlock)completion
           rejection:(RNNTransitionRejectionBlock)reject {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController *modalToDismiss =
        (UIViewController *)[_layoutManager findComponentForId:componentId];

    if (!modalToDismiss.isModal) {
        [RNNErrorHandler
                      reject:reject
               withErrorCode:1013
            errorDescription:[NSString stringWithFormat:@"component with id: '%@' is not a modal",
                                                        componentId]];
        return;
    }

    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
    [modalToDismiss.presentedComponentViewController mergeOptions:options];

    [_modalManager
        dismissModal:modalToDismiss
            animated:[modalToDismiss.resolveOptionsWithDefault.animations.dismissModal.exit.enable
                         withDefault:YES]
          completion:^{
            [self->_eventEmitter sendOnNavigationCommandCompletion:dismissModal
                                                         commandId:commandId];
            [self emitStateChangedForCommand:dismissModal commandId:commandId];
            completion(modalToDismiss.topMostViewController.layoutInfo.componentId);
          }];
}

- (void)dismissAllModals:(NSDictionary *)mergeOptions
               commandId:(NSString *)commandId
              completion:(RNNTransitionCompletionBlock)completion {
    [self assertReady];
    RNNAssertMainQueue();

    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
    [_modalManager
        dismissAllModalsAnimated:[options.animations.dismissModal.exit.enable withDefault:YES]
                      completion:^{
                        [self->_eventEmitter sendOnNavigationCommandCompletion:dismissAllModals
                                                                     commandId:commandId];
                        [self emitStateChangedForCommand:dismissAllModals commandId:commandId];
                        completion();
                      }];
}

- (void)showOverlay:(NSDictionary *)layout
          commandId:(NSString *)commandId
         completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController *overlayVC = [_controllerFactory createLayout:layout];
    [_layoutManager addPendingViewController:overlayVC];

    __weak UIViewController *weakOverlayVC = overlayVC;
    [overlayVC setReactViewReadyCallback:^{
      UIWindow *overlayWindow =
          [[RNNOverlayWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
      overlayWindow.rootViewController = weakOverlayVC;
      if ([weakOverlayVC.resolveOptionsWithDefault.overlay.handleKeyboardEvents withDefault:NO]) {
          [self->_overlayManager showOverlayWindowAsKeyWindow:overlayWindow];
      } else {
          [self->_overlayManager showOverlayWindow:overlayWindow];
      }

      [self->_layoutManager removePendingViewController:weakOverlayVC];
      [self->_eventEmitter sendOnNavigationCommandCompletion:showOverlay commandId:commandId];
      [self emitStateChangedForCommand:showOverlay commandId:commandId];
      completion(weakOverlayVC.layoutInfo.componentId);
    }];

    [overlayVC render];
}

- (void)dismissOverlay:(NSString *)componentId
             commandId:(NSString *)commandId
            completion:(RNNTransitionCompletionBlock)completion
             rejection:(RNNTransitionRejectionBlock)reject {
    [self assertReady];
    RNNAssertMainQueue();

    UIViewController *viewController = [_layoutManager findComponentForId:componentId];
    if (viewController) {
        [_overlayManager dismissOverlay:viewController];
        [_eventEmitter sendOnNavigationCommandCompletion:dismissOverlay commandId:commandId];
        [self emitStateChangedForCommand:dismissOverlay commandId:commandId];
        completion();
    } else {
        [RNNErrorHandler reject:reject
                  withErrorCode:1010
               errorDescription:@"ComponentId not found"];
    }
}

- (void)dismissAllOverlays:(NSString *)commandId {
    [self assertReady];
    RNNAssertMainQueue();

    [_overlayManager dismissAllOverlays];
    [self->_eventEmitter sendOnNavigationCommandCompletion:dismissAllOverlays commandId:commandId];
    [self emitStateChangedForCommand:dismissAllOverlays commandId:commandId];
}

- (void)emitStateChangedForCommand:(NSString *)commandName commandId:(NSString *)commandId {
    NSDictionary *state = [self getNavigationState];
    [_eventEmitter sendNavigationStateChanged:state commandName:commandName commandId:commandId];
}

- (NSDictionary *)getNavigationState {
    NSMutableDictionary *state = [NSMutableDictionary dictionary];

    UIViewController *rootVC = _mainWindow.rootViewController;
    if (rootVC && rootVC.layoutInfo) {
        state[@"root"] = [self serializeViewController:rootVC];
    } else {
        state[@"root"] = [NSNull null];
    }

    NSMutableArray *modals = [NSMutableArray array];
    UIViewController *presented = rootVC.presentedViewController;
    while (presented) {
        if (presented.layoutInfo) {
            [modals addObject:[self serializeViewController:presented]];
        }
        presented = presented.presentedViewController;
    }
    state[@"modals"] = modals;

    NSMutableArray *overlays = [NSMutableArray array];
    for (RNNOverlayWindow *window in _overlayManager.overlayWindows) {
        UIViewController *overlayVC = window.rootViewController;
        if (overlayVC && overlayVC.layoutInfo) {
            [overlays addObject:[self serializeViewController:overlayVC]];
        }
    }
    state[@"overlays"] = overlays;

    return state;
}

- (NSDictionary *)serializeViewController:(UIViewController *)vc {
    NSMutableDictionary *node = [NSMutableDictionary dictionary];
    node[@"id"] = vc.layoutInfo.componentId ?: @"";
    node[@"type"] = [self layoutTypeForViewController:vc];

    if (vc.layoutInfo.name) {
        node[@"name"] = vc.layoutInfo.name;
    }

    NSMutableArray *children = [NSMutableArray array];

    if ([vc isKindOfClass:[RNNStackController class]]) {
        RNNStackController *stack = (RNNStackController *)vc;
        for (UIViewController *child in stack.childViewControllers) {
            if (child.layoutInfo) {
                [children addObject:[self serializeViewController:child]];
            }
        }
    } else if ([vc isKindOfClass:[RNNBottomTabsController class]]) {
        RNNBottomTabsController *tabs = (RNNBottomTabsController *)vc;
        for (UIViewController *child in tabs.childViewControllers) {
            if (child.layoutInfo) {
                [children addObject:[self serializeViewController:child]];
            }
        }
        node[@"selectedIndex"] = @(tabs.selectedIndex);
    } else if ([vc isKindOfClass:[RNNSideMenuViewController class]]) {
        RNNSideMenuViewController *sideMenu = (RNNSideMenuViewController *)vc;
        if (sideMenu.left) {
            [children addObject:[self serializeViewController:sideMenu.left]];
        }
        if (sideMenu.center) {
            [children addObject:[self serializeViewController:sideMenu.center]];
        }
        if (sideMenu.right) {
            [children addObject:[self serializeViewController:sideMenu.right]];
        }
    } else if ([vc isKindOfClass:[RNNSideMenuChildViewController class]]) {
        RNNSideMenuChildViewController *sideMenuChild = (RNNSideMenuChildViewController *)vc;
        if (sideMenuChild.child) {
            [children addObject:[self serializeViewController:sideMenuChild.child]];
        }
    } else if ([vc isKindOfClass:[RNNSplitViewController class]]) {
        for (UIViewController *child in vc.childViewControllers) {
            if (child.layoutInfo) {
                [children addObject:[self serializeViewController:child]];
            }
        }
    } else if ([vc isKindOfClass:[RNNTopTabsViewController class]]) {
        for (UIViewController *child in vc.childViewControllers) {
            if (child.layoutInfo) {
                [children addObject:[self serializeViewController:child]];
            }
        }
    }

    node[@"children"] = children;
    return node;
}

- (NSString *)layoutTypeForViewController:(UIViewController *)vc {
    if ([vc isKindOfClass:[RNNBottomTabsController class]]) {
        return @"BottomTabs";
    } else if ([vc isKindOfClass:[RNNStackController class]]) {
        return @"Stack";
    } else if ([vc isKindOfClass:[RNNSideMenuViewController class]]) {
        return @"SideMenuRoot";
    } else if ([vc isKindOfClass:[RNNSideMenuChildViewController class]]) {
        RNNSideMenuChildViewController *sideMenuChild = (RNNSideMenuChildViewController *)vc;
        switch (sideMenuChild.type) {
            case RNNSideMenuChildTypeCenter:
                return @"SideMenuCenter";
            case RNNSideMenuChildTypeLeft:
                return @"SideMenuLeft";
            case RNNSideMenuChildTypeRight:
                return @"SideMenuRight";
        }
    } else if ([vc isKindOfClass:[RNNTopTabsViewController class]]) {
        return @"TopTabs";
    } else if ([vc isKindOfClass:[RNNSplitViewController class]]) {
        return @"SplitView";
    } else if ([vc isKindOfClass:[RNNExternalViewController class]]) {
        return @"ExternalComponent";
    }
    return @"Component";
}

#pragma mark - private

- (void)assertReady {
#ifndef RCT_NEW_ARCH_ENABLED
    if (!self.readyToReceiveCommands) {
        [[NSException exceptionWithName:@"BridgeNotLoadedError"
                                 reason:@"Bridge not yet loaded! Send commands after "
                                        @"Navigation.events().onAppLaunched() has been called."
                               userInfo:nil] raise];
    }
#endif
    return;
}

@end
