#import "RNNNavigationButtons.h"
#import "RNNUIBarButtonItem.h"
#import <React/RCTConvert.h>
#import "RCTHelpers.h"

@interface RNNNavigationButtons()

@property (weak, nonatomic) RNNRootViewController* viewController;
@property (strong, nonatomic) NSArray* rightButtons;
@property (strong, nonatomic) NSArray* leftButtons;

@end

@implementation RNNNavigationButtons

-(instancetype)initWithViewController:(RNNRootViewController*)viewController {
	self = [super init];
	
	self.viewController = viewController;
	
	return self;
}

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons {
	if (leftButtons) {
		[self setButtons:leftButtons side:@"left" animated:NO];
	}
	
	if (rightButtons) {
		[self setButtons:rightButtons side:@"right" animated:NO];
	}
}

-(void)setButtons:(NSArray*)buttons side:(NSString*)side animated:(BOOL)animated {
	NSMutableArray *barButtonItems = [NSMutableArray new];
	for (NSDictionary *button in buttons) {
		RNNUIBarButtonItem* barButtonItem = [self buildButton:button];
		if(barButtonItem) {
			[barButtonItems addObject:barButtonItem];
		}
	}
	
	if ([side isEqualToString:@"left"]) {
		self.leftButtons = barButtonItems;
		[self.viewController.navigationItem setLeftBarButtonItems:self.leftButtons animated:animated];
	}
	
	if ([side isEqualToString:@"right"]) {
		self.rightButtons = barButtonItems;
		[self.viewController.navigationItem setRightBarButtonItems:self.rightButtons animated:animated];
	}
}

-(RNNUIBarButtonItem*)buildButton: (NSDictionary*)dictionary {
	NSString* buttonId = dictionary[@"id"];
	NSString* title = dictionary[@"title"];
	NSString* component = dictionary[@"component"];
	
	if (!buttonId) {
		@throw [NSException exceptionWithName:@"NSInvalidArgumentException" reason:[@"button id is not specified " stringByAppendingString:title] userInfo:nil];
	}
	
	UIImage* iconImage = nil;
	id icon = dictionary[@"icon"];
	if (icon) {
		iconImage = [RCTConvert UIImage:icon];
	}
	
	RNNUIBarButtonItem *barButtonItem;
	if (component) {
		RCTRootView *view = (RCTRootView*)[self.viewController.creator createRootView:component rootViewId:buttonId];
		barButtonItem = [[RNNUIBarButtonItem alloc] init:buttonId withCustomView:view];
	} else if (iconImage) {
		barButtonItem = [[RNNUIBarButtonItem alloc] init:buttonId withIcon:iconImage];
	} else if (title) {
		barButtonItem = [[RNNUIBarButtonItem alloc] init:buttonId withTitle:title];
		
		NSMutableDictionary *buttonTextAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"button"];
		if (buttonTextAttributes.allKeys.count > 0) {
			[barButtonItem setTitleTextAttributes:buttonTextAttributes forState:UIControlStateNormal];
		}
	} else {
		return nil;
	}
	
	barButtonItem.target = self;
	barButtonItem.action = @selector(onButtonPress:);
	
	NSNumber *enabled = dictionary[@"enabled"];
	BOOL enabledBool = enabled ? [enabled boolValue] : YES;
	[barButtonItem setEnabled:enabledBool];
	
	NSNumber *disableIconTintString = dictionary[@"disableIconTint"];
	BOOL disableIconTint = disableIconTintString ? [disableIconTintString boolValue] : NO;
	if (disableIconTint) {
		[barButtonItem setImage:[barButtonItem.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
	}
	
	id tintColor = dictionary[@"tintColor"];
	if (tintColor) {
		[barButtonItem setTintColor:[RCTConvert UIColor: tintColor]];
	}
	
	NSString *testID = dictionary[@"testID"];
	if (testID)
	{
		barButtonItem.accessibilityIdentifier = testID;
	}
	
	return barButtonItem;
}

-(void)onButtonPress:(RNNUIBarButtonItem*)barButtonItem {
	[self.viewController.eventEmitter sendOnNavigationButtonPressed:self.viewController.componentId buttonId:barButtonItem.buttonId];
}

@end
