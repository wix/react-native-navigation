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
    RNNControllerFactory *_controllerFactory;
    RNNLayoutManager *_layoutManager;
    RNNModalManager *_modalManager;
    RNNOverlayManager *_overlayManager;
    RNNEventEmitter *_eventEmitter;
    UIWindow *_mainWindow;
    RNNSetRootAnimator *_setRootAnimator;
}

- (instancetype)initWithControllerFactory:(RNNControllerFactory *)controllerFactory
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
    vc.waitForRender = [optionsWithDefault.animations.setRoot.waitForRender withDefault:NO];

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
    if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] ||
        [vc isKindOfClass:[RNNComponentViewController class]]) {
        [CATransaction begin];
        [CATransaction setCompletionBlock:completion];

        [vc mergeOptions:newOptions];

        [CATransaction commit];
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

    completion();
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
              UIView *view = [[ReactNativeNavigation getBridge].uiManager
                  viewForReactTag:optionsWithDefault.preview.reactTag.get];
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
    newVC.waitForRender = ([options.animations.setStackRoot.waitForRender withDefault:NO]);

    __weak typeof(RNNEventEmitter *) weakEventEmitter = _eventEmitter;
    __weak UIViewController *weakNewVC = newVC;
    [newVC setReactViewReadyCallback:^{
      [fromVC.stack setStackChildren:childViewControllers
                  fromViewController:fromVC
                            animated:[options.animations.setStackRoot.enable withDefault:YES]
                          completion:^{
                            [self->_layoutManager removePendingViewController:weakNewVC];
                            [weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot
                                                                      commandId:commandId];
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
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
    [vc mergeOptions:options];

    if (vc) {
        [vc.stack popTo:vc
               animated:[vc.resolveOptionsWithDefault.animations.pop.enable withDefault:YES]
             completion:^(NSArray *poppedViewControllers) {
               [self->_eventEmitter sendOnNavigationCommandCompletion:popTo commandId:commandId];
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
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
    [vc mergeOptions:options];

    [CATransaction begin];
    [CATransaction setCompletionBlock:^{
      [self->_eventEmitter sendOnNavigationCommandCompletion:popToRoot commandId:commandId];
      completion();
    }];

    [vc.stack popToRoot:vc
               animated:[vc.resolveOptionsWithDefault.animations.pop.enable withDefault:YES]
             completion:^(NSArray *poppedViewControllers) {

             }
              rejection:^(NSString *code, NSString *message, NSError *error){

              }];

    [CATransaction commit];
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
}

#pragma mark - private

- (void)assertReady {
    if (!self.readyToReceiveCommands) {
        [[NSException exceptionWithName:@"BridgeNotLoadedError"
                                 reason:@"Bridge not yet loaded! Send commands after "
                                        @"Navigation.events().onAppLaunched() has been called."
                               userInfo:nil] raise];
    }
}

@end
