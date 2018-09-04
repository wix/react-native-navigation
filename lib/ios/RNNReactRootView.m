#import "RNNReactRootView.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>

@implementation RNNReactRootView

- (void)layoutSubviews {
	[super layoutSubviews];
	[self.bridge.uiManager setSize:self.bounds.size forView:self];
}

- (void)contentDidAppear:(NSNotification *)notification {
	
}

@end
