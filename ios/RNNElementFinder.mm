#import "RNNElementFinder.h"
#import "RNNReactView.h"
#import <React/RCTSurfaceView.h>
#import <React/RCTUIManager.h>
#import <React/RCTRootView.h>
#import <React/RCTUIManager.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#endif

@implementation RNNElementFinder

// TODO: Low performant search, improve later with using UIManager (investigate how to get it with Fabric)
#ifdef RCT_NEW_ARCH_ENABLED
+ (UIView *)findElementForId:(NSString *)elementId inView:(UIView *)view {
	auto nativeId = view.nativeID;
	auto casted = (RCTViewComponentView *)view;

	if (!nativeId && [casted respondsToSelector:@selector(nativeId)]) {
		nativeId = casted.nativeId;
	}

	if ([elementId isEqualToString:nativeId]) {
		return view;
	}

	if (view.subviews.count > 0) {
		for (UIView* subview in view.subviews) {
			UIView* found = [RNNElementFinder findElementForId:elementId inView:subview];

			if (found != nil) return found;
		}
	}

	return nil;
#else
+ (UIView *)findElementForId:(NSString *)elementId inView:(RCTRootView *)view {
	UIView *subView = [view.bridge.uiManager viewForNativeID:elementId withRootTag:view.reactTag];
	return subView;
#endif
}

@end
