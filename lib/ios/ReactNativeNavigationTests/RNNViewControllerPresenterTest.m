#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNViewControllerPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNReactView.h"

@interface RNNViewControllerPresenterTest : XCTestCase

@property (nonatomic, strong) RNNViewControllerPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) UIViewController *bindedViewController;

@end

@implementation RNNViewControllerPresenterTest

- (void)setUp {
    [super setUp];
	self.uut = [[RNNViewControllerPresenter alloc] init];
	self.bindedViewController = [OCMockObject partialMockForObject:[UIViewController new]];
	[self.uut bindViewController:self.bindedViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_backgroundImageDefaultNilShouldNotAddSubview {
	[self.uut applyOptions:self.options];
	XCTAssertTrue((self.bindedViewController.view.subviews.count) == 0);
}

- (void)testApplyOptions_topBarPrefersLargeTitleDefaultFalse {
	[self.uut applyOptions:self.options];
	
	XCTAssertTrue(self.bindedViewController.navigationItem.largeTitleDisplayMode == UINavigationItemLargeTitleDisplayModeNever);
}

- (void)testApplyOptions_layoutBackgroundColorDefaultWhiteColor {
	[self.uut applyOptions:self.options];
	XCTAssertNil(self.bindedViewController.view.backgroundColor);
}

- (void)testApplyOptions_statusBarBlurDefaultFalse {
	[self.uut applyOptions:self.options];
	XCTAssertNil([self.bindedViewController.view viewWithTag:BLUR_STATUS_TAG]);
}

- (void)testApplyOptions_statusBarStyleDefaultStyle {
	[self.uut applyOptions:self.options];
	XCTAssertTrue([self.bindedViewController preferredStatusBarStyle] == UIStatusBarStyleDefault);
}

- (void)testApplyOptions_backButtonVisibleDefaultTrue {
	[self.uut applyOptions:self.options];
	XCTAssertFalse(self.bindedViewController.navigationItem.hidesBackButton);
}

- (void)testApplyOptions_drawBehindTabBarTrueWhenVisibleFalse {
	self.options.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
	[[(id)self.bindedViewController expect] rnn_setDrawBehindTabBar:YES];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.bindedViewController verify];
}

- (void)testApplyOptions_setOverlayTouchOutsideIfHasValue {
    self.options.overlay.interceptTouchOutside = [[Bool alloc] initWithBOOL:YES];
    [[(id)self.bindedViewController expect] rnn_setInterceptTouchOutside:YES];
    [self.uut applyOptions:self.options];
    [(id)self.bindedViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalPresentetionStyleWithDefault {
	[[(id)self.bindedViewController expect] rnn_setModalPresentationStyle:UIModalPresentationFullScreen];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.bindedViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalTransitionStyleWithDefault {
	[[(id)self.bindedViewController expect] rnn_setModalTransitionStyle:UIModalTransitionStyleCoverVertical];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.bindedViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalPresentetionStyleWithValue {
	self.options.modalPresentationStyle = [[Text alloc] initWithValue:@"overCurrentContext"];
	[[(id)self.bindedViewController expect] rnn_setModalPresentationStyle:UIModalPresentationOverCurrentContext];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.bindedViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalTransitionStyleWithValue {
	self.options.modalTransitionStyle = [[Text alloc] initWithValue:@"crossDissolve"];
	[[(id)self.bindedViewController expect] rnn_setModalTransitionStyle:UIModalTransitionStyleCrossDissolve];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.bindedViewController verify];
}

-(void)testApplyOptionsOnInit_TopBarDrawUnder_true {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(1)];
    
    [[(id)self.bindedViewController expect] rnn_setDrawBehindTopBar:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.bindedViewController verify];
}

-(void)testApplyOptionsOnInit_TopBarDrawUnder_false {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(0)];
    
    [[(id)self.bindedViewController expect] rnn_setDrawBehindTopBar:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.bindedViewController verify];
}

-(void)testApplyOptionsOnInit_BottomTabsDrawUnder_true {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(1)];
    
    [[(id)self.bindedViewController expect] rnn_setDrawBehindTabBar:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.bindedViewController verify];
}

-(void)testApplyOptionsOnInit_BottomTabsDrawUnder_false {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(0)];
    
    [[(id)self.bindedViewController expect] rnn_setDrawBehindTabBar:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.bindedViewController verify];
}

@end
