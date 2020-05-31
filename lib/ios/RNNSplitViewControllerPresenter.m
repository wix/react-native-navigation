#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"
#import "RNNSplitViewController.h"

@implementation RNNSplitViewControllerPresenter

-(instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super initWithDefaultOptions:defaultOptions];
	return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	[self.splitViewController rnn_setDisplayMode:options.splitView.displayMode];
	[self.splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
	[self.splitViewController rnn_setMinWidth:options.splitView.minWidth];
	[self.splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
}


- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];
	
	[self.splitViewController rnn_setDisplayMode:initialOptions.splitView.displayMode];
	[self.splitViewController rnn_setPrimaryEdge:initialOptions.splitView.primaryEdge];
	[self.splitViewController rnn_setMinWidth:initialOptions.splitView.minWidth];
	[self.splitViewController rnn_setMaxWidth:initialOptions.splitView.maxWidth];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
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
}

- (UISplitViewController *)splitViewController {
    return self.boundViewController;
}

@end
