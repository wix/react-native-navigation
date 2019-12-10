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
	RNNSideMenuController* sideMenu = self.boundViewController;

	[sideMenu side:MMDrawerSideLeft enabled:[withDefault.sideMenu.left.enabled getWithDefaultValue:YES]];
	[sideMenu side:MMDrawerSideRight enabled:[withDefault.sideMenu.right.enabled getWithDefaultValue:YES]];
	
	[sideMenu setShouldStretchLeftDrawer:[withDefault.sideMenu.left.shouldStretchDrawer getWithDefaultValue:YES]];
	[sideMenu setShouldStretchRightDrawer:[withDefault.sideMenu.right.shouldStretchDrawer getWithDefaultValue:YES]];
	
	[sideMenu setAnimationVelocityLeft:[withDefault.sideMenu.left.animationVelocity getWithDefaultValue:840.0f]];
	[sideMenu setAnimationVelocityRight:[withDefault.sideMenu.right.animationVelocity getWithDefaultValue:840.0f]];
	
	[sideMenu setAnimationType:[withDefault.sideMenu.animationType getWithDefaultValue:nil]];
	
	if (withDefault.sideMenu.left.width.hasValue) {
		[sideMenu side:MMDrawerSideLeft width:withDefault.sideMenu.left.width.get];
	}
	
	if (withDefault.sideMenu.right.width.hasValue) {
		[sideMenu side:MMDrawerSideRight width:withDefault.sideMenu.right.width.get];
	}
	
	if (withDefault.sideMenu.left.visible.hasValue) {
		[sideMenu side:MMDrawerSideLeft visible:withDefault.sideMenu.left.visible.get];
		[withDefault.sideMenu.left.visible consume];
	}
	
	if (withDefault.sideMenu.right.visible.hasValue) {
		[sideMenu side:MMDrawerSideRight visible:withDefault.sideMenu.right.visible.get];
		[withDefault.sideMenu.right.visible consume];
	}
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
	[super applyOptionsOnInit:initialOptions];

	RNNNavigationOptions *withDefault = [initialOptions withDefault:[self defaultOptions]];
	RNNSideMenuController* sideMenu = self.boundViewController;
	if (withDefault.sideMenu.left.width.hasValue) {
		[sideMenu side:MMDrawerSideLeft width:withDefault.sideMenu.left.width.get];
	}
	
	if (withDefault.sideMenu.right.width.hasValue) {
		[sideMenu side:MMDrawerSideRight width:withDefault.sideMenu.right.width.get];
	}

		[sideMenu setOpenDrawerGestureModeMask:[[withDefault.sideMenu.openGestureMode getWithDefaultValue:@(MMOpenDrawerGestureModeAll)] integerValue]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
	RNNSideMenuController* sideMenu = self.boundViewController;
	
	if (options.sideMenu.left.enabled.hasValue) {
		[sideMenu side:MMDrawerSideLeft enabled:options.sideMenu.left.enabled.get];
	}
	
	if (options.sideMenu.right.enabled.hasValue) {
		[sideMenu side:MMDrawerSideRight enabled:options.sideMenu.right.enabled.get];
	}
	
	if (options.sideMenu.left.visible.hasValue) {
		[sideMenu side:MMDrawerSideLeft visible:options.sideMenu.left.visible.get];
		[options.sideMenu.left.visible consume];
	}
	
	if (options.sideMenu.right.visible.hasValue) {
		[sideMenu side:MMDrawerSideRight visible:options.sideMenu.right.visible.get];
		[options.sideMenu.right.visible consume];
	}
	
	if (options.sideMenu.left.width.hasValue) {
		[sideMenu side:MMDrawerSideLeft width:options.sideMenu.left.width.get];
	}
	
	if (options.sideMenu.right.width.hasValue) {
		[sideMenu side:MMDrawerSideRight width:options.sideMenu.right.width.get];
	}
	
	if (options.sideMenu.left.shouldStretchDrawer.hasValue) {
		sideMenu.shouldStretchLeftDrawer = options.sideMenu.left.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.right.shouldStretchDrawer.hasValue) {
		sideMenu.shouldStretchRightDrawer = options.sideMenu.right.shouldStretchDrawer.get;
	}
	
	if (options.sideMenu.left.animationVelocity.hasValue) {
		sideMenu.animationVelocityLeft = options.sideMenu.left.animationVelocity.get;
	}
	
	if (options.sideMenu.right.animationVelocity.hasValue) {
		sideMenu.animationVelocityRight = options.sideMenu.right.animationVelocity.get;
	}
	
	if (options.sideMenu.animationType.hasValue) {
		[sideMenu setAnimationType:options.sideMenu.animationType.get];
	}
}

@end
