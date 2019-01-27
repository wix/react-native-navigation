#import "RNNSplitViewControllerPresenter.h"
#import "UISplitViewController+RNNOptions.h"
#import "RCTConvert+Modal.h"
#import "RNNSplitViewController.h"

@implementation RNNSplitViewControllerPresenter

- (void)bindViewController:(UISplitViewController *)bindedViewController viewCreator:(id<RNNRootViewCreator>)creator {
	self.bindedViewController = bindedViewController;
	_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self.bindedViewController rootViewCreator:creator];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UISplitViewController* splitViewController = self.bindedViewController;
	[splitViewController rnn_setDisplayMode:[options.splitView.displayMode getWithDefaultValue:@"auto"]];
	[splitViewController rnn_setPrimaryEdge:[options.splitView.primaryEdge getWithDefaultValue:@"leading"]];
	[splitViewController rnn_setMinWidth:[options.splitView.minWidth getWithDefaultValue:150]];
	[splitViewController rnn_setMaxWidth:[options.splitView.maxWidth getWithDefaultValue:300]];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	UISplitViewController* splitViewController = self.bindedViewController;

	if (newOptions.splitView.hasValue) {
		if (newOptions.splitView.displayMode.hasValue) {
			[splitViewController rnn_setDisplayMode:newOptions.splitView.displayMode.get];
		}
		if (newOptions.splitView.primaryEdge.hasValue) {
			[splitViewController rnn_setPrimaryEdge:newOptions.splitView.primaryEdge.get];
		}
		if (newOptions.splitView.minWidth.hasValue) {
			[splitViewController rnn_setMinWidth:<#(NSNumber *)#>:newOptions.splitView.minWidth.get];
		}
		if (newOptions.splitView.maxWidth.hasValue) {
			[splitViewController rnn_setMaxWidth:<#(NSNumber *)#>:newOptions.splitView.maxWidth.get];
		}
	}
}

@end
