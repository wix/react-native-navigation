#import "RNNSplitViewControllerPresenter.h"
#import "RNNSplitViewController.h"
#import "UISplitViewController+RNNOptions.h"

@implementation RNNSplitViewControllerPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithDefaultOptions:defaultOptions];
    return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    [super applyOptions:options];

    [self.splitViewController rnn_setDisplayMode:options.splitView.displayMode];
    [self.splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
    [self.splitViewController rnn_setMinWidth:options.splitView.minWidth];
    [self.splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
    [self.splitViewController
        rnn_setPrimaryBackgroundStyle:options.splitView.primaryBackgroundStyle];
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
    [super applyOptionsOnInit:initialOptions];

    [self.splitViewController rnn_setDisplayMode:initialOptions.splitView.displayMode];
    [self.splitViewController rnn_setPrimaryEdge:initialOptions.splitView.primaryEdge];
    [self.splitViewController rnn_setMinWidth:initialOptions.splitView.minWidth];
    [self.splitViewController rnn_setMaxWidth:initialOptions.splitView.maxWidth];
    [self.splitViewController
        rnn_setPrimaryBackgroundStyle:initialOptions.splitView.primaryBackgroundStyle];
}

- (void)mergeOptions:(RNNNavigationOptions *)options
     resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];

    if (options.splitView.displayMode) {
        [self.splitViewController rnn_setDisplayMode:options.splitView.displayMode];
    }
    if (options.splitView.primaryEdge) {
        [self.splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
    }
    if (options.splitView.minWidth) {
        [self.splitViewController rnn_setMinWidth:options.splitView.minWidth];
    }
    if (options.splitView.maxWidth) {
        [self.splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
    }
    if (options.splitView.primaryBackgroundStyle) {
        [self.splitViewController
            rnn_setPrimaryBackgroundStyle:options.splitView.primaryBackgroundStyle];
    }
}

- (UISplitViewController *)splitViewController {
    return self.boundViewController;
}

@end
