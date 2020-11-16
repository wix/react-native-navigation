#import "RNNSplitViewController.h"
#import "UISplitViewController+RNNOptions.h"

@implementation UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode {
    if ([displayMode isEqualToString:@"visible"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModeAllVisible;
    } else if ([displayMode isEqualToString:@"hidden"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModePrimaryHidden;
    } else if ([displayMode isEqualToString:@"overlay"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModePrimaryOverlay;
    } else {
        self.preferredDisplayMode = UISplitViewControllerDisplayModeAutomatic;
    }
}

- (void)rnn_setPrimaryEdge:(NSString *)primaryEdge {
    if ([primaryEdge isEqualToString:@"trailing"]) {
        self.primaryEdge = UISplitViewControllerPrimaryEdgeTrailing;
    } else {
        self.primaryEdge = UISplitViewControllerPrimaryEdgeLeading;
    }
}

- (void)rnn_setMinWidth:(Number *)minWidth {
    if (minWidth.hasValue) {
        [self setMinimumPrimaryColumnWidth:[[minWidth get] doubleValue]];
    }
}

- (void)rnn_setMaxWidth:(Number *)maxWidth {
    if (maxWidth.hasValue) {
        [self setMaximumPrimaryColumnWidth:[[maxWidth get] doubleValue]];
    }
}

- (void)rnn_setPrimaryBackgroundStyle:(NSString *)style {
    if (@available(iOS 13.0, *)) {
        if ([style isEqualToString:@"sidebar"]) {
            [self setPrimaryBackgroundStyle:UISplitViewControllerBackgroundStyleSidebar];
        }
    }
}

@end
