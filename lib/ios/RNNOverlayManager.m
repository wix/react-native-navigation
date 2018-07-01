#import "RNNOverlayManager.h"
#import "RNNErrorHandler.h"

@implementation RNNOverlayManager {
	NSMutableDictionary* _overlayDict;
	RNNStore* _store;
}

- (instancetype)initWithStore:(RNNStore *)store {
	self = [super init];
	_overlayDict = [[NSMutableDictionary alloc] init];
	_store = store;
	return self;
}

#pragma mark - public

- (void)showOverlay:(RNNRootViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	[self cacheOverlay:viewController];
	[[[UIApplication sharedApplication] keyWindow] addSubview:viewController.view];
	completion();
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject {
	RNNRootViewController* viewController = [_overlayDict objectForKey:componentId];
	if (viewController) {
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
	[viewController.view removeFromSuperview];
	[_overlayDict removeObjectForKey:viewController.componentId];
	[_store removeComponent:viewController.componentId];
}

@end
