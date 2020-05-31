#import "RNNCommandsHandler.h"
#import "RNNComponentViewController.h"
#import "RNNErrorHandler.h"
#import "RNNDefaultOptionsHelper.h"
#import "UIViewController+RNNOptions.h"
#import "React/RCTI18nUtil.h"
#import "UIViewController+LayoutProtocol.h"
#import "RNNLayoutManager.h"
#import "UIViewController+Utils.h"
#import "UINavigationController+RNNCommands.h"
#import "RNNAssert.h"

static NSString* const setRoot	= @"setRoot";
static NSString* const setStackRoot	= @"setStackRoot";
static NSString* const push	= @"push";
static NSString* const preview	= @"preview";
static NSString* const pop	= @"pop";
static NSString* const popTo	= @"popTo";
static NSString* const popToRoot	= @"popToRoot";
static NSString* const showModal	= @"showModal";
static NSString* const dismissModal	= @"dismissModal";
static NSString* const dismissAllModals	= @"dismissAllModals";
static NSString* const showOverlay	= @"showOverlay";
static NSString* const dismissOverlay	= @"dismissOverlay";
static NSString* const mergeOptions	= @"mergeOptions";
static NSString* const setDefaultOptions	= @"setDefaultOptions";

@interface RNNCommandsHandler() <RNNModalManagerDelegate>

@end

@implementation RNNCommandsHandler {
	RNNControllerFactory *_controllerFactory;
	RNNModalManager* _modalManager;
	RNNOverlayManager* _overlayManager;
	RNNEventEmitter* _eventEmitter;
	UIWindow* _mainWindow;
}

- (instancetype)initWithControllerFactory:(RNNControllerFactory*)controllerFactory eventEmitter:(RNNEventEmitter *)eventEmitter  modalManager:(RNNModalManager *)modalManager overlayManager:(RNNOverlayManager *)overlayManager mainWindow:(UIWindow *)mainWindow {
	self = [super init];
	_controllerFactory = controllerFactory;
	_eventEmitter = eventEmitter;
	_modalManager = modalManager;
	_modalManager.delegate = self;
	_overlayManager = overlayManager;
	_mainWindow = mainWindow;
	return self;
}

#pragma mark - public

- (void)setRoot:(NSDictionary*)layout commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
	
	if (@available(iOS 9, *)) {
		if(_controllerFactory.defaultOptions.layout.direction.hasValue) {
			if ([_controllerFactory.defaultOptions.layout.direction.get isEqualToString:@"rtl"]) {
				[[RCTI18nUtil sharedInstance] allowRTL:YES];
				[[RCTI18nUtil sharedInstance] forceRTL:YES];
				[[UIView appearance] setSemanticContentAttribute:UISemanticContentAttributeForceRightToLeft];
				[[UINavigationBar appearance] setSemanticContentAttribute:UISemanticContentAttributeForceRightToLeft];
			} else {
				[[RCTI18nUtil sharedInstance] allowRTL:NO];
				[[RCTI18nUtil sharedInstance] forceRTL:NO];
				[[UIView appearance] setSemanticContentAttribute:UISemanticContentAttributeForceLeftToRight];
				[[UINavigationBar appearance] setSemanticContentAttribute:UISemanticContentAttributeForceLeftToRight];
			}
		}
	}
	
	[_modalManager dismissAllModalsAnimated:NO completion:nil];
    
    UIViewController *vc = [_controllerFactory createLayout:layout[@"root"]];
    vc.waitForRender = [vc.resolveOptionsWithDefault.animations.setRoot.waitForRender getWithDefaultValue:NO];
    __weak UIViewController* weakVC = vc;
    [vc setReactViewReadyCallback:^{
        [self->_mainWindow.rootViewController destroy];
        self->_mainWindow.rootViewController = weakVC;
        [self->_eventEmitter sendOnNavigationCommandCompletion:setRoot commandId:commandId params:@{@"layout": layout}];
        completion();
    }];
    
    [vc render];
}

- (void)mergeOptions:(NSString*)componentId options:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
	
	UIViewController<RNNLayoutProtocol>* vc = [RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions* newOptions = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] || [vc isKindOfClass:[RNNComponentViewController class]]) {
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[vc mergeOptions:newOptions];
		
		[CATransaction commit];
	}
}

- (void)setDefaultOptions:(NSDictionary*)optionsDict completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
    
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initWithDict:optionsDict];
	[_controllerFactory setDefaultOptions:defaultOptions];
	
	UIViewController *rootViewController = UIApplication.sharedApplication.delegate.window.rootViewController;
	[RNNDefaultOptionsHelper recrusivelySetDefaultOptions:defaultOptions onRootViewController:rootViewController];

	completion();
}

