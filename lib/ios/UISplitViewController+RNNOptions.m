#import "UISplitViewController+RNNOptions.h"
#import "RNNSplitViewController.h"

@implementation UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode {
	if ([displayMode isEqualToString:@"visible"]) {
		[svc setPreferredDisplayMode:UISplitViewControllerDisplayModeAllVisible];
	} else if ([displayMode isEqualToString:@"hidden"]) {
		[svc setPreferredDisplayMode:UISplitViewControllerDisplayModePrimaryHidden];
	} else if ([displayMode isEqualToString:@"overlay"]) {
		[svc setPreferredDisplayMode:UISplitViewControllerDisplayModePrimaryOverlay];
	} else {
		[svc setPreferredDisplayMode:UISplitViewControllerDisplayModeAutomatic];
	}
}

- (void)rnn_setPrimaryEdge:(NSString *)primaryEdge {
	if (@available(iOS 11.0, *)) {
		if ([primaryEdge isEqualToString:@"trailing"]) {
			[svc setPrimaryEdge:UISplitViewControllerPrimaryEdgeTrailing];
		} else {
			[svc setPrimaryEdge:UISplitViewControllerPrimaryEdgeLeading];
		}
	}
}

- (void)rnn_setMinWidth:(NSNumber *)minWidth {
	[svc setMinimumPrimaryColumnWidth:[minWidth doubleValue]];
}

- (void)rnn_setMaxWidth:(NSNumber *)maxWidth {
	[svc setMaximumPrimaryColumnWidth:[maxWidth doubleValue]];
}

@end

