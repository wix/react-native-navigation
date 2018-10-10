#import "RNNTopBarOptions.h"
#import "RNNNavigationButtons.h"
#import "RNNCustomTitleView.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNTopBarOptions ()

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end

@implementation RNNTopBarOptions

- (void)applyOn:(UIViewController*)viewController {
	[self.title applyOn:viewController];
	[self.largeTitle applyOn:viewController];
	[self.backButton applyOn:viewController];
	
	if ([self.searchBar boolValue]) {
		[viewController rnn_setSearchBarWithPlaceholder:self.searchBarPlaceholder];
	}
	
	if (self.drawBehind) {
		[viewController rnn_setDrawBehindTopBar:[self.drawBehind boolValue]];
	}
	
	if (self.rightButtons || self.leftButtons) {
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:self.leftButtons rightButtons:self.rightButtons defaultLeftButtonStyle:self.leftButtonStyle defaultRightButtonStyle:self.rightButtonStyle];
	}
		
	self.rightButtons = nil;
	self.leftButtons = nil;
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.testID) {
		[navigationController rnn_setNavigationBarTestID:self.testID];
	}
	
	if (self.visible) {
		[navigationController rnn_setNavigationBarVisible:[self.visible boolValue] animated:self.animate];
	}
	
	if (self.hideOnScroll) {
		[navigationController rnn_hideBarsOnScroll:[self.hideOnScroll boolValue]];
	}
	
	if (self.noBorder) {
		[navigationController rnn_setNavigationBarNoBorder:[self.noBorder boolValue]];
	}
	
	if (self.barStyle) {
		[navigationController rnn_setBarStyle:[RCTConvert UIBarStyle:self.barStyle]];
	}
	
	[self.background applyOnNavigationController:navigationController];
}

- (void)setRightButtonColor:(NSNumber *)rightButtonColor {
	_rightButtonColor = rightButtonColor;
	_rightButtonStyle.color = rightButtonColor;
}

- (void)setRightButtonDisabledColor:(NSNumber *)rightButtonDisabledColor {
	_rightButtonDisabledColor = rightButtonDisabledColor;
	_rightButtonStyle.disabledColor = rightButtonDisabledColor;
}

- (void)setLeftButtonColor:(NSNumber *)leftButtonColor {
	_leftButtonColor = leftButtonColor;
	_leftButtonStyle.color = leftButtonColor;
}

- (void)setLeftButtonDisabledColor:(NSNumber *)leftButtonDisabledColor {
	_leftButtonDisabledColor = leftButtonDisabledColor;
	_leftButtonStyle.disabledColor = leftButtonDisabledColor;
}

- (void)setRightButtons:(id)rightButtons {
	if ([rightButtons isKindOfClass:[NSArray class]]) {
		_rightButtons = rightButtons;
	} else if ([rightButtons isKindOfClass:[NSDictionary class]]) {
		if (rightButtons[@"id"]) {
			_rightButtons = @[rightButtons];
		} else {
			[_rightButtonStyle mergeWith:rightButtons];
		}
	} else {
		_rightButtons = rightButtons;
	}
}

- (void)setLeftButtons:(id)leftButtons {
	if ([leftButtons isKindOfClass:[NSArray class]]) {
		_leftButtons = leftButtons;
	} else if ([leftButtons isKindOfClass:[NSDictionary class]]) {
		if (leftButtons[@"id"]) {
			_leftButtons = @[leftButtons];
		} else {
			[_leftButtonStyle mergeWith:leftButtons];
		}
	} else {
		_leftButtons = leftButtons;
	}
}

@end
