#import "RNNSplitViewController.h"
#import "UISplitViewController+RNNOptions.h"

@implementation UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode {
    if ([displayMode isEqualToString:@"visible"]) {
        // deprecated since iOS 14
        self.preferredDisplayMode = UISplitViewControllerDisplayModeAllVisible;
    } else if ([displayMode isEqualToString:@"hidden"]) {
        // deprecated since iOS 14
        self.preferredDisplayMode = UISplitViewControllerDisplayModePrimaryHidden;
    } else if ([displayMode isEqualToString:@"overlay"]) {
        // deprecated since iOS 14
        self.preferredDisplayMode = UISplitViewControllerDisplayModePrimaryOverlay;
    } else if ([displayMode isEqualToString:@"secondaryOnly"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModeSecondaryOnly;
    } else if ([displayMode isEqualToString:@"oneBesideSecondary"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModeOneBesideSecondary;
    } else if ([displayMode isEqualToString:@"oneOverSecondary"]) {
        self.preferredDisplayMode = UISplitViewControllerDisplayModeOneOverSecondary;
    } else if (@available(iOS 14.0, *)) {
        if ([displayMode isEqualToString:@"twoBesideSecondary"]) {
            self.preferredDisplayMode = UISplitViewControllerDisplayModeTwoBesideSecondary;
        } else if ([displayMode isEqualToString:@"twoDisplaceSecondary"]) {
            self.preferredDisplayMode = UISplitViewControllerDisplayModeTwoDisplaceSecondary;
        } else if ([displayMode isEqualToString:@"twoOverSecondary"]) {
            self.preferredDisplayMode = UISplitViewControllerDisplayModeTwoOverSecondary;
        } else {
            self.preferredDisplayMode = UISplitViewControllerDisplayModeAutomatic;
        }
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
