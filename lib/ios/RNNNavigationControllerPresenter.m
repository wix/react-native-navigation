#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"
#import "RNNNavigationController.h"

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
	[navigationController rnn_setBackButtonIcon:[options.topBar.backButton.icon getWithDefaultValue:nil] withColor:[options.topBar.backButton.color getWithDefaultValue:nil] title:[options.topBar.backButton.showTitle getWithDefaultValue:nil] ? options.topBar.backButton.title.get : @""];
	[navigationController rnn_setNavigationBarFontFamily:[options.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.title.fontSize getWithDefaultValue:nil] color:[options.topBar.title.color getWithDefaultValue:nil]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[super mergeOptions:options];
	
	RNNNavigationController* navigationController = self.bindedViewController;
	
	if (options.popGesture.hasValue) {
		[navigationController rnn_setInteractivePopGestureEnabled:options.popGesture.get];
	}
	
	if (options.rootBackgroundImage.hasValue) {
		[navigationController rnn_setRootBackgroundImage:options.rootBackgroundImage.get];
	}
	
	if (options.topBar.testID.hasValue) {
		[navigationController rnn_setNavigationBarTestID:options.topBar.testID.get];
	}
	
	if (options.topBar.visible.hasValue) {
		[navigationController rnn_setNavigationBarVisible:options.topBar.visible.get animated:[options.topBar.animate getWithDefaultValue:YES]];
	}
	
	if (options.topBar.hideOnScroll.hasValue) {
		[navigationController rnn_hideBarsOnScroll:[options.topBar.hideOnScroll get]];
	}
	
	if (options.topBar.noBorder.hasValue) {
		[navigationController rnn_setNavigationBarNoBorder:[options.topBar.noBorder get]];
	}
	
	if (options.topBar.barStyle.hasValue) {
		[navigationController rnn_setBarStyle:[RCTConvert UIBarStyle:options.topBar.barStyle.get]];
	}
	
	if (options.topBar.background.translucent.hasValue) {
		[navigationController rnn_setNavigationBarTranslucent:[options.topBar.background.translucent get]];
	}
	
	if (options.topBar.background.clipToBounds.hasValue) {
		[navigationController rnn_setNavigationBarClipsToBounds:[options.topBar.background.clipToBounds get]];
	}
	
	if (options.topBar.background.blur.hasValue) {
		[navigationController rnn_setNavigationBarBlur:[options.topBar.background.blur get]];
	}
	
	if (options.topBar.background.color.hasValue) {
		[navigationController setTopBarBackgroundColor:options.topBar.background.color.get];
	}
	
	if (options.topBar.largeTitle.visible.hasValue) {
		[navigationController rnn_setNavigationBarLargeTitleVisible:options.topBar.largeTitle.visible.get];
	}
	
	if (options.topBar.backButton.icon.hasValue) {
		[navigationController rnn_setBackButtonIcon:[options.topBar.backButton.icon getWithDefaultValue:nil] withColor:[options.topBar.backButton.color getWithDefaultValue:nil] title:[options.topBar.backButton.showTitle getWithDefaultValue:nil] ? options.topBar.backButton.title.get : @""];

	}
	
	[navigationController rnn_setNavigationBarLargeTitleFontFamily:[options.topBar.largeTitle.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.largeTitle.fontSize getWithDefaultValue:nil] color:[options.topBar.largeTitle.color getWithDefaultValue:nil]];
	
	[navigationController rnn_setNavigationBarFontFamily:[options.topBar.title.fontFamily getWithDefaultValue:nil] fontSize:[options.topBar.title.fontSize getWithDefaultValue:nil] color:[options.topBar.title.color getWithDefaultValue:nil]];

}

@end
