#import "RNNNavigationButtons.h"
#import "RNNUIBarButtonItem.h"
#import <React/RCTConvert.h>
#import "RCTHelpers.h"

@interface RNNNavigationButtons()

@property (weak, nonatomic) RNNRootViewController* viewController;
@property (strong, nonatomic) NSArray* rightButtons;
@property (strong, nonatomic) NSArray* leftButtons;
@property (strong, nonatomic) RNNButtonOptions* defaultButtonStyle;

@end

@implementation RNNNavigationButtons

-(instancetype)initWithViewController:(RNNRootViewController*)viewController {
	self = [super init];
	
	self.viewController = viewController;
	
	return self;
}

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle {
	_defaultButtonStyle = defaultButtonStyle;
	if (leftButtons) {
		[self setButtons:leftButtons side:@"left" animated:NO];
	}
	
	if (rightButtons) {
		[self setButtons:rightButtons side:@"right" animated:NO];
	}
}

-(void)setButtons:(NSArray*)buttons side:(NSString*)side animated:(BOOL)animated {
	NSMutableArray *barButtonItems = [NSMutableArray new];
	NSArray* resolvedButtons = [self resolveButtons:buttons];
	for (NSDictionary *button in resolvedButtons) {
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

- (NSArray *)resolveButtons:(id)buttons {
	if ([buttons isKindOfClass:[NSArray class]]) {
		return buttons;
	} else {
		return @[buttons];
	}
}

-(RNNUIBarButtonItem*)buildButton: (NSDictionary*)dictionary {
	NSString* buttonId = dictionary[@"id"];
	NSString* title = dictionary[@"text"];
	NSDictionary* component = dictionary[@"component"];
	
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
		RCTRootView *view = (RCTRootView*)[self.viewController.creator createRootView:component[@"name"] rootViewId:component[@"componentId"]];
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
	
	NSMutableDictionary* textAttributes = [[NSMutableDictionary alloc] init];
	NSMutableDictionary* disabledTextAttributes = [[NSMutableDictionary alloc] init];
	
	UIColor* color = [self color:dictionary[@"color"] defaultColor:_defaultButtonStyle.color];
	if (color) {
		[textAttributes setObject:color forKey:NSForegroundColorAttributeName];
	}
	
	UIColor* disabledColor = [self color:dictionary[@"disabledColor"] defaultColor:_defaultButtonStyle.disabledColor];;
	if (disabledColor) {
		UIColor *color = disabledColor;
		[disabledTextAttributes setObject:color forKey:NSForegroundColorAttributeName];
	}
	
	NSNumber* fontSize = [self fontSize:dictionary[@"fontSize"] defaultFontSize:_defaultButtonStyle.fontSize];
	NSString* fontFamily = [self fontFamily:dictionary[@"fontFamily"] defaultFontFamily:_defaultButtonStyle.fontFamily];
	if (fontFamily) {
		[textAttributes setObject:[UIFont fontWithName:fontFamily size:[fontSize floatValue]] forKey:NSFontAttributeName];
	} else{
		[textAttributes setObject:[UIFont systemFontOfSize:[fontSize floatValue]] forKey:NSFontAttributeName];
	}
	
	[barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
	[barButtonItem setTitleTextAttributes:disabledTextAttributes forState:UIControlStateDisabled];
	
	NSString *testID = dictionary[@"testID"];
	if (testID)
	{
		barButtonItem.accessibilityIdentifier = testID;
	}
	
	return barButtonItem;
}

- (UIColor *)color:(NSNumber *)color defaultColor:(NSNumber *)defaultColor {
	if (color) {
		return [RCTConvert UIColor:color];
	} else if (defaultColor) {
		return [RCTConvert UIColor:defaultColor];
	}
		
	return nil;
}

- (NSNumber *)fontSize:(NSNumber *)fontSize defaultFontSize:(NSNumber *)defaultFontSize {
	if (fontSize) {
		return fontSize;
	} else if (defaultFontSize) {
		return defaultFontSize;
	}
	
	return @(17);
}

- (NSString *)fontFamily:(NSString *)fontFamily defaultFontFamily:(NSString *)defaultFontFamily {
	if (fontFamily) {
		return fontFamily;
	} else {
		return defaultFontFamily;
	}
}

-(void)onButtonPress:(RNNUIBarButtonItem*)barButtonItem {
	[self.viewController.eventEmitter sendOnNavigationButtonPressed:self.viewController.componentId buttonId:barButtonItem.buttonId];
}

@end
