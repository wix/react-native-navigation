#import "RNNOverlayManager.h"

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

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	RNNRootViewController* viewController = [_overlayDict objectForKey:componentId];
	[self removeCachedOverlay:viewController];
	completion();
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
