#import "RNNOverlayManager.h"
#import "RNNErrorHandler.h"
#import "RNNOverlayWindow.h"

@implementation RNNOverlayManager {
	NSMutableDictionary* _overlayDict;
	NSMutableDictionary* _overlayWindows;
	RNNStore* _store;
}

- (instancetype)initWithStore:(RNNStore *)store {
	self = [super init];
	_overlayDict = [[NSMutableDictionary alloc] init];
	_overlayWindows = [[NSMutableDictionary alloc] init];
	_store = store;
	return self;
}

#pragma mark - public

- (void)showOverlay:(RNNRootViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	[self cacheOverlay:viewController];
	UIWindow* overlayWindow = [[RNNOverlayWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
	[_overlayWindows setObject:overlayWindow forKey:viewController.componentId];
	[overlayWindow setWindowLevel:UIWindowLevelNormal];
	[overlayWindow setRootViewController:viewController];
	[overlayWindow setHidden:NO];
	
	completion();
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	RNNRootViewController* viewController = [_overlayDict objectForKey:componentId];
	if (viewController) {
		[self detachOverlayWindow:componentId];
		[self removeCachedOverlay:viewController];
		completion();
	} else {
		[RNNErrorHandler reject:reject withErrorCode:1010 errorDescription:@"ComponentId not found"];
	}
}

#pragma mark - private

- (void)cacheOverlay:(RNNRootViewController*)viewController {
	[_overlayDict setObject:viewController forKey:viewController.componentId];
}

- (void)removeCachedOverlay:(RNNRootViewController*)viewController {
	[_overlayDict removeObjectForKey:viewController.componentId];
	[_store removeComponent:viewController.componentId];
}

- (void)detachOverlayWindow:(NSString *)componentId {
	UIWindow* overlayWindow = [_overlayWindows objectForKey:componentId];
	[overlayWindow setHidden:YES];
	[overlayWindow setRootViewController:nil];
	overlayWindow = nil;
}

@end
