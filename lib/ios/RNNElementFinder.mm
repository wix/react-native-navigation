#import "RNNElementFinder.h"
#import "RNNReactView.h"
#import <React/RCTSurfaceView.h>
#import <React/RCTUIManager.h>
#import <React/RCTRootView.h>
#import <React/RCTUIManager.h>

@implementation RNNElementFinder

// TODO: Low performant search, OK for test, improve later after investigation
#ifdef RCT_NEW_ARCH_ENABLED
+ (UIView *)findElementForId:(NSString *)elementId inView:(UIView *)view {
    if (view.nativeID == elementId) {
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
