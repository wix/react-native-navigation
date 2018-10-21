#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuPresenter

- (void)applyOptions:(RNNNavigationOptions *)initialOptions {
	[super applyOptions:initialOptions];
	
	RNNNavigationOptions* options = [initialOptions withDefault:self.defaultOptions];
	
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	
	[sideMenuController side:MMDrawerSideLeft enabled:[options.sideMenu.left.enabled getWithDefaultValue:YES]];
	[sideMenuController side:MMDrawerSideRight enabled:[options.sideMenu.right.enabled getWithDefaultValue:YES]];
	
	[sideMenuController setShouldStretchLeftDrawer:[options.sideMenu.left.shouldStretchDrawer getWithDefaultValue:YES]];
	[sideMenuController setShouldStretchRightDrawer:[options.sideMenu.right.shouldStretchDrawer getWithDefaultValue:YES]];
	
	[sideMenuController setAnimationVelocityLeft:[options.sideMenu.left.animationVelocity getWithDefaultValue:840.0f]];
	[sideMenuController setAnimationVelocityRight:[options.sideMenu.right.animationVelocity getWithDefaultValue:840.0f]];
	
	if (options.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:options.sideMenu.left.width.get];
	}
	
	if (options.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:options.sideMenu.right.width.get];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
	[super mergeOptions:options resolvedOptions:resolvedOptions];
	
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	
	if (options.sideMenu.left.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideLeft enabled:options.sideMenu.left.enabled.get];
		[options.sideMenu.left.enabled consume];
	}
	
	if (options.sideMenu.right.enabled.hasValue) {
		[sideMenuController side:MMDrawerSideRight enabled:options.sideMenu.right.enabled.get];
		[options.sideMenu.right.enabled consume];
	}
	
	if (options.sideMenu.left.visible.hasValue) {
		[sideMenuController side:MMDrawerSideLeft visible:options.sideMenu.left.visible.get];
		[options.sideMenu.left.visible consume];
	}
	
	if (options.sideMenu.right.visible.hasValue) {
		[sideMenuController side:MMDrawerSideRight visible:options.sideMenu.right.visible.get];
		[options.sideMenu.right.visible consume];
	}
	
	if (options.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:options.sideMenu.left.width.get];
	}
	
	if (options.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:options.sideMenu.right.width.get];
	}
	
	if (options.sideMenu.left.shouldStretchDrawer.hasValue) {
		sideMenuController.shouldStretchLeftDrawer = options.sideMenu.left.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.right.shouldStretchDrawer.hasValue) {
		sideMenuController.shouldStretchRightDrawer = options.sideMenu.right.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.left.animationVelocity.hasValue) {
		sideMenuController.animationVelocityLeft = options.sideMenu.left.animationVelocity.get;
	}
	
	if (options.sideMenu.right.animationVelocity.hasValue) {
		sideMenuController.animationVelocityRight = options.sideMenu.right.animationVelocity.get;
	}
}

@end
