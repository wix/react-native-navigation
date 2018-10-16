#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNOverlayManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNElementFinder.h"
#import "React/RCTUIManager.h"
#import "RNNErrorHandler.h"
#import "RNNDefaultOptionsHelper.h"

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
	RNNStore *_store;
	RNNModalManager* _modalManager;
	RNNOverlayManager* _overlayManager;
	RNNNavigationStackManager* _stackManager;
	RNNEventEmitter* _eventEmitter;
}

- (instancetype)initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [super init];
	_store = store;
	_controllerFactory = controllerFactory;
	_eventEmitter = eventEmitter;
	_modalManager = [[RNNModalManager alloc] init];
	_modalManager.delegate = self;
	_stackManager = [[RNNNavigationStackManager alloc] init];
	_overlayManager = [[RNNOverlayManager alloc] init];
	return self;
}

#pragma mark - public

- (void)setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[_modalManager dismissAllModalsAnimated:NO];
	
	UIViewController *vc = [_controllerFactory createLayoutAndSaveToStore:layout[@"root"]];
	
	UIApplication.sharedApplication.delegate.window.rootViewController = vc;
	[UIApplication.sharedApplication.delegate.window makeKeyWindow];
	[_eventEmitter sendOnNavigationCommandCompletion:setRoot params:@{@"layout": layout}];
	completion();
}

- (void)mergeOptions:(NSString*)componentId options:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNLayoutProtocol>* vc = (UIViewController<RNNLayoutProtocol>*)[_store findComponentForId:componentId];
	RNNNavigationOptions* newOptions = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] || [vc isKindOfClass:[RNNRootViewController class]]) {
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[vc.options overrideOptions:newOptions];
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

- (void)push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	RNNRootViewController *newVc = (RNNRootViewController *)[_controllerFactory createLayoutAndSaveToStore:layout];
	UIViewController *fromVC = [_store findComponentForId:componentId];
	
	if ([newVc.options.preview.reactTag floatValue] > 0) {
		UIViewController* vc = [_store findComponentForId:componentId];
		
		if([vc isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController* rootVc = (RNNRootViewController*)vc;
			rootVc.previewController = newVc;

      rootVc.previewCallback = ^(UIViewController *vcc) {
				RNNRootViewController* rvc  = (RNNRootViewController*)vcc;
				[self->_eventEmitter sendOnPreviewCompleted:componentId previewComponentId:newVc.layoutInfo.componentId];
				if ([newVc.options.preview.commit floatValue] > 0) {
					[CATransaction begin];
					[CATransaction setCompletionBlock:^{
						[self->_eventEmitter sendOnNavigationCommandCompletion:push params:@{@"componentId": componentId}];
						completion();
					}];
					[rvc.navigationController pushViewController:newVc animated:YES];
					[CATransaction commit];
				}
			};
			
			CGSize size = CGSizeMake(rootVc.view.frame.size.width, rootVc.view.frame.size.height);
			
			if (newVc.options.preview.width) {
				size.width = [newVc.options.preview.width floatValue];
			}
			
			if (newVc.options.preview.height) {
				size.height = [newVc.options.preview.height floatValue];
			}
			
			if (newVc.options.preview.width || newVc.options.preview.height) {
				newVc.preferredContentSize = size;
			}
      
			RCTExecuteOnMainQueue(^{
				UIView *view = [[ReactNativeNavigation getBridge].uiManager viewForReactTag:newVc.options.preview.reactTag];
				[rootVc registerForPreviewingWithDelegate:(id)rootVc sourceView:view];
			});
		}
	} else {
		id animationDelegate = (newVc.options.animations.push.hasCustomAnimation || newVc.isCustomTransitioned) ? newVc : nil;
		[newVc waitForReactViewRender:(newVc.options.animations.push.waitForRender || animationDelegate) perform:^{
			[_stackManager push:newVc onTop:fromVC animated:newVc.options.animations.push.enable animationDelegate:animationDelegate completion:^{
				[_eventEmitter sendOnNavigationCommandCompletion:push params:@{@"componentId": componentId}];
				completion();
			} rejection:rejection];
		}];
	}
}

- (void)setStackRoot:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	UIViewController<RNNParentProtocol> *newVC = [_controllerFactory createLayoutAndSaveToStore:layout];
	RNNNavigationOptions* options = [newVC getCurrentChild].options;
	UIViewController *fromVC = [_store findComponentForId:componentId];
	__weak typeof(RNNEventEmitter*) weakEventEmitter = _eventEmitter;
	[_stackManager setStackRoot:newVC fromViewController:fromVC animated:options.animations.setStackRoot.enable completion:^{
		[weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot params:@{@"componentId": componentId}];
		completion();
	} rejection:rejection];
}

