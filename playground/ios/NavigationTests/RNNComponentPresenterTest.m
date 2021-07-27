#import "RNNComponentPresenter.h"
#import "RNNComponentViewController.h"
#import "RNNReactTitleView.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNComponentPresenterTest : XCTestCase

@property(nonatomic, strong) RNNComponentPresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) UIViewController *boundViewController;
@property(nonatomic, strong) RNNReactComponentRegistry *componentRegistry;
@property(nonatomic, strong) id buttonsPresenter;

@end

@implementation RNNComponentPresenterTest

- (void)setUp {
    [super setUp];
    self.buttonsPresenter = [OCMockObject niceMockForClass:[RNNButtonsPresenter class]];
    self.componentRegistry = [OCMockObject partialMockForObject:[RNNReactComponentRegistry new]];
    self.uut =
        [[RNNComponentPresenter alloc] initWithComponentRegistry:self.componentRegistry
                                                  defaultOptions:[RNNNavigationOptions emptyOptions]
                                                buttonsPresenter:self.buttonsPresenter];
    self.boundViewController = [OCMockObject partialMockForObject:[RNNComponentViewController new]];
    [self.uut bindViewController:self.boundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
}

- (void)testApplyOptions_backgroundImageDefaultNilShouldNotAddSubview {
    [self.uut applyOptions:self.options];
    XCTAssertTrue((self.boundViewController.view.subviews.count) == 0);
}

- (void)testApplyOptionsOnInit_topBarPrefersLargeTitleDefaultFalse {
    [self.uut applyOptionsOnInit:self.options];

    XCTAssertTrue(self.boundViewController.navigationItem.largeTitleDisplayMode ==
                  UINavigationItemLargeTitleDisplayModeNever);
}

- (void)testApplyOptions_layoutBackgroundColorDefaultSystemColor {
    [self.uut applyOptions:self.options];
    XCTAssertEqual(UIColor.systemBackgroundColor, self.boundViewController.view.backgroundColor);
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

- (void)testApplyOptions_defaultAnimateLeftButtonsFalse {
    RNNButtonOptions *button = [RNNButtonOptions new];
    self.options.topBar.leftButtons = @[ button ];
    [[self.buttonsPresenter expect] applyLeftButtons:self.options.topBar.leftButtons
                                        defaultColor:OCMArg.any
                                defaultDisabledColor:OCMArg.any
                                            animated:NO];
    [self.uut applyOptions:self.options];
    [self.buttonsPresenter verify];
}

- (void)testApplyOptions_defaultAnimateRightButtonsFalse {
    RNNButtonOptions *button = [RNNButtonOptions new];
    self.options.topBar.rightButtons = @[ button ];
    [[self.buttonsPresenter expect] applyRightButtons:self.options.topBar.rightButtons
                                         defaultColor:OCMArg.any
                                 defaultDisabledColor:OCMArg.any
                                             animated:NO];
    [self.uut applyOptions:self.options];
    [self.buttonsPresenter verify];
}

- (void)testApplyOptions_animateLeftButtons {
    self.options.topBar.animateLeftButtons = [Bool withValue:YES];
    RNNButtonOptions *button = [RNNButtonOptions new];
    self.options.topBar.leftButtons = @[ button ];
    [[self.buttonsPresenter expect] applyLeftButtons:self.options.topBar.leftButtons
                                        defaultColor:OCMArg.any
                                defaultDisabledColor:OCMArg.any
                                            animated:YES];
    [self.uut applyOptions:self.options];
    [self.buttonsPresenter verify];
}

- (void)testApplyOptions_animateRightButtons {
    self.options.topBar.animateRightButtons = [Bool withValue:YES];
    RNNButtonOptions *button = [RNNButtonOptions new];
    self.options.topBar.rightButtons = @[ button ];
    [[self.buttonsPresenter expect] applyRightButtons:self.options.topBar.rightButtons
                                         defaultColor:OCMArg.any
                                 defaultDisabledColor:OCMArg.any
                                             animated:YES];
    [self.uut applyOptions:self.options];
    [self.buttonsPresenter verify];
}

- (void)testMergeOptions_animateLeftButtons {
    RNNNavigationOptions *mergeOptions = RNNNavigationOptions.emptyOptions;
    mergeOptions.topBar.animateLeftButtons = [Bool withValue:YES];
    RNNButtonOptions *button = [RNNButtonOptions new];
    mergeOptions.topBar.leftButtons = @[ button ];

    [[self.buttonsPresenter expect]
            applyLeftButtons:[OCMArg checkWithBlock:^BOOL(NSArray *buttons) {
              return buttons.firstObject == button;
            }]
                defaultColor:OCMArg.any
        defaultDisabledColor:OCMArg.any
                    animated:YES];
    [self.uut mergeOptions:mergeOptions resolvedOptions:RNNNavigationOptions.emptyOptions];
    [self.buttonsPresenter verify];
}

- (void)testMergeOptions_animateRightButtons {
    RNNNavigationOptions *mergeOptions = RNNNavigationOptions.emptyOptions;
    mergeOptions.topBar.animateRightButtons = [Bool withValue:YES];
    RNNButtonOptions *button = [RNNButtonOptions new];
    mergeOptions.topBar.rightButtons = @[ button ];

    [[self.buttonsPresenter expect]
           applyRightButtons:[OCMArg checkWithBlock:^BOOL(NSArray *buttons) {
             return buttons.firstObject == button;
           }]
                defaultColor:OCMArg.any
        defaultDisabledColor:OCMArg.any
                    animated:YES];
    [self.uut mergeOptions:mergeOptions resolvedOptions:RNNNavigationOptions.emptyOptions];
    [self.buttonsPresenter verify];
}

- (void)testApplyOptions_drawBehindTabBarTrueWhenVisibleFalse {
    self.options.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
    [[(id)self.boundViewController expect] setDrawBehindBottomTabs:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testApplyOptions_setOverlayTouchOutsideIfHasValue {
    self.options.overlay.interceptTouchOutside = [[Bool alloc] initWithBOOL:YES];
    [(RNNComponentViewController *)[(id)self.boundViewController expect]
        setInterceptTouchOutside:YES];
    [self.uut applyOptions:self.options];
    [(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_TopBarDrawUnder_true {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(1)];

    [[(id)self.boundViewController expect] setDrawBehindTopBar:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_TopBarDrawUnder_false {
    self.options.topBar.drawBehind = [[Bool alloc] initWithValue:@(0)];

    [[(id)self.boundViewController expect] setDrawBehindTopBar:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_BottomTabsDrawUnder_true {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(1)];

    [[(id)self.boundViewController expect] setDrawBehindBottomTabs:YES];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_BottomTabsDrawUnder_false {
    self.options.bottomTabs.drawBehind = [[Bool alloc] initWithValue:@(0)];

    [[(id)self.boundViewController expect] setDrawBehindBottomTabs:NO];
    [self.uut applyOptionsOnInit:self.options];
    [(id)self.boundViewController verify];
}

- (void)testReactViewShouldBeReleasedOnDealloc {
    RNNComponentViewController *bindViewController = [RNNComponentViewController new];
    bindViewController.layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
    [self.uut bindViewController:bindViewController];

    self.options.topBar.title.component =
        [[RNNComponentOptions alloc] initWithDict:@{@"name" : @"componentName"}];

    [[(id)self.componentRegistry expect] clearComponentsForParentId:self.uut.boundComponentId];
    self.uut = nil;
    [(id)self.componentRegistry verify];
}

- (void)testBindViewControllerShouldSetBoundComponentId {
    RNNComponentViewController *bindViewController = [RNNComponentViewController new];
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = @"componentId";
    bindViewController.layoutInfo = layoutInfo;

    [self.uut bindViewController:bindViewController];
    XCTAssertEqual(self.uut.boundComponentId, @"componentId");
}

- (void)testRenderComponentsCreateReactViewWithBoundComponentId {
    RNNComponentViewController *boundViewController = [RNNComponentViewController new];
    RNNLayoutInfo *layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
    boundViewController.layoutInfo = layoutInfo;
    boundViewController.defaultOptions = [RNNNavigationOptions emptyOptions];
    [self.uut bindViewController:boundViewController];

    self.options.topBar.title.component = [[RNNComponentOptions alloc]
        initWithDict:@{@"name" : @"titleComponent", @"componentId" : @"id"}];

    [[(id)self.componentRegistry expect]
        createComponentIfNotExists:[OCMArg checkWithBlock:^BOOL(RNNComponentOptions *options) {
          return [options.name.get isEqual:@"titleComponent"] &&
                 [options.componentId.get isEqual:@"id"];
        }]
                 parentComponentId:self.uut.boundComponentId
                     componentType:RNNComponentTypeTopBarTitle
               reactViewReadyBlock:[OCMArg any]];
    [self.uut renderComponents:self.options perform:nil];
    [(id)self.componentRegistry verify];

    XCTAssertEqual(self.uut.boundComponentId, @"componentId");
}

- (void)testRenderComponentsCreateReactViewFromDefaultOptions {
    RNNComponentViewController *boundViewController = [RNNComponentViewController new];
    boundViewController.layoutInfo = [self createLayoutInfoWithComponentId:@"componentId"];
    self.uut.defaultOptions = [RNNNavigationOptions emptyOptions];
    [self.uut bindViewController:boundViewController];

    self.uut.defaultOptions.topBar.title.component = [[RNNComponentOptions alloc]
        initWithDict:@{@"name" : @"titleComponent", @"componentId" : @"id"}];

    [[(id)self.componentRegistry expect]
        createComponentIfNotExists:[OCMArg checkWithBlock:^BOOL(RNNComponentOptions *options) {
          return [options.name.get isEqual:@"titleComponent"] &&
                 [options.componentId.get isEqual:@"id"];
        }]
                 parentComponentId:self.uut.boundComponentId
                     componentType:RNNComponentTypeTopBarTitle
               reactViewReadyBlock:[OCMArg any]];
    [self.uut renderComponents:self.options perform:nil];
    [(id)self.componentRegistry verify];

    XCTAssertEqual(self.uut.boundComponentId, @"componentId");
}

- (void)testRemoveTitleComponentIfNeeded_componentIsRemovedIfTitleTextIsDefined {
    id mockTitle = [OCMockObject niceMockForClass:[RNNReactTitleView class]];
    OCMStub([self.componentRegistry createComponentIfNotExists:[OCMArg any]
                                             parentComponentId:[OCMArg any]
                                                 componentType:RNNComponentTypeTopBarTitle
                                           reactViewReadyBlock:nil])
        .andReturn(mockTitle);

    RNNComponentOptions *component = [RNNComponentOptions new];
    component.name = [[Text alloc] initWithValue:@"componentName"];
    component.componentId = [[Text alloc] initWithValue:@"someId"];
    _options.topBar.title.component = component;

    [self.uut mergeOptions:_options resolvedOptions:[RNNNavigationOptions emptyOptions]];
    XCTAssertNotNil(self.boundViewController.navigationItem.titleView);
    XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);

    [[mockTitle expect] removeFromSuperview];
    _options = [RNNNavigationOptions emptyOptions];
    _options.topBar.title.text = [[Text alloc] initWithValue:@""];
    [self.uut mergeOptions:_options resolvedOptions:[RNNNavigationOptions emptyOptions]];
    XCTAssertNotEqual(self.boundViewController.navigationItem.titleView, mockTitle);
    [mockTitle verify];
}

- (void)
    testRemoveTitleComponentIfNeeded_componentIsNotRemovedIfMergeOptionsIsCalledWithoutTitleText {
    id mockTitle = [OCMockObject niceMockForClass:[RNNReactTitleView class]];
    OCMStub([self.componentRegistry createComponentIfNotExists:[OCMArg any]
                                             parentComponentId:[OCMArg any]
                                                 componentType:RNNComponentTypeTopBarTitle
                                           reactViewReadyBlock:nil])
        .andReturn(mockTitle);

    RNNComponentOptions *component = [RNNComponentOptions new];
    component.name = [[Text alloc] initWithValue:@"componentName"];
    component.componentId = [[Text alloc] initWithValue:@"someId"];
    _options.topBar.title.component = component;

    [self.uut mergeOptions:_options resolvedOptions:[RNNNavigationOptions emptyOptions]];
    XCTAssertNotNil(self.boundViewController.navigationItem.titleView);
    XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);

    _options = [RNNNavigationOptions emptyOptions];
    _options.bottomTabs.visible = [[Bool alloc] initWithBOOL:NO];
    [self.uut mergeOptions:_options resolvedOptions:[RNNNavigationOptions emptyOptions]];
    XCTAssertEqual(self.boundViewController.navigationItem.titleView, mockTitle);
}

- (RNNLayoutInfo *)createLayoutInfoWithComponentId:(NSString *)componentId {
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = componentId;
    return layoutInfo;
}

@end
