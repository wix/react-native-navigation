#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuPresenter

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
		
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	
	[sideMenuController side:MMDrawerSideLeft enabled:[options.sideMenu.left.enabled getWithDefaultValue:YES]];
	[sideMenuController side:MMDrawerSideRight enabled:[options.sideMenu.right.enabled getWithDefaultValue:YES]];
	
	[sideMenuController setShouldStretchLeftDrawer:[options.sideMenu.left.shouldStretchDrawer getWithDefaultValue:YES]];
	[sideMenuController setShouldStretchRightDrawer:[options.sideMenu.right.shouldStretchDrawer getWithDefaultValue:YES]];
	
	[sideMenuController setAnimationVelocityLeft:[options.sideMenu.left.animationVelocity getWithDefaultValue:840.0f]];
	[sideMenuController setAnimationVelocityRight:[options.sideMenu.right.animationVelocity getWithDefaultValue:840.0f]];
	
	[sideMenuController setAnimationType:[options.sideMenu.animationType getWithDefaultValue:nil]];
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];
	
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	if (initialOptions.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:initialOptions.sideMenu.left.width.get];
	}
	
	if (initialOptions.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:initialOptions.sideMenu.right.width.get];
	}

		[sideMenuController setOpenDrawerGestureModeMask:[[initialOptions.sideMenu.openGestureMode getWithDefaultValue:@(MMOpenDrawerGestureModeAll)] integerValue]];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	
	if (newOptions.sideMenu.left.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideLeft enabled:newOptions.sideMenu.left.enabled.get];
		[newOptions.sideMenu.left.enabled consume];
	}
	
	if (newOptions.sideMenu.right.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideRight enabled:newOptions.sideMenu.right.enabled.get];
		[newOptions.sideMenu.right.enabled consume];
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