- (void)pop:(NSString*)componentId mergeOptions:(NSDictionary*)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc.options overrideOptions:options];
	
	UINavigationController *nvc = vc.navigationController;
	
	if ([nvc topViewController] == vc) {
		if (vc.options.animations.pop) {
			nvc.delegate = vc;
		} else {
			nvc.delegate = nil;
		}
	} else {
		NSMutableArray * vcs = nvc.viewControllers.mutableCopy;
		[vcs removeObject:vc];
		[nvc setViewControllers:vcs animated:vc.options.animations.pop.enable];
	}
	
	[_stackManager pop:vc animated:vc.options.animations.pop.enable completion:^{
		[_store removeComponent:componentId];
		[_eventEmitter sendOnNavigationCommandCompletion:pop params:@{@"componentId": componentId}];
		completion();
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
}

- (void)popTo:(NSString*)componentId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc.options overrideOptions:options];
	
	[_stackManager popTo:vc animated:vc.options.animations.pop.enable completion:^(NSArray *poppedViewControllers) {
		[_eventEmitter sendOnNavigationCommandCompletion:popTo params:@{@"componentId": componentId}];
		[self removePopedViewControllers:poppedViewControllers];
		completion();
	} rejection:rejection];
}

- (void)popToRoot:(NSString*)componentId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[vc.options overrideOptions:options];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:popToRoot params:@{@"componentId": componentId}];
		completion();
	}];
	
	[_stackManager popToRoot:vc animated:vc.options.animations.pop.enable completion:^(NSArray *poppedViewControllers) {
		[self removePopedViewControllers:poppedViewControllers];
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	
	[CATransaction commit];
}

- (void)showModal:(NSDictionary*)layout completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNParentProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	
	[newVc.getCurrentChild waitForReactViewRender:newVc.getCurrentChild.options.animations.showModal.waitForRender perform:^{
		[_modalManager showModal:newVc animated:newVc.getCurrentChild.options.animations.showModal.enable hasCustomAnimation:newVc.getCurrentChild.options.animations.showModal.hasCustomAnimation completion:^(NSString *componentId) {
			[_eventEmitter sendOnNavigationCommandCompletion:showModal params:@{@"layout": layout}];
			completion(componentId);
		}];
	}];
}

- (void)dismissModal:(NSString*)componentId mergeOptions:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissModal params:@{@"componentId": componentId}];
	}];
	UIViewController<RNNParentProtocol> *modalToDismiss = (UIViewController<RNNParentProtocol>*)[_store findComponentForId:componentId];
	RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[modalToDismiss.getCurrentChild.options overrideOptions:options];
	
	[self removePopedViewControllers:modalToDismiss.navigationController.viewControllers];
	
	[_modalManager dismissModal:modalToDismiss completion:completion];
	
	[CATransaction commit];
}

- (void)dismissAllModals:(NSDictionary *)mergeOptions completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissAllModals params:@{}];
		completion();
	}];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:mergeOptions];
	[_modalManager dismissAllModalsAnimated:options.animations.dismissModal.enable];
	
	[CATransaction commit];
}

- (void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNParentProtocol>* overlayVC = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_overlayManager showOverlay:overlayVC];
	[_eventEmitter sendOnNavigationCommandCompletion:showOverlay params:@{@"layout": layout}];
	completion();
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	[self assertReady];
	UIViewController* viewController = [_store findComponentForId:componentId];
	if (viewController) {
		[_overlayManager dismissOverlay:viewController];
		[_eventEmitter sendOnNavigationCommandCompletion:dismissOverlay params:@{@"componentId": componentId}];
		completion();
	} else {
		[RNNErrorHandler reject:reject withErrorCode:1010 errorDescription:@"ComponentId not found"];
	}
}

#pragma mark - private

- (void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeComponentByViewControllerInstance:popedVC];
	}
}

- (void)assertReady {
	if (!_store.isReadyToReceiveCommands) {
		[[NSException exceptionWithName:@"BridgeNotLoadedError"
								 reason:@"Bridge not yet loaded! Send commands after Navigation.events().onAppLaunched() has been called."
							   userInfo:nil]
		 raise];
	}
}

#pragma mark - RNNModalManagerDelegate

- (void)dismissedModal:(UIViewController<RNNParentProtocol> *)viewController {
	[_eventEmitter sendModalsDismissedEvent:viewController.layoutInfo.componentId numberOfModalsDismissed:@(1)];
}

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	if (viewControllers && viewControllers.count) {
		[_eventEmitter sendModalsDismissedEvent:((UIViewController<RNNParentProtocol> *)viewControllers.lastObject).layoutInfo.componentId numberOfModalsDismissed:@(viewControllers.count)];
	}
}

@end
