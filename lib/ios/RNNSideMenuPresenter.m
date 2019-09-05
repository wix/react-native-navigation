#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuPresenter

-(instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super initWithDefaultOptions:defaultOptions];
	return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
	RNNSideMenuController* sideMenuController = self.boundViewController;

	[sideMenuController side:MMDrawerSideLeft enabled:[withDefault.sideMenu.left.enabled getWithDefaultValue:YES]];
	[sideMenuController side:MMDrawerSideRight enabled:[withDefault.sideMenu.right.enabled getWithDefaultValue:YES]];
	
	[sideMenuController setShouldStretchLeftDrawer:[withDefault.sideMenu.left.shouldStretchDrawer getWithDefaultValue:YES]];
	[sideMenuController setShouldStretchRightDrawer:[withDefault.sideMenu.right.shouldStretchDrawer getWithDefaultValue:YES]];
	
	[sideMenuController setAnimationVelocityLeft:[withDefault.sideMenu.left.animationVelocity getWithDefaultValue:840.0f]];
	[sideMenuController setAnimationVelocityRight:[withDefault.sideMenu.right.animationVelocity getWithDefaultValue:840.0f]];
	
	[sideMenuController setAnimationType:[withDefault.sideMenu.animationType getWithDefaultValue:nil]];
	
	if (withDefault.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:withDefault.sideMenu.left.width.get];
	}
	
	if (withDefault.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:withDefault.sideMenu.right.width.get];
	}
	
	if (withDefault.sideMenu.left.visible.hasValue) {
		[sideMenuController side:MMDrawerSideLeft visible:withDefault.sideMenu.left.visible.get];
		[withDefault.sideMenu.left.visible consume];
	}
	
	if (withDefault.sideMenu.right.visible.hasValue) {
		[sideMenuController side:MMDrawerSideRight visible:withDefault.sideMenu.right.visible.get];
		[withDefault.sideMenu.right.visible consume];
	}
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];

	RNNNavigationOptions *withDefault = [initialOptions withDefault:[self defaultOptions]];
	RNNSideMenuController* sideMenuController = self.boundViewController;
	if (withDefault.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:withDefault.sideMenu.left.width.get];
	}
	
	if (withDefault.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:withDefault.sideMenu.right.width.get];
	}

		[sideMenuController setOpenDrawerGestureModeMask:[[withDefault.sideMenu.openGestureMode getWithDefaultValue:@(MMOpenDrawerGestureModeAll)] integerValue]];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions];
	
	RNNSideMenuController* sideMenuController = self.boundViewController;
	
	if (newOptions.sideMenu.left.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideLeft enabled:newOptions.sideMenu.left.enabled.get];
	}
	
	if (newOptions.sideMenu.right.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideRight enabled:newOptions.sideMenu.right.enabled.get];
	}
	
	if (newOptions.sideMenu.left.visible.hasValue) {
		[sideMenuController side:MMDrawerSideLeft visible:newOptions.sideMenu.left.visible.get];
		[newOptions.sideMenu.left.visible consume];
	}
	
	if (newOptions.sideMenu.right.visible.hasValue) {
		[sideMenuController side:MMDrawerSideRight visible:newOptions.sideMenu.right.visible.get];
		[newOptions.sideMenu.right.visible consume];
	}
	
	if (newOptions.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:newOptions.sideMenu.left.width.get];
	}
	
	if (newOptions.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:newOptions.sideMenu.right.width.get];
	}
	
	if (newOptions.sideMenu.left.shouldStretchDrawer.hasValue) {
		sideMenuController.shouldStretchLeftDrawer = newOptions.sideMenu.left.shouldStretchDrawer.get;
	}
	
	if (newOptions.sideMenu.right.shouldStretchDrawer.hasValue) {
		sideMenuController.shouldStretchRightDrawer = newOptions.sideMenu.right.shouldStretchDrawer.get;
	}
	
	if (newOptions.sideMenu.left.animationVelocity.hasValue) {
		sideMenuController.animationVelocityLeft = newOptions.sideMenu.left.animationVelocity.get;
	}
	
	if (newOptions.sideMenu.right.animationVelocity.hasValue) {
		sideMenuController.animationVelocityRight = newOptions.sideMenu.right.animationVelocity.get;
	}
	
	if (newOptions.sideMenu.animationType.hasValue) {
		[sideMenuController setAnimationType:newOptions.sideMenu.animationType.get];
	}
}

@end
