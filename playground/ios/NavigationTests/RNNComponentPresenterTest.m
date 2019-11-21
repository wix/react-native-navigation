#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNComponentPresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNComponentViewController.h"
#import "UIViewController+LayoutProtocol.h"
#import "RNNTitleViewHelper.h"

@interface RNNComponentPresenterTest : XCTestCase

@property (nonatomic, strong) RNNComponentPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) UIViewController *boundViewController;
@property (nonatomic, strong) RNNReactComponentRegistry *componentRegistry;

@end

@implementation RNNComponentPresenterTest

- (void)setUp {
    [super setUp];
	self.componentRegistry = [OCMockObject partialMockForObject:[RNNReactComponentRegistry new]];
	self.uut = [[RNNComponentPresenter alloc] initWithComponentRegistry:self.componentRegistry:[[RNNNavigationOptions alloc] initEmptyOptions]];
	self.boundViewController = [OCMockObject partialMockForObject:[RNNComponentViewController new]];
	[self.uut boundViewController:self.boundViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_backgroundImageDefaultNilShouldNotAddSubview {
	[self.uut applyOptions:self.options];
	XCTAssertTrue((self.boundViewController.view.subviews.count) == 0);
}

- (void)testApplyOptions_topBarPrefersLargeTitleDefaultFalse {
	[self.uut applyOptions:self.options];
	
	XCTAssertTrue(self.boundViewController.navigationItem.largeTitleDisplayMode == UINavigationItemLargeTitleDisplayModeNever);
}

- (void)testApplyOptions_layoutBackgroundColorDefaultWhiteColor {
	[self.uut applyOptions:self.options];
	XCTAssertNil(self.boundViewController.view.backgroundColor);
}

- (void)testApplyOptions_statusBarBlurDefaultFalse {
	[self.uut applyOptions:self.options];
	XCTAssertNil([self.boundViewController.view viewWithTag:BLUR_STATUS_TAG]);
}

- (void)testApplyOptions_statusBarStyleDefaultStyle {
	[self.uut applyOptions:self.options];
	XCTAssertTrue([self.boundViewController preferredStatusBarStyle] == UIStatusBarStyleDefault);
}

- (void)testApplyOptions_backButtonVisibleDefaultTrue {
	[self.uut applyOptions:self.options];
	XCTAssertFalse(self.boundViewController.navigationItem.hidesBackButton);
}

- (void)testApplyOptions_drawBehindTabBarTrueWhenVisibleFalse {
	self.options.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
	[[(id) self.boundViewController expect] setDrawBehindTabBar:YES];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptions_setOverlayTouchOutsideIfHasValue {
    self.options.overlay.interceptTouchOutside = [[Bool alloc] initWithBOOL:YES];
    [(UIViewController *) [(id) self.boundViewController expect] setInterceptTouchOutside:YES];
    [self.uut applyOptions:self.options];
    [(id)self.boundViewController verify];
}

- (void)testBindViewControllerShouldCreateNavigationButtonsCreator {
	RNNComponentPresenter* presenter = [[RNNComponentPresenter alloc] init];
	[presenter boundViewController:self.boundViewController];
	XCTAssertNotNil(presenter.navigationButtons);
}

- (void)testApplyOptionsOnInit_shouldSetModalPresentationStyleWithDefault {
    [(UIViewController *) [(id) self.boundViewController expect] setModalPresentationStyle:UIModalPresentationFullScreen];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalTransitionStyleWithDefault {
	[(UIViewController *) [(id) self.boundViewController expect] setModalTransitionStyle:UIModalTransitionStyleCoverVertical];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalPresentationStyleWithValue {
	self.options.modalPresentationStyle = [[Text alloc] initWithValue:@"overCurrentContext"];
    [(UIViewController *) [(id) self.boundViewController expect] setModalPresentationStyle:UIModalPresentationOverCurrentContext];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_shouldSetModalTransitionStyleWithValue {
	self.options.modalTransitionStyle = [[Text alloc] initWithValue:@"crossDissolve"];
	[(UIViewController *) [(id) self.boundViewController expect] setModalTransitionStyle:UIModalTransitionStyleCrossDissolve];
	[self.uut applyOptionsOnInit:self.options];
	[(id)self.boundViewController verify];
}

-(void)testApplyOptionsOnInit_TopBarDrawUnder_true {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(1)];

	[[(id) self.boundViewController expect] setDrawBehindTopBar:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

-(void)testApplyOptionsOnInit_TopBarDrawUnder_false {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(0)];

	[[(id) self.boundViewController expect] setDrawBehindTopBar:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

-(void)testApplyOptionsOnInit_BottomTabsDrawUnder_true {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(1)];

	[[(id) self.boundViewController expect] setDrawBehindTabBar:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

-(void)testApplyOptionsOnInit_BottomTabsDrawUnder_false {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(0)];

	[[(id) self.boundViewController expect] setDrawBehindTabBar:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testReactViewShouldBeReleasedOnDealloc {
	RNNComponentViewController* bindViewController = [RNNComponentViewController new];
	bindViewController.layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
	[self.uut boundViewController:bindViewController];
	
	self.options.topBar.title.component = [[RNNComponentOptions alloc] initWithDict:@{@"name": @"componentName"}];
	
	[[(id)self.componentRegistry expect] clearComponentsForParentId:self.uut.boundComponentId];
	self.uut = nil;
	[(id)self.componentRegistry verify];
}

- (void)testBindViewControllerShouldSetBoundComponentId {
	RNNComponentViewController* bindViewController = [RNNComponentViewController new];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.componentId = @"componentId";
	bindViewController.layoutInfo = layoutInfo;

	[self.uut boundViewController:bindViewController];
	XCTAssertEqual(self.uut.boundComponentId, @"componentId");
}

- (void)testRenderComponentsCreateReactViewWithBoundComponentId {
	RNNComponentViewController* boundViewController = [RNNComponentViewController new];
	RNNLayoutInfo* layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
	boundViewController.layoutInfo = layoutInfo;
	[self.uut boundViewController:boundViewController];
	
	self.options.topBar.title.component = [[RNNComponentOptions alloc] initWithDict:@{@"name": @"titleComponent"}];
	
	[[(id)self.componentRegistry expect] createComponentIfNotExists:self.options.topBar.title.component parentComponentId:self.uut.boundComponentId reactViewReadyBlock:[OCMArg any]];
	[self.uut renderComponents:self.options perform:nil];
	[(id)self.componentRegistry verify];
	
	
	XCTAssertEqual(self.uut.boundComponentId, @"componentId");
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBoundViewController_withTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	[[(id) self.boundViewController expect] setBackButtonIcon:nil withColor:nil title:title.get];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBoundViewController_withHideTitle {
	Text* title = [[Text alloc] initWithValue:@"Title"];
	self.options.topBar.backButton.title = title;
	self.options.topBar.backButton.showTitle = [[Bool alloc] initWithValue:@(0)];
	[[(id) self.boundViewController expect] setBackButtonIcon:nil withColor:nil title:@""];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBoundViewController_withIcon {
	Image* image = [[Image alloc] initWithValue:[UIImage new]];
	self.options.topBar.backButton.icon = image;
	[[(id) self.boundViewController expect] setBackButtonIcon:image.get withColor:nil title:nil];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnWillMoveToParent_shouldSetBackButtonOnBoundViewController_withDefaultValues {
	[[(id) self.boundViewController expect] setBackButtonIcon:nil withColor:nil title:nil];
	[self.uut applyOptionsOnWillMoveToParentViewController:self.options];
	[(id)self.boundViewController verify];
}

- (void)testRemoveTitleComponentIfNeeded_componentIsRemovedIfTitleTextIsDefined {
	id mockTitle = [OCMockObject niceMockForClass:[RNNReactView class]];
    OCMStub([self.componentRegistry createComponentIfNotExists:[OCMArg any] parentComponentId:[OCMArg any] reactViewReadyBlock:nil]).andReturn(mockTitle);

	RNNComponentOptions* component = [RNNComponentOptions new];
	component.name = [[Text alloc] initWithValue:@"componentName"];
	component.componentId = [[Text alloc] initWithValue:@"someId"];
	_options.topBar.title.component = component;

	[self.uut mergeOptions:_options resolvedOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
    XCTAssertNotNil(self.boundViewController.navigationItem.titleView);
	XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);

	[[mockTitle expect] removeFromSuperview];
    _options = [[RNNNavigationOptions alloc] initEmptyOptions];
    _options.topBar.title.text = [[Text alloc] initWithValue:@""];
	[self.uut mergeOptions:_options resolvedOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
    XCTAssertNotEqual(self.boundViewController.navigationItem.titleView, mockTitle);
	[mockTitle verify];
}

- (void)testRemoveTitleComponentIfNeeded_componentIsNotRemovedIfMergeOptionsIsCalledWithoutTitleText {
    id mockTitle = [OCMockObject niceMockForClass:[RNNReactView class]];
    OCMStub([self.componentRegistry createComponentIfNotExists:[OCMArg any] parentComponentId:[OCMArg any] reactViewReadyBlock:nil]).andReturn(mockTitle);

    RNNComponentOptions* component = [RNNComponentOptions new];
    component.name = [[Text alloc] initWithValue:@"componentName"];
    component.componentId = [[Text alloc] initWithValue:@"someId"];
    _options.topBar.title.component = component;

    [self.uut mergeOptions:_options resolvedOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
    XCTAssertNotNil(self.boundViewController.navigationItem.titleView);
    XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);


    _options = [[RNNNavigationOptions alloc] initEmptyOptions];
    _options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    [self.uut mergeOptions:_options resolvedOptions:[[RNNNavigationOptions alloc] initEmptyOptions]];
    XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);
}

- (RNNLayoutInfo *)createLayoutInfoWithComponentId:(NSString *)componentId {
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.componentId = componentId;
	return layoutInfo;
}

@end
