#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuPresenter

- (void)applyOptions:(RNNNavigationOptions *)initialOptions {
	[super applyOptions:initialOptions];
	
	RNNNavigationOptions* options = [initialOptions withDefault:self.defaultOptions];
	
	RNNSideMenuController* sideMenuController = self.bindedViewController;
	
	[sideMenuController side:MMDrawerSideLeft enabled:[options.sideMenu.left.enabled getWithDefaultValue:YES]];
	[sideMenuController side:MMDrawerSideRight enabled:[options.sideMenu.right.enabled getWithDefaultValue:YES]];
	
	[sideMenuController setShouldStretchDrawer:[options.sideMenu.shouldStretchDrawer getWithDefaultValue:YES]];
	[sideMenuController setAnimationVelocity:[options.sideMenu.animationVelocity getWithDefaultValue:840.0f]];
	
	if (options.sideMenu.left.width.hasValue) {
		[sideMenuController side:MMDrawerSideLeft width:options.sideMenu.left.width.get];
	}
	
	if (options.sideMenu.right.width.hasValue) {
		[sideMenuController side:MMDrawerSideRight width:options.sideMenu.right.width.get];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[super mergeOptions:options];
	
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
	
	if (options.sideMenu.shouldStretchDrawer.hasValue) {
		sideMenuController.shouldStretchDrawer = options.sideMenu.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.animationVelocity.hasValue) {
		sideMenuController.animationVelocity = options.sideMenu.animationVelocity.get;
	}
}

@end
