#import "RNNTopBarOptions.h"
#import "RNNNavigationButtons.h"
#import "RNNCustomTitleView.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNTopBarOptions ()

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end

@implementation RNNTopBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.hideOnScroll = [BoolParser parse:dict key:@"hideOnScroll"];
	self.leftButtonColor = [ColorParser parse:dict key:@"leftButtonColor"];
	self.rightButtonColor = [ColorParser parse:dict key:@"rightButtonColor"];
	self.leftButtonDisabledColor = [ColorParser parse:dict key:@"leftButtonDisabledColor"];
	self.rightButtonDisabledColor = [ColorParser parse:dict key:@"rightButtonDisabledColor"];
	self.drawBehind = [BoolParser parse:dict key:@"drawBehind"];
	self.noBorder = [BoolParser parse:dict key:@"noBorder"];
	self.animate = [BoolParser parse:dict key:@"animate"];
	self.searchBar = [BoolParser parse:dict key:@"searchBar"];
	self.searchBarHiddenWhenScrolling = [BoolParser parse:dict key:@"searchBarHiddenWhenScrolling"];
	self.testID = [TextParser parse:dict key:@"testID"];
	self.barStyle = [TextParser parse:dict key:@"barStyle"];
	self.searchBarPlaceholder = [TextParser parse:dict key:@"searchBarPlaceholder"];
	self.largeTitle = [[RNNLargeTitleOptions alloc] initWithDict:dict[@"largeTitle"]];
	self.title = [[RNNTitleOptions alloc] initWithDict:dict[@"title"]];
	self.subtitle = [[RNNSubtitleOptions alloc] initWithDict:dict[@"subtitle"]];
	self.background = [[RNNBackgroundOptions alloc] initWithDict:dict[@"background"]];
	self.backButton = [[RNNBackButtonOptions alloc] initWithDict:dict[@"backButton"]];
	self.leftButtonStyle = [[RNNButtonOptions alloc] initWithDict:dict[@"leftButtonStyle"]];
	self.rightButtonStyle = [[RNNButtonOptions alloc] initWithDict:dict[@"rightButtonStyle"]];
	
	if (self.leftButtonColor.hasValue) {
		self.leftButtonStyle.color = self.leftButtonColor;
	}
	
	if (self.rightButtonColor.hasValue) {
		self.rightButtonStyle.color = self.rightButtonColor;
	}
	
	if (self.leftButtonDisabledColor.hasValue) {
		self.leftButtonStyle.disabledColor = self.rightButtonDisabledColor;
	}
	
	if (self.rightButtonDisabledColor.hasValue) {
		self.rightButtonStyle.disabledColor = self.rightButtonDisabledColor;
	}
	
	
	self.leftButtons = dict[@"leftButtons"];
	self.rightButtons = dict[@"rightButtons"];
	

	return self;
}

- (void)applyOn:(UIViewController*)viewController {
	if (self.rightButtons || self.leftButtons) {
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:self.leftButtons rightButtons:self.rightButtons defaultLeftButtonStyle:self.leftButtonStyle defaultRightButtonStyle:self.rightButtonStyle];
	}
	
	self.rightButtons = nil;
	self.leftButtons = nil;
}

//- (void)setRightButtonColor:(NSNumber *)rightButtonColor {
//	_rightButtonColor = rightButtonColor;
//	_rightButtonStyle.color = rightButtonColor;
//}
//
//- (void)setRightButtonDisabledColor:(NSNumber *)rightButtonDisabledColor {
//	_rightButtonDisabledColor = rightButtonDisabledColor;
//	_rightButtonStyle.disabledColor = rightButtonDisabledColor;
//}
//
//- (void)setLeftButtonColor:(NSNumber *)leftButtonColor {
//	_leftButtonColor = leftButtonColor;
//	_leftButtonStyle.color = leftButtonColor;
//}
//
//- (void)setLeftButtonDisabledColor:(NSNumber *)leftButtonDisabledColor {
//	_leftButtonDisabledColor = leftButtonDisabledColor;
//	_leftButtonStyle.disabledColor = leftButtonDisabledColor;
//}

//- (void)setRightButtons:(id)rightButtons {
//	if ([rightButtons isKindOfClass:[NSArray class]]) {
//		_rightButtons = rightButtons;
//	} else if ([rightButtons isKindOfClass:[NSDictionary class]]) {
//		if (rightButtons[@"id"]) {
//			_rightButtons = @[rightButtons];
//		} else {
//			[_rightButtonStyle mergeWith:rightButtons];
//		}
//	} else {
//		_rightButtons = rightButtons;
//	}
//}
//
//- (void)setLeftButtons:(id)leftButtons {
//	if ([leftButtons isKindOfClass:[NSArray class]]) {
//		_leftButtons = leftButtons;
//	} else if ([leftButtons isKindOfClass:[NSDictionary class]]) {
//		if (leftButtons[@"id"]) {
//			_leftButtons = @[leftButtons];
//		} else {
//			[_leftButtonStyle mergeWith:leftButtons];
//		}
//	} else {
//		_leftButtons = leftButtons;
//	}
//}

@end
