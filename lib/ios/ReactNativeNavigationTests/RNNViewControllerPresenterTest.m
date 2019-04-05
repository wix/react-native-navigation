#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNViewControllerPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNReactView.h"
#import "RNNRootViewController.h"

@interface RNNViewControllerPresenterTest : XCTestCase

@property (nonatomic, strong) RNNViewControllerPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) UIViewController *bindedViewController;
@property (nonatomic, strong) RNNReactComponentRegistry *componentRegistry;

@end

@implementation RNNViewControllerPresenterTest

- (void)setUp {
    [super setUp];
	self.componentRegistry = [OCMockObject partialMockForObject:[RNNReactComponentRegistry new]];
	self.uut = [[RNNViewControllerPresenter alloc] initWithComponentRegistry:self.componentRegistry];
	self.bindedViewController = [OCMockObject partialMockForObject:[RNNRootViewController new]];
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

- (void)testBindViewControllerShouldCreateNavigationButtonsCreator {
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
	[presenter bindViewController:self.bindedViewController];
	XCTAssertNotNil(presenter.navigationButtons);
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

- (void)testReactViewShouldBeReleasedOnDealloc {
	RNNRootViewController* bindViewController = [RNNRootViewController new];
	bindViewController.layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
	[self.uut bindViewController:bindViewController];
	
	self.options.topBar.title.component = [[RNNComponentOptions alloc] initWithDict:@{@"name": @"componentName"}];
	
	[[(id)self.componentRegistry expect] clearComponentsForParentId:self.uut.bindedComponentId];
	self.uut = nil;
	[(id)self.componentRegistry verify];
}

- (void)testBindViewControllerShouldSetBindedComponentId {
	RNNRootViewController* bindViewController = [RNNRootViewController new];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.componentId = @"componentId";
	bindViewController.layoutInfo = layoutInfo;
	
	[self.uut bindViewController:bindViewController];
	XCTAssertEqual(self.uut.bindedComponentId, @"componentId");
}

- (void)testRenderComponentsCreateReactViewWithBindedComponentId {
	RNNRootViewController* bindedViewController = [RNNRootViewController new];
	RNNLayoutInfo* layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
	bindedViewController.layoutInfo = layoutInfo;
	
	[self.uut bindViewController:bindedViewController];
	
	self.options.topBar.title.component = [[RNNComponentOptions alloc] initWithDict:@{@"name": @"titleComponent"}];
	
	[[(id)self.componentRegistry expect] createComponentIfNotExists:self.options.topBar.title.component parentComponentId:self.uut.bindedComponentId reactViewReadyBlock:[OCMArg any]];
	[self.uut renderComponents:self.options perform:nil];
	[(id)self.componentRegistry verify];
	
	
	XCTAssertEqual(self.uut.bindedComponentId, @"componentId");
}



- (RNNLayoutInfo *)createLayoutInfoWithComponentId:(NSString *)componentId {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.componentId = @"componentId";
	return layoutInfo;
}

@end
