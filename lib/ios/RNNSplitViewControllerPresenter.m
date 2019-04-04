#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNSplitViewController.h"

@implementation RNNSplitViewControllerPresenter

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UISplitViewController* splitViewController = self.bindedViewController;
	[splitViewController rnn_setDisplayMode:options.splitView.displayMode];
	[splitViewController rnn_setPrimaryEdge:options.splitView.primaryEdge];
	[splitViewController rnn_setMinWidth:options.splitView.minWidth];
	[splitViewController rnn_setMaxWidth:options.splitView.maxWidth];
}


- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];
	
	UISplitViewController* splitViewController = self.bindedViewController;
	[splitViewController rnn_setDisplayMode:initialOptions.splitView.displayMode];
	[splitViewController rnn_setPrimaryEdge:initialOptions.splitView.primaryEdge];
	[splitViewController rnn_setMinWidth:initialOptions.splitView.minWidth];
	[splitViewController rnn_setMaxWidth:initialOptions.splitView.maxWidth];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	UISplitViewController* splitViewController = self.bindedViewController;

	if (newOptions.splitView.displayMode) {
		[splitViewController rnn_setDisplayMode:newOptions.splitView.displayMode];
	}
	if (newOptions.splitView.primaryEdge) {
		[splitViewController rnn_setPrimaryEdge:newOptions.splitView.primaryEdge];
	}
	if (newOptions.splitView.minWidth) {
		[splitViewController rnn_setMinWidth:newOptions.splitView.minWidth];
	}
	if (newOptions.splitView.maxWidth) {
		[splitViewController rnn_setMaxWidth:newOptions.splitView.maxWidth];
	}
}

@end
