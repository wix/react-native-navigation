#import "RNNNavigationButtons.h"
#import "RNNUIBarButtonItem.h"
#import <React/RCTConvert.h>
#import "RCTHelpers.h"
#import "UIImage+tint.h"

@interface RNNNavigationButtons()

@property (weak, nonatomic) RNNRootViewController* viewController;
@property (strong, nonatomic) RNNButtonOptions* defaultLeftButtonStyle;
@property (strong, nonatomic) RNNButtonOptions* defaultRightButtonStyle;

@end

@implementation RNNNavigationButtons

-(instancetype)initWithViewController:(RNNRootViewController*)viewController {
	self = [super init];
	
	self.viewController = viewController;
	
	return self;
}

- (void)applyLeftButtons:(NSArray *)leftButtons rightButtons:(NSArray *)rightButtons defaultLeftButtonStyle:(RNNButtonOptions *)defaultLeftButtonStyle defaultRightButtonStyle:(RNNButtonOptions *)defaultRightButtonStyle {
	_defaultLeftButtonStyle = defaultLeftButtonStyle;
	_defaultRightButtonStyle = defaultRightButtonStyle;
	if (leftButtons) {
		[self setButtons:leftButtons side:@"left" animated:NO defaultStyle:_defaultLeftButtonStyle];
	}
	
	if (rightButtons) {
		[self setButtons:rightButtons side:@"right" animated:NO defaultStyle:_defaultRightButtonStyle];
	}
}

-(void)setButtons:(NSArray*)buttons side:(NSString*)side animated:(BOOL)animated defaultStyle:(RNNButtonOptions *)defaultStyle {
	NSMutableArray *barButtonItems = [NSMutableArray new];
	NSArray* resolvedButtons = [self resolveButtons:buttons];
	for (NSDictionary *button in resolvedButtons) {
		RNNUIBarButtonItem* barButtonItem = [self buildButton:button defaultStyle:defaultStyle];
		if(barButtonItem) {
			[barButtonItems addObject:barButtonItem];
		}
	}
	
	if ([side isEqualToString:@"left"]) {
		[self.viewController.navigationItem setLeftBarButtonItems:barButtonItems animated:animated];
	}
	
	if ([side isEqualToString:@"right"]) {
		[self.viewController.navigationItem setRightBarButtonItems:barButtonItems animated:animated];
	}
}

- (NSArray *)resolveButtons:(id)buttons {
	if ([buttons isKindOfClass:[NSArray class]]) {
		return buttons;
	} else {
		return @[buttons];
	}
}

-(RNNUIBarButtonItem*)buildButton: (NSDictionary*)dictionary defaultStyle:(RNNButtonOptions *)defaultStyle {
	NSString* buttonId = dictionary[@"id"];
	NSString* title = [self getValue:dictionary[@"text"] withDefault:defaultStyle.text];
	NSDictionary* component = dictionary[@"component"];
	
	if (!buttonId) {
		@throw [NSException exceptionWithName:@"NSInvalidArgumentException" reason:[@"button id is not specified " stringByAppendingString:title] userInfo:nil];
	}
	
	UIImage* iconImage = nil;
	id icon = [self getValue:dictionary[@"icon"] withDefault:defaultStyle.icon];
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
	
	NSNumber *enabled = [self getValue:dictionary[@"enabled"] withDefault:defaultStyle.enabled];
	BOOL enabledBool = enabled ? [enabled boolValue] : YES;
	[barButtonItem setEnabled:enabledBool];
	
	NSMutableDictionary* textAttributes = [[NSMutableDictionary alloc] init];
	NSMutableDictionary* disabledTextAttributes = [[NSMutableDictionary alloc] init];
	
	UIColor* color = [self color:dictionary[@"color"] defaultColor:defaultStyle.color];
	UIColor* disabledColor = [self color:dictionary[@"disabledColor"] defaultColor:defaultStyle.disabledColor];
	if (!enabledBool && disabledColor) {
		color = disabledColor;
		[disabledTextAttributes setObject:disabledColor forKey:NSForegroundColorAttributeName];
	}
	
	if (color) {
		[textAttributes setObject:color forKey:NSForegroundColorAttributeName];
		[barButtonItem setImage:[[iconImage withTintColor:color] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
	}
	
	NSNumber* fontSize = [self fontSize:dictionary[@"fontSize"] defaultFontSize:defaultStyle.fontSize];
	NSString* fontFamily = [self fontFamily:dictionary[@"fontFamily"] defaultFontFamily:defaultStyle.fontFamily];
	UIFont *font = nil;
	if (fontFamily) {
		font = [UIFont fontWithName:fontFamily size:[fontSize floatValue]];
	} else {
		font = [UIFont systemFontOfSize:[fontSize floatValue]];
	}
	[textAttributes setObject:font forKey:NSFontAttributeName];
	[disabledTextAttributes setObject:font forKey:NSFontAttributeName];
	
	[barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
	[barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
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

- (id)getValue:(id)value withDefault:(id)defaultValue {
	return value ? value : defaultValue;
}

-(void)onButtonPress:(RNNUIBarButtonItem*)barButtonItem {
	[self.viewController.eventEmitter sendOnNavigationButtonPressed:self.viewController.componentId buttonId:barButtonItem.buttonId];
}

@end
