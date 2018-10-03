#import "RNNTopBarOptions.h"
#import "RNNNavigationButtons.h"
#import "RNNCustomTitleView.h"

@interface RNNTopBarOptions ()

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end

@implementation RNNTopBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super initWithDict:dict];
	
	self.title.subtitle = self.subtitle;
	
	return self;
}

- (void)mergeWith:(NSDictionary *)otherOptions {
	[super mergeWith:otherOptions];
	self.title.subtitle = self.subtitle;
}

- (void)applyOn:(UIViewController*)viewController {
	[self.title applyOn:viewController];
	[self.largeTitle applyOn:viewController];
	[self.backButton applyOn:viewController];
	
	if (@available(iOS 11.0, *)) {
		if ([self.searchBar boolValue] && !viewController.navigationItem.searchController) {
			UISearchController *search = [[UISearchController alloc]initWithSearchResultsController:nil];
			search.dimsBackgroundDuringPresentation = NO;
			if ([viewController conformsToProtocol:@protocol(UISearchResultsUpdating)]) {
				[search setSearchResultsUpdater:((UIViewController <UISearchResultsUpdating> *) viewController)];
			}
			search.searchBar.delegate = (id<UISearchBarDelegate>)viewController;
			if (self.searchBarPlaceholder) {
				search.searchBar.placeholder = self.searchBarPlaceholder;
			}
			viewController.navigationItem.searchController = search;
			
			viewController.navigationItem.hidesSearchBarWhenScrolling = [self.searchBarHiddenWhenScrolling boolValue];
			
			// Fixes #3450, otherwise, UIKit will infer the presentation context to be the root most view controller
			viewController.definesPresentationContext = YES;
		}
	}
	
	if (self.drawBehind) {
		if ([self.drawBehind boolValue]) {
			viewController.edgesForExtendedLayout |= UIRectEdgeTop;
		} else {
			viewController.edgesForExtendedLayout &= ~UIRectEdgeTop;
		}
	} else {
		viewController.edgesForExtendedLayout = UIRectEdgeAll;
	}
	
	if (self.rightButtons || self.leftButtons) {
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:self.leftButtons rightButtons:self.rightButtons defaultLeftButtonStyle:self.leftButtonStyle defaultRightButtonStyle:self.rightButtonStyle];
	}
	
	[self applyOnNavigationController:viewController.navigationController];
	
	self.rightButtons = nil;
	self.leftButtons = nil;
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.testID) {
		navigationController.navigationBar.accessibilityIdentifier = self.testID;
	}
	
	if (self.visible) {
		[navigationController setNavigationBarHidden:![self.visible boolValue] animated:[self.animate boolValue]];
	} else {
		[navigationController setNavigationBarHidden:NO animated:NO];
	}
	
	if (self.hideOnScroll) {
		navigationController.hidesBarsOnSwipe = [self.hideOnScroll boolValue];
	} else {
		navigationController.hidesBarsOnSwipe = NO;
	}
	
	if (self.noBorder) {
		if ([self.noBorder boolValue]) {
			navigationController.navigationBar
			.shadowImage = [[UIImage alloc] init];
		} else {
			navigationController.navigationBar
			.shadowImage = nil;
		}
	}
	
	if (self.barStyle) {
		navigationController.navigationBar.barStyle = [RCTConvert UIBarStyle:self.barStyle];
	} else {
		navigationController.navigationBar.barStyle = UIBarStyleDefault;
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
