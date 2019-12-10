#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNSplitViewController.h"

@implementation RNNSplitViewControllerPresenter

-(instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super initWithDefaultOptions:defaultOptions];
	return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UISplitViewController* splitViewController = self.boundViewController;
	[splitViewController rnn_setDisplayMode:options.splitView.displayMode];
	[splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
	[splitViewController rnn_setMinWidth:options.splitView.minWidth];
	[splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
}


- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];
	
	UISplitViewController* splitViewController = self.boundViewController;
	[splitViewController rnn_setDisplayMode:initialOptions.splitView.displayMode];
	[splitViewController rnn_setPrimaryEdge:initialOptions.splitView.primaryEdge];
	[splitViewController rnn_setMinWidth:initialOptions.splitView.minWidth];
	[splitViewController rnn_setMaxWidth:initialOptions.splitView.maxWidth];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
	
	UISplitViewController* splitViewController = self.boundViewController;

	if (options.splitView.displayMode) {
		[splitViewController rnn_setDisplayMode:options.splitView.displayMode];
	}
	if (options.splitView.primaryEdge) {
		[splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
	}
	if (options.splitView.minWidth) {
		[splitViewController rnn_setMinWidth:options.splitView.minWidth];
	}
	if (options.splitView.maxWidth) {
		[splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
	}
}

@end
