#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNNavigationController.h"
#import <React/RCTConvert.h>

@implementation RNNNavigationControllerPresenter

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	RNNNavigationController* navigationController = self.bindedViewController;
	
	[navigationController rnn_setInteractivePopGestureEnabled:[options.popGesture getWithDefaultValue:YES]];
	[navigationController rnn_setRootBackgroundImage:[options.rootBackgroundImage getWithDefaultValue:nil]];
	[navigationController rnn_setNavigationBarTestID:[options.topBar.testID getWithDefaultValue:nil]];
	[navigationController rnn_setNavigationBarVisible:[options.topBar.visible getWithDefaultValue:YES] animated:[options.topBar.animate getWithDefaultValue:YES]];
	[navigationController rnn_hideBarsOnScroll:[options.topBar.hideOnScroll getWithDefaultValue:NO]];
	[navigationController rnn_setNavigationBarNoBorder:[options.topBar.noBorder getWithDefaultValue:NO]];
	[navigationController rnn_setBarStyle:[RCTConvert UIBarStyle:[options.topBar.barStyle getWithDefaultValue:@"default"]]];
	[navigationController rnn_setNavigationBarTranslucent:[options.topBar.background.translucent getWithDefaultValue:NO]];
	[navigationController rnn_setNavigationBarClipsToBounds:[options.topBar.background.clipToBounds getWithDefaultValue:NO]];
	[navigationController rnn_setNavigationBarBlur:[options.topBar.background.blur getWithDefaultValue:NO]];
	[navigationController setTopBarBackgroundColor:[options.topBar.background.color getWithDefaultValue:nil]];
	[navigationController rnn_setNavigationBarLargeTitleVisible:[options.topBar.largeTitle.visible getWithDefaultValue:NO]];
	[navigationController rnn_setNavigationBarLargeTitleFontFamily:[options.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[options.topBar.largeTitle.color getWithDefaultValue:nil]];
	[navigationController rnn_setNavigationBarFontFamily:[options.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.title.fontSize getWithDefaultValue:nil] color:[options.topBar.title.color getWithDefaultValue:nil]];
	[navigationController rnn_setBackButtonColor:[options.topBar.backButton.color getWithDefaultValue:nil]];
	[navigationController rnn_setBackButtonIcon:[options.topBar.backButton.icon getWithDefaultValue:nil] withColor:[options.topBar.backButton.color getWithDefaultValue:nil] title:[options.topBar.backButton.showTitle getWithDefaultValue:YES] ? [options.topBar.backButton.title getWithDefaultValue:nil] : @""];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	[super applyOptionsOnWillMoveToParentViewController:options];
	
	RNNNavigationController* navigationController = self.bindedViewController;
	[navigationController rnn_setBackButtonIcon:[options.topBar.backButton.icon getWithDefaultValue:nil] withColor:[options.topBar.backButton.color getWithDefaultValue:nil] title:[options.topBar.backButton.showTitle getWithDefaultValue:YES] ? [options.topBar.backButton.title getWithDefaultValue:nil] : @""];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	RNNNavigationController* navigationController = self.bindedViewController;
	
	if (newOptions.popGesture.hasValue) {
		[navigationController rnn_setInteractivePopGestureEnabled:newOptions.popGesture.get];
	}
	
	if (newOptions.rootBackgroundImage.hasValue) {
		[navigationController rnn_setRootBackgroundImage:newOptions.rootBackgroundImage.get];
	}
	
	if (newOptions.topBar.testID.hasValue) {
		[navigationController rnn_setNavigationBarTestID:newOptions.topBar.testID.get];
	}
	
	if (newOptions.topBar.visible.hasValue) {
		[navigationController rnn_setNavigationBarVisible:newOptions.topBar.visible.get animated:[newOptions.topBar.animate getWithDefaultValue:YES]];
	}
	
	if (newOptions.topBar.hideOnScroll.hasValue) {
		[navigationController rnn_hideBarsOnScroll:[newOptions.topBar.hideOnScroll get]];
	}
	
	if (newOptions.topBar.noBorder.hasValue) {
		[navigationController rnn_setNavigationBarNoBorder:[newOptions.topBar.noBorder get]];
	}
	
	if (newOptions.topBar.barStyle.hasValue) {
		[navigationController rnn_setBarStyle:[RCTConvert UIBarStyle:newOptions.topBar.barStyle.get]];
	}
	
	if (newOptions.topBar.background.translucent.hasValue) {
		[navigationController rnn_setNavigationBarTranslucent:[newOptions.topBar.background.translucent get]];
	}
	
	if (newOptions.topBar.background.clipToBounds.hasValue) {
		[navigationController rnn_setNavigationBarClipsToBounds:[newOptions.topBar.background.clipToBounds get]];
	}
	
	if (newOptions.topBar.background.blur.hasValue) {
		[navigationController rnn_setNavigationBarBlur:[newOptions.topBar.background.blur get]];
	}
	
	if (newOptions.topBar.background.color.hasValue) {
		[navigationController setTopBarBackgroundColor:newOptions.topBar.background.color.get];
	}
	
	if (newOptions.topBar.largeTitle.visible.hasValue) {
		[navigationController rnn_setNavigationBarLargeTitleVisible:newOptions.topBar.largeTitle.visible.get];
	}
	
	if (newOptions.topBar.backButton.icon.hasValue) {
		[navigationController rnn_setBackButtonIcon:[newOptions.topBar.backButton.icon getWithDefaultValue:nil] withColor:[newOptions.topBar.backButton.color getWithDefaultValue:nil] title:[newOptions.topBar.backButton.showTitle getWithDefaultValue:YES] ? [newOptions.topBar.backButton.title getWithDefaultValue:nil] : @""];
		
	}
	
	if (newOptions.topBar.backButton.color.hasValue) {
		[navigationController rnn_setBackButtonColor:newOptions.topBar.backButton.color.get];
	}
	
	[navigationController rnn_setNavigationBarLargeTitleFontFamily:[newOptions.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[newOptions.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[newOptions.topBar.largeTitle.color getWithDefaultValue:nil]];
	
	[navigationController rnn_setNavigationBarFontFamily:[newOptions.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[newOptions.topBar.title.fontSize getWithDefaultValue:nil] color:[newOptions.topBar.title.color getWithDefaultValue:nil]];
	
}

@end
