#import "RNNCommandsHandler.h"
#import "RNNComponentViewController.h"
#import "RNNErrorHandler.h"
#import "RNNDefaultOptionsHelper.h"
#import "UIViewController+RNNOptions.h"
#import "React/RCTI18nUtil.h"
#import "UIViewController+LayoutProtocol.h"
#import "RNNLayoutManager.h"
#import "UIViewController+Utils.h"

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
	RNNNavigationStackManager* _stackManager;
	RNNEventEmitter* _eventEmitter;
	UIWindow* _mainWindow;
}

- (instancetype)initWithControllerFactory:(RNNControllerFactory*)controllerFactory eventEmitter:(RNNEventEmitter *)eventEmitter stackManager:(RNNNavigationStackManager *)stackManager modalManager:(RNNModalManager *)modalManager overlayManager:(RNNOverlayManager *)overlayManager mainWindow:(UIWindow *)mainWindow {
	self = [super init];
	_controllerFactory = controllerFactory;
	_eventEmitter = eventEmitter;
	_modalManager = modalManager;
	_modalManager.delegate = self;
	_stackManager = stackManager;
	_overlayManager = overlayManager;
	_mainWindow = mainWindow;
	return self;
}

#pragma mark - public

- (void)setRoot:(NSDictionary*)layout commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
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
	
	[_modalManager dismissAllModalsAnimated:NO];
	
	UIViewController *vc = [_controllerFactory createLayout:layout[@"root"]];
	
	[vc renderTreeAndWait:[vc.resolveOptions.animations.setRoot.waitForRender getWithDefaultValue:NO] perform:^{
		_mainWindow.rootViewController = vc;
		[_eventEmitter sendOnNavigationCommandCompletion:setRoot commandId:commandId params:@{@"layout": layout}];
		completion() ;
	}];
}

- (void)mergeOptions:(NSString*)componentId options:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNLayoutProtocol>* vc = [RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions* newOptions = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] || [vc isKindOfClass:[RNNComponentViewController class]]) {
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[vc overrideOptions:newOptions];
		[vc mergeOptions:newOptions];
		
		[CATransaction commit];
	}
}

- (void)setDefaultOptions:(NSDictionary*)optionsDict completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initWithDict:optionsDict];
	[_controllerFactory setDefaultOptions:defaultOptions];
	
	UIViewController *rootViewController = UIApplication.sharedApplication.delegate.window.rootViewController;
	[RNNDefaultOptionsHelper recrusivelySetDefaultOptions:defaultOptions onRootViewController:rootViewController];

	completion();
}

- (void)push:(NSString*)componentId commandId:(NSString*)commandId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayout:layout];
	UIViewController *fromVC = [RNNLayoutManager findComponentForId:componentId];
	
	if ([[newVc.resolveOptions.preview.reactTag getWithDefaultValue:@(0)] floatValue] > 0) {
		UIViewController* vc = [RNNLayoutManager findComponentForId:componentId];
		
		if([vc isKindOfClass:[RNNComponentViewController class]]) {
			RNNComponentViewController* rootVc = (RNNComponentViewController*)vc;
			rootVc.previewController = newVc;
			[newVc renderTreeAndWait:NO perform:nil];
			
			rootVc.previewCallback = ^(UIViewController *vcc) {
				RNNComponentViewController* rvc  = (RNNComponentViewController*)vcc;
				[self->_eventEmitter sendOnPreviewCompleted:componentId previewComponentId:newVc.layoutInfo.componentId];
				if ([newVc.resolveOptions.preview.commit getWithDefaultValue:NO]) {
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
			
			if (newVc.resolveOptions.preview.width.hasValue) {
				size.width = [newVc.resolveOptions.preview.width.get floatValue];
			}
			
			if (newVc.resolveOptions.preview.height.hasValue) {
				size.height = [newVc.resolveOptions.preview.height.get floatValue];
			}
			
			if (newVc.resolveOptions.preview.width.hasValue || newVc.resolveOptions.preview.height.hasValue) {
				newVc.preferredContentSize = size;
			}
			
			RCTExecuteOnMainQueue(^{
				UIView *view = [[ReactNativeNavigation getBridge].uiManager viewForReactTag:newVc.resolveOptions.preview.reactTag.get];
				[rootVc registerForPreviewingWithDelegate:(id)rootVc sourceView:view];
			});
		}
	} else {
		id animationDelegate = (newVc.resolveOptions.animations.push.hasCustomAnimation || newVc.resolveOptions.customTransition.animations) ? newVc : nil;
		[newVc renderTreeAndWait:([newVc.resolveOptions.animations.push.waitForRender getWithDefaultValue:NO] || animationDelegate) perform:^{
			[_stackManager push:newVc onTop:fromVC animated:[newVc.resolveOptions.animations.push.enable getWithDefaultValue:YES] animationDelegate:animationDelegate completion:^{
				[_eventEmitter sendOnNavigationCommandCompletion:push commandId:commandId params:@{@"componentId": componentId}];
				completion();
			} rejection:rejection];
		}];
	}
}

- (void)setStackRoot:(NSString*)componentId commandId:(NSString*)commandId children:(NSArray*)children completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	NSArray<UIViewController *> *childViewControllers = [_controllerFactory createChildrenLayout:children];
	for (UIViewController<RNNLayoutProtocol>* viewController in childViewControllers) {
		if (![viewController isEqual:childViewControllers.lastObject]) {
			[viewController renderTreeAndWait:NO perform:nil];
		}
	}
	UIViewController *newVC = childViewControllers.lastObject;
	UIViewController *fromVC = [RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions* options = newVC.resolveOptions;
	__weak typeof(RNNEventEmitter*) weakEventEmitter = _eventEmitter;

	[newVC renderTreeAndWait:([options.animations.setStackRoot.waitForRender getWithDefaultValue:NO]) perform:^{
		[_stackManager setStackChildren:childViewControllers fromViewController:fromVC animated:[options.animations.setStackRoot.enable getWithDefaultValue:YES] completion:^{
			[weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot commandId:commandId params:@{@"componentId": componentId}];
			completion();
		} rejection:rejection];
	}]; 
}

- (void)pop:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc overrideOptions:options];
	
	UINavigationController *nvc = vc.navigationController;
	
	if ([nvc topViewController] == vc) {
		if (vc.resolveOptions.animations.pop) {
			nvc.delegate = vc;
		} else {
			nvc.delegate = nil;
		}
	} else {
		NSMutableArray * vcs = nvc.viewControllers.mutableCopy;
		[vcs removeObject:vc];
		[nvc setViewControllers:vcs animated:[vc.resolveOptions.animations.pop.enable getWithDefaultValue:YES]];
	}
	
	[_stackManager pop:vc animated:[vc.resolveOptions.animations.pop.enable getWithDefaultValue:YES] completion:^{
		[_eventEmitter sendOnNavigationCommandCompletion:pop commandId:commandId params:@{@"componentId": componentId}];
		completion();
	} rejection:rejection];
}

