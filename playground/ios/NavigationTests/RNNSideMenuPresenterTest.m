#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"

@interface RNNSideMenuPresenterTest : XCTestCase

@property (nonatomic, strong) RNNSideMenuPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) id bindedViewController;

@end

@implementation RNNSideMenuPresenterTest

- (void)setUp {
    [super setUp];
	self.uut = [[RNNSideMenuPresenter alloc] init];
	self.bindedViewController = [OCMockObject partialMockForObject:[RNNSideMenuController new]];
    [self.uut boundViewController:self.bindedViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptionsShouldSetDefaultValues {
	[[self.bindedViewController expect] side:MMDrawerSideLeft enabled:YES];
	[[self.bindedViewController expect] side:MMDrawerSideRight enabled:YES];
	[[self.bindedViewController expect] setShouldStretchLeftDrawer:YES];
	[[self.bindedViewController expect] setShouldStretchRightDrawer:YES];
	[[self.bindedViewController expect] setAnimationVelocityLeft:840.0f];
	[[self.bindedViewController expect] setAnimationVelocityRight:840.0f];
	[[self.bindedViewController reject] side:MMDrawerSideLeft width:0];
	[[self.bindedViewController reject] side:MMDrawerSideRight width:0];
  	[[self.bindedViewController expect] setAnimationType:nil];
    
	[self.uut applyOptions:self.options];

	[self.bindedViewController verify];
}

- (void)testApplyOptionsShouldSetInitialValues {
	self.options.sideMenu.left.enabled = [[Bool alloc] initWithBOOL:NO];
	self.options.sideMenu.right.enabled = [[Bool alloc] initWithBOOL:NO];
	self.options.sideMenu.left.shouldStretchDrawer = [[Bool alloc] initWithBOOL:NO];
	self.options.sideMenu.right.shouldStretchDrawer = [[Bool alloc] initWithBOOL:NO];
	self.options.sideMenu.right.animationVelocity = [[Double alloc] initWithValue:@(100.0f)];
	self.options.sideMenu.left.animationVelocity = [[Double alloc] initWithValue:@(100.0f)];
	
	[[self.bindedViewController expect] side:MMDrawerSideLeft enabled:NO];
	[[self.bindedViewController expect] side:MMDrawerSideRight enabled:NO];
	[[self.bindedViewController expect] setShouldStretchLeftDrawer:NO];
	[[self.bindedViewController expect] setShouldStretchRightDrawer:NO];
	[[self.bindedViewController expect] setAnimationVelocityLeft:100.0f];
	[[self.bindedViewController expect] setAnimationVelocityRight:100.0f];
	
	[self.uut applyOptions:self.options];
	
	[self.bindedViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetWidthOptions {
	self.options.sideMenu.right.width = [[Double alloc] initWithValue:@(100.0f)];
	self.options.sideMenu.left.width = [[Double alloc] initWithValue:@(100.0f)];

	[[self.bindedViewController expect] side:MMDrawerSideLeft width:100.0f];
	[[self.bindedViewController expect] side:MMDrawerSideRight width:100.0f];
	
	[self.uut applyOptionsOnInit:self.options];
	
	[self.bindedViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetDefaultDrawerGestureMode {
	[[self.bindedViewController expect] setOpenDrawerGestureModeMask:MMOpenDrawerGestureModeAll];
	[self.uut applyOptionsOnInit:self.options];
	[self.bindedViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetBezelDrawerGestureMode {
	self.options.sideMenu.openGestureMode = [[SideMenuOpenMode alloc] initWithValue:@(MMOpenDrawerGestureModeNone)];
	[[self.bindedViewController expect] setOpenDrawerGestureModeMask:MMOpenDrawerGestureModeNone];
	[self.uut applyOptionsOnInit:self.options];
	[self.bindedViewController verify];
}

@end