- (void)push:(NSString*)componentId commandId:(NSString*)commandId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
    RNNAssertMainQueue();
	
	UIViewController *newVc = [_controllerFactory createLayout:layout];
	UIViewController *fromVC = [RNNLayoutManager findComponentForId:componentId];
	
	if ([[newVc.resolveOptionsWithDefault.preview.reactTag getWithDefaultValue:@(0)] floatValue] > 0) {
		if ([fromVC isKindOfClass:[RNNComponentViewController class]]) {
			RNNComponentViewController* rootVc = (RNNComponentViewController*)fromVC;
			rootVc.previewController = newVc;
			[newVc render];
			
			rootVc.previewCallback = ^(UIViewController *vcc) {
				RNNComponentViewController* rvc  = (RNNComponentViewController*)vcc;
				[self->_eventEmitter sendOnPreviewCompleted:componentId previewComponentId:newVc.layoutInfo.componentId];
				if ([newVc.resolveOptionsWithDefault.preview.commit getWithDefaultValue:NO]) {
					[CATransaction begin];
					[CATransaction setCompletionBlock:^{
						[self->_eventEmitter sendOnNavigationCommandCompletion:push commandId:commandId params:@{@"componentId": componentId}];
						completion();
					}];
					[rvc.navigationController pushViewController:newVc animated:YES];
					[CATransaction commit];
				}
			};
			
			CGSize size = CGSizeMake(rootVc.view.frame.size.width, rootVc.view.frame.size.height);
			
			if (newVc.resolveOptionsWithDefault.preview.width.hasValue) {
				size.width = [newVc.resolveOptionsWithDefault.preview.width.get floatValue];
			}
			
			if (newVc.resolveOptionsWithDefault.preview.height.hasValue) {
				size.height = [newVc.resolveOptionsWithDefault.preview.height.get floatValue];
			}
			
			if (newVc.resolveOptionsWithDefault.preview.width.hasValue || newVc.resolveOptionsWithDefault.preview.height.hasValue) {
				newVc.preferredContentSize = size;
			}
			RCTExecuteOnMainQueue(^{
				UIView *view = [[ReactNativeNavigation getBridge].uiManager viewForReactTag:newVc.resolveOptionsWithDefault.preview.reactTag.get];
				[rootVc registerForPreviewingWithDelegate:(id)rootVc sourceView:view];
			});
		}
	} else {
        newVc.waitForRender = newVc.resolveOptionsWithDefault.animations.push.shouldWaitForRender;
        __weak UIViewController* weakNewVC = newVc;
        [newVc setReactViewReadyCallback:^{
            [fromVC.stack push:weakNewVC onTop:fromVC animated:[weakNewVC.resolveOptionsWithDefault.animations.push.enable getWithDefaultValue:YES] completion:^{
                [self->_eventEmitter sendOnNavigationCommandCompletion:push commandId:commandId params:@{@"componentId": componentId}];
                completion();
            } rejection:rejection];
        }];
        
        [newVc render];
	}
}

- (void)setStackRoot:(NSString*)componentId commandId:(NSString*)commandId children:(NSArray*)children completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
    RNNAssertMainQueue();
	
	NSArray<UIViewController *> *childViewControllers = [_controllerFactory createChildrenLayout:children];
	for (UIViewController<RNNLayoutProtocol>* viewController in childViewControllers) {
		if (![viewController isEqual:childViewControllers.lastObject]) {
			[viewController render];
		}
	}
	UIViewController *newVC = childViewControllers.lastObject;
	UIViewController *fromVC = [RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions* options = newVC.resolveOptionsWithDefault;
	__weak typeof(RNNEventEmitter*) weakEventEmitter = _eventEmitter;

    newVC.waitForRender = ([options.animations.setStackRoot.waitForRender getWithDefaultValue:NO]);
    [newVC setReactViewReadyCallback:^{
        [fromVC.stack setStackChildren:childViewControllers fromViewController:fromVC animated:[options.animations.setStackRoot.enable getWithDefaultValue:YES] completion:^{
            [weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot commandId:commandId params:@{@"componentId": componentId}];
            completion();
        } rejection:rejection];
    }];

    [newVC render];
}

- (void)pop:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	RNNAssertMainQueue();
    
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
  if (vc) {
      RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
      [vc overrideOptions:options];

      [vc.stack pop:vc animated:[vc.resolveOptionsWithDefault.animations.pop.enable getWithDefaultValue:YES] completion:^{
          [self->_eventEmitter sendOnNavigationCommandCompletion:pop commandId:commandId params:@{@"componentId": componentId}];
          completion();
      } rejection:rejection];
  } else {
      [RNNErrorHandler reject:rejection withErrorCode:1012 errorDescription:[NSString stringWithFormat:@"Popping component failed - componentId '%@' not found", componentId]];
  }
}

- (void)popTo:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
    RNNAssertMainQueue();
    
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc overrideOptions:options];
	
	[vc.stack popTo:vc animated:[vc.resolveOptionsWithDefault.animations.pop.enable getWithDefaultValue:YES] completion:^(NSArray *poppedViewControllers) {
		[self->_eventEmitter sendOnNavigationCommandCompletion:popTo commandId:commandId params:@{@"componentId": componentId}];
		completion();
	} rejection:rejection];
}