- (void)popTo:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc overrideOptions:options];
	
	[_stackManager popTo:vc animated:[vc.resolveOptions.animations.pop.enable getWithDefaultValue:YES] completion:^(NSArray *poppedViewControllers) {
		[_eventEmitter sendOnNavigationCommandCompletion:popTo commandId:commandId params:@{@"componentId": componentId}];
		completion();
	} rejection:rejection];
}

- (void)popToRoot:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	RNNComponentViewController *vc = (RNNComponentViewController*)[RNNLayoutManager findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc overrideOptions:options];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:popToRoot commandId:commandId params:@{@"componentId": componentId}];
		completion();
	}];
	
	[_stackManager popToRoot:vc animated:[vc.resolveOptions.animations.pop.enable getWithDefaultValue:YES] completion:^(NSArray *poppedViewControllers) {
		
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	
	[CATransaction commit];
}

- (void)showModal:(NSDictionary*)layout commandId:(NSString *)commandId completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayout:layout];
	
	[newVc renderTreeAndWait:[newVc.resolveOptions.animations.showModal.waitForRender getWithDefaultValue:NO] perform:^{
		[_modalManager showModal:newVc animated:[newVc.resolveOptions.animations.showModal.enable getWithDefaultValue:YES] hasCustomAnimation:newVc.resolveOptions.animations.showModal.hasCustomAnimation completion:^(NSString *componentId) {
			[_eventEmitter sendOnNavigationCommandCompletion:showModal commandId:commandId params:@{@"layout": layout}];
			completion(newVc.layoutInfo.componentId);
		}];
	}];
}

- (void)dismissModal:(NSString*)componentId commandId:(NSString*)commandId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	[self assertReady];
	
	UIViewController *modalToDismiss = (UIViewController *)[RNNLayoutManager findComponentForId:componentId];
	
	if (!modalToDismiss.isModal) {
		[RNNErrorHandler reject:reject withErrorCode:1013 errorDescription:@"component is not a modal"];
		return;
	}
	
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[modalToDismiss.getCurrentChild overrideOptions:options];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissModal commandId:commandId params:@{@"componentId": componentId}];
	}];
	
	[_modalManager dismissModal:modalToDismiss completion:completion];
	
	[CATransaction commit];
}

- (void)dismissAllModals:(NSDictionary *)mergeOptions commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissAllModals commandId:commandId params:@{}];
		completion();
	}];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[_modalManager dismissAllModalsAnimated:[options.animations.dismissModal.enable getWithDefaultValue:YES]];
	
	[CATransaction commit];
}

- (void)showOverlay:(NSDictionary *)layout commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController* overlayVC = [_controllerFactory createLayout:layout];
	[overlayVC renderTreeAndWait:NO perform:^{
		UIWindow* overlayWindow = [[RNNOverlayWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
		overlayWindow.rootViewController = overlayVC;
		if ([overlayVC.resolveOptions.overlay.handleKeyboardEvents getWithDefaultValue:NO]) {
			[_overlayManager showOverlayWindowAsKeyWindow:overlayWindow];
		} else {
			[_overlayManager showOverlayWindow:overlayWindow];
		}
		
		[_eventEmitter sendOnNavigationCommandCompletion:showOverlay commandId:commandId params:@{@"layout": layout}];
		completion();
	}];
}

- (void)dismissOverlay:(NSString*)componentId commandId:(NSString*)commandId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	[self assertReady];
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
	[_eventEmitter sendModalsDismissedEvent:viewController.layoutInfo.componentId numberOfModalsDismissed:@(1)];
}

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	if (viewControllers && viewControllers.count) {
		[_eventEmitter sendModalsDismissedEvent:((UIViewController *)viewControllers.lastObject).layoutInfo.componentId numberOfModalsDismissed:@(viewControllers.count)];
	}
}

@end
