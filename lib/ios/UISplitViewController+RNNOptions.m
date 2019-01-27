#import "UISplitViewController+RNNOptions.h"
#import "RNNSplitViewController.h"

@implementation UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode {
	if ([displayMode isEqualToString:@"visible"]) {
		[self.splitViewController setPreferredDisplayMode:UISplitViewControllerDisplayModeAllVisible];
	} else if ([displayMode isEqualToString:@"hidden"]) {
		[self.splitViewController setPreferredDisplayMode:UISplitViewControllerDisplayModePrimaryHidden];
	} else if ([displayMode isEqualToString:@"overlay"]) {
		[self.splitViewController setPreferredDisplayMode:UISplitViewControllerDisplayModePrimaryOverlay];
	} else {
		[self.splitViewController setPreferredDisplayMode:UISplitViewControllerDisplayModeAutomatic];
	}
}

- (void)rnn_setPrimaryEdge:(NSString *)primaryEdge {
	if (@available(iOS 11.0, *)) {
		if ([primaryEdge isEqualToString:@"trailing"]) {
			[self.splitViewController setPrimaryEdge:UISplitViewControllerPrimaryEdgeTrailing];
		} else {
			[self.splitViewController setPrimaryEdge:UISplitViewControllerPrimaryEdgeLeading];
		}
	}
}

- (void)rnn_setMinWidth:(NSNumber *)minWidth {
	[self.splitViewController setMinimumPrimaryColumnWidth:[minWidth doubleValue]];
}

- (void)rnn_setMaxWidth:(NSNumber *)maxWidth {
	[self.splitViewController setMaximumPrimaryColumnWidth:[maxWidth doubleValue]];
}

@end

