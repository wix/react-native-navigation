#import "RNNNavigationButtons.h"
#import "RNNUIBarButtonItem.h"
#import <React/RCTConvert.h>
#import "RCTHelpers.h"

@interface RNNNavigationButtons()
@property (weak, nonatomic) RNNRootViewController* viewController;

@end

@implementation RNNNavigationButtons

-(instancetype)init {
	return [super init];
}

-(instancetype)initWithViewController:(RNNRootViewController*)viewController {
	self = [super init];
	
	self.viewController = viewController;
	
	return self;
}

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons {
	if(leftButtons) {
		[self setButtons:leftButtons side:@"left" animated:NO];
	}
	
	if(rightButtons) {
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
	
	if ([side isEqualToString:@"left"])
	{
		[self.viewController.navigationItem setLeftBarButtonItems:barButtonItems animated:animated];
	}
	
	if ([side isEqualToString:@"right"])
	{
		[self.viewController.navigationItem setRightBarButtonItems:barButtonItems animated:animated];
	}
}

-(RNNUIBarButtonItem*)buildButton: (NSDictionary*)dictionary {
	NSString* buttonId = dictionary[@"id"];
	NSString* title = dictionary[@"title"];
	
	if(!buttonId){
		@throw [NSException exceptionWithName:@"NSInvalidArgumentException" reason:[@"button id is not specified " stringByAppendingString:title] userInfo:nil];
	}
	
	UIImage* iconImage = nil;
	id icon = dictionary[@"icon"];
	if (icon) {
		iconImage = [RCTConvert UIImage:icon];
	}
	
	RNNUIBarButtonItem *barButtonItem;
	if (iconImage) {
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
	
	NSNumber *disabled = dictionary[@"disabled"];
	BOOL disabledBool = disabled ? [disabled boolValue] : NO;
	if (disabledBool) {
		[barButtonItem setEnabled:NO];
	}
	
	NSNumber *disableIconTintString = dictionary[@"disableIconTint"];
	BOOL disableIconTint = disableIconTintString ? [disableIconTintString boolValue] : NO;
	if (disableIconTint) {
		[barButtonItem setImage:[barButtonItem.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
	}
	
	NSString *testID = dictionary[@"testID"];
	if (testID)
	{
		barButtonItem.accessibilityIdentifier = testID;
	}
	
	return barButtonItem;
}

-(void)onButtonPress:(RNNUIBarButtonItem*)barButtonItem
{
	[self.viewController.eventEmitter sendOnNavigationButtonPressed:self.viewController.containerId buttonId:barButtonItem.buttonId];
}
@end