- (void)popToRoot:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
    RNNAssertMainQueue();
    
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc overrideOptions:options];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[self->_eventEmitter sendOnNavigationCommandCompletion:popToRoot commandId:commandId params:@{@"componentId": componentId}];
		completion();
	}];
	
	[vc.stack popToRoot:vc animated:[vc.resolveOptionsWithDefault.animations.pop.enable getWithDefaultValue:YES] completion:^(NSArray *poppedViewControllers) {
		
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	
	[CATransaction commit];
}

- (void)showModal:(NSDictionary*)layout commandId:(NSString *)commandId completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
	
	UIViewController *newVc = [_controllerFactory createLayout:layout];
    __weak UIViewController* weakNewVC = newVc;
    newVc.waitForRender = [newVc.resolveOptionsWithDefault.animations.showModal.waitForRender getWithDefaultValue:NO];
    [newVc setReactViewReadyCallback:^{
        [self->_modalManager showModal:weakNewVC animated:[weakNewVC.resolveOptionsWithDefault.animations.showModal.enable getWithDefaultValue:YES] completion:^(NSString *componentId) {
            [self->_eventEmitter sendOnNavigationCommandCompletion:showModal commandId:commandId params:@{@"layout": layout}];
            completion(weakNewVC.layoutInfo.componentId);
        }];
    }];
	[newVc render];
}

- (void)dismissModal:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	[self assertReady];
    RNNAssertMainQueue();
	
	UIViewController *modalToDismiss = (UIViewController *)[RNNLayoutManager findComponentForId:componentId];
	
	if (!modalToDismiss.isModal) {
		[RNNErrorHandler reject:reject withErrorCode:1013 errorDescription:@"component is not a modal"];
		return;
	}
	
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[modalToDismiss.getCurrentChild overrideOptions:options];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
        [self->_eventEmitter sendOnNavigationCommandCompletion:dismissModal commandId:commandId params:@{@"componentId": componentId}];
	}];
	
	[_modalManager dismissModal:modalToDismiss completion:completion];
	
	[CATransaction commit];
}

- (void)dismissAllModals:(NSDictionary *)mergeOptions commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[self->_eventEmitter sendOnNavigationCommandCompletion:dismissAllModals commandId:commandId params:@{}];
		completion();
	}];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[_modalManager dismissAllModalsAnimated:[options.animations.dismissModal.enable getWithDefaultValue:YES] completion:nil];
	
	[CATransaction commit];
}

- (void)showOverlay:(NSDictionary *)layout commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
    RNNAssertMainQueue();
    
	UIViewController* overlayVC = [_controllerFactory createLayout:layout];
    __weak UIViewController* weakOverlayVC = overlayVC;
    [overlayVC setReactViewReadyCallback:^{UIWindow* overlayWindow = [[RNNOverlayWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
        overlayWindow.rootViewController = weakOverlayVC;
        if ([weakOverlayVC.resolveOptionsWithDefault.overlay.handleKeyboardEvents getWithDefaultValue:NO]) {
            [self->_overlayManager showOverlayWindowAsKeyWindow:overlayWindow];
        } else {
            [self->_overlayManager showOverlayWindow:overlayWindow];
        }
        
        [self->_eventEmitter sendOnNavigationCommandCompletion:showOverlay commandId:commandId params:@{@"layout": layout}];
        completion();
        
    }];
    
    [overlayVC render];
}

- (void)dismissOverlay:(NSString*)componentId commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	[self assertReady];
    RNNAssertMainQueue();
    
	UIViewController* viewController = [RNNLayoutManager findComponentForId:componentId];
	if (viewController) {
		[_overlayManager dismissOverlay:viewController];
		[_eventEmitter sendOnNavigationCommandCompletion:dismissOverlay commandId:commandId params:@{@"componentId": componentId}];
		completion();
	} else {
		[RNNErrorHandler reject:reject withErrorCode:1010 errorDescription:@"ComponentId not found"];
	}
}

#pragma mark - private

- (void)assertReady {
	if (!self.readyToReceiveCommands) {
		[[NSException exceptionWithName:@"BridgeNotLoadedError"
								 reason:@"Bridge not yet loaded! Send commands after Navigation.events().onAppLaunched() has been called."
							   userInfo:nil]
		 raise];
	}
}

#pragma mark - RNNModalManagerDelegate

- (void)dismissedModal:(UIViewController *)viewController {
	[_eventEmitter sendModalsDismissedEvent:viewController.layoutInfo.componentId componentName:viewController.layoutInfo.name numberOfModalsDismissed:@(1)];
}

- (void)attemptedToDismissModal:(UIViewController *)viewController {
    [_eventEmitter sendModalAttemptedToDismissEvent:viewController.layoutInfo.componentId];
}

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	if (viewControllers && viewControllers.count) {
		UIViewController* lastViewController = [viewControllers.lastObject presentedComponentViewController];
        [_eventEmitter sendModalsDismissedEvent:lastViewController.layoutInfo.componentId componentName:lastViewController.layoutInfo.name numberOfModalsDismissed:@(viewControllers.count)];
	}
}

@end
