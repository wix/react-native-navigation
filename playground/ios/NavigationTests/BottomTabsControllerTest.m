#import "RNNBottomTabsController+Helpers.h"
#import "RNNComponentViewController+Utils.h"
#import "RNNStackController.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/BottomTabPresenterCreator.h>
#import <ReactNativeNavigation/RNNBottomTabsController.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <ReactNativeNavigation/UITabBar+utils.h>
#import <XCTest/XCTest.h>

@interface BottomTabsControllerTest : XCTestCase

@property(nonatomic, strong) RNNBottomTabsController *originalUut;
@property(nonatomic, strong) RNNBottomTabsController *uut;
@property(nonatomic, strong) id mockChildViewController;
@property(nonatomic, strong) id mockEventEmitter;
@property(nonatomic, strong) id mockTabBarPresenter;

@end

@implementation BottomTabsControllerTest

- (void)setUp {
    [super setUp];

    id tabBarClassMock = OCMClassMock([RNNBottomTabsController class]);
    OCMStub([tabBarClassMock parentViewController])
        .andReturn([OCMockObject partialMockForObject:[RNNBottomTabsController new]]);
    UIViewController *childViewController =
        [RNNComponentViewController createWithComponentId:@"componentId"
                                           initialOptions:[RNNNavigationOptions emptyOptions]];
    NSArray *children = @[ childViewController ];
    self.mockTabBarPresenter =
        [OCMockObject partialMockForObject:[[RNNBottomTabsPresenter alloc] init]];
    self.mockChildViewController = [OCMockObject partialMockForObject:childViewController];
    self.mockEventEmitter = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
    self.originalUut = [[RNNBottomTabsController alloc]
           initWithLayoutInfo:nil
                      creator:nil
                      options:[[RNNNavigationOptions alloc] initWithDict:@{}]
               defaultOptions:nil
                    presenter:self.mockTabBarPresenter
           bottomTabPresenter:[BottomTabPresenterCreator createWithDefaultOptions:nil]
        dotIndicatorPresenter:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:nil]
                 eventEmitter:self.mockEventEmitter
         childViewControllers:children
           bottomTabsAttacher:nil];
    self.uut = [OCMockObject partialMockForObject:self.originalUut];
    OCMStub([self.uut selectedViewController]).andReturn(self.mockChildViewController);
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
    XCTAssertNotNil([self.uut presenter]);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
    UIViewController *vc1 = [[UIViewController alloc] init];
    UIViewController *vc2 = [[UIViewController alloc] init];

    RNNBottomTabsController *uut = [RNNBottomTabsController createWithChildren:@[ vc1, vc2 ]];
    [uut viewWillAppear:YES];
    XCTAssertTrue(uut.viewControllers.count == 2);
}

- (void)testInitWithLayoutInfo_shouldInitializeDependencies {
    RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];
    RNNBottomTabsPresenter *presenter = [[RNNBottomTabsPresenter alloc] init];
    NSArray *childViewControllers = @[ [UIViewController new] ];
    RNNEventEmitter *eventEmmiter = [RNNEventEmitter new];

    RNNBottomTabsController *uut =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:layoutInfo
                                                    creator:nil
                                                    options:options
                                             defaultOptions:nil
                                                  presenter:presenter
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:eventEmmiter
                                       childViewControllers:childViewControllers
                                         bottomTabsAttacher:nil];
    [uut viewWillAppear:YES];
    XCTAssertTrue(uut.layoutInfo == layoutInfo);
    XCTAssertTrue(uut.options == options);
    XCTAssertTrue(uut.presenter == presenter);
    XCTAssertTrue(uut.childViewControllers.count == childViewControllers.count);
    XCTAssertTrue(uut.eventEmitter == eventEmmiter);
}

- (void)testInitWithLayoutInfo_shouldSetDelegate {
    RNNBottomTabsController *uut = [RNNBottomTabsController createWithChildren:@[]];

    XCTAssertTrue(uut.delegate == uut);
}

- (void)testInitWithLayoutInfo_shouldCreateWithDefaultStyles {
    RNNBottomTabsController *uut = [[RNNBottomTabsController alloc]
          initWithLayoutInfo:nil
                     creator:nil
                     options:[[RNNNavigationOptions alloc] initWithDict:@{}]
              defaultOptions:nil
                   presenter:[[RNNBottomTabsPresenter alloc] init]
                eventEmitter:nil
        childViewControllers:nil];

    XCTAssertEqual(uut.modalPresentationStyle, UIModalPresentationPageSheet);
    XCTAssertEqual(uut.modalTransitionStyle, UIModalTransitionStyleCoverVertical);
}

- (void)testOnChildAppear_shouldInvokePresenterApplyOptionsWithResolvedOptions {
    [[self.mockTabBarPresenter expect] applyOptions:[OCMArg any]];
    [self.uut onChildWillAppear];
    [self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokePresenterMergeOptions {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];

    [(RNNBottomTabsPresenter *)[self.mockTabBarPresenter expect] mergeOptions:options
                                                              resolvedOptions:[OCMArg any]];
    [self.uut mergeOptions:options];
    [self.mockTabBarPresenter verify];
}

- (void)testMergeOptions_shouldInvokeParentMergeOptions {
    id parentMock = [OCMockObject niceMockForClass:[RNNComponentViewController class]];
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];

    OCMStub([self.uut parentViewController]).andReturn(parentMock);
    [((RNNComponentViewController *)[parentMock expect]) mergeChildOptions:options
                                                                     child:self.originalUut];
    [self.uut mergeOptions:options];
    [parentMock verify];
}

- (void)testOnChildAppear_shouldInvokeParentOnChildAppear {
    id parentMock = [OCMockObject partialMockForObject:[RNNStackController new]];

    OCMStub([self.uut parentViewController]).andReturn(parentMock);

    [[parentMock expect] onChildWillAppear];
    [self.uut onChildWillAppear];
    [parentMock verify];
}

- (void)testViewDidLayoutSubviews_delegateToPresenter {
    [[[self mockTabBarPresenter] expect] viewDidLayoutSubviews];
    [[self uut] viewDidLayoutSubviews];
    [[self mockTabBarPresenter] verify];
}

- (void)testGetCurrentChild_shouldReturnSelectedViewController {
    XCTAssertEqual([self.uut getCurrentChild],
                   [(RNNBottomTabsController *)self.uut selectedViewController]);
}

- (void)testPreferredStatusBarStyle_shouldInvokeSelectedViewControllerPreferredStatusBarStyle {
    [[self.mockTabBarPresenter expect] getStatusBarStyle];
    [self.uut preferredStatusBarStyle];
    [self.mockTabBarPresenter verify];
}

- (void)testPreferredStatusHidden_shouldResolveChildStatusBarVisibleTrue {
    self.uut.getCurrentChild.options.statusBar.visible = [Bool withValue:YES];
    XCTAssertFalse(self.uut.prefersStatusBarHidden);
}

- (void)testPreferredStatusHidden_shouldResolveChildStatusBarVisibleFalse {
    self.uut.getCurrentChild.options.statusBar.visible = [Bool withValue:NO];
    XCTAssertTrue(self.uut.prefersStatusBarHidden);
}

- (void)testPreferredStatusHidden_shouldHideStatusBar {
    self.uut.options.statusBar.visible = [Bool withValue:YES];
    XCTAssertFalse(self.uut.prefersStatusBarHidden);
}

- (void)testPreferredStatusHidden_shouldShowStatusBar {
    self.uut.options.statusBar.visible = [Bool withValue:NO];
    XCTAssertTrue(self.uut.prefersStatusBarHidden);
}

- (void)testTabBarControllerDidSelectViewControllerDelegate_shouldInvokeSendBottomTabSelectedEvent {
    NSUInteger selectedIndex = 2;
    OCMStub([self.uut selectedIndex]).andReturn(selectedIndex);

    [[self.mockEventEmitter expect] sendBottomTabSelected:@(selectedIndex) unselected:@(0)];
    [self.uut tabBarController:self.uut didSelectViewController:[UIViewController new]];
    [self.mockEventEmitter verify];
}

- (void)testSetSelectedIndexByComponentID_ShouldSetSelectedIndexWithCorrectIndex {
    RNNLayoutInfo *layoutInfo = [RNNLayoutInfo new];
    layoutInfo.componentId = @"componentId";

    RNNComponentViewController *vc =
        [[RNNComponentViewController alloc] initWithLayoutInfo:layoutInfo
                                               rootViewCreator:nil
                                                  eventEmitter:nil
                                                     presenter:nil
                                                       options:nil
                                                defaultOptions:nil];

    RNNBottomTabsController *uut =
        [RNNBottomTabsController createWithChildren:@[ [UIViewController new], vc ]];
    [uut viewWillAppear:YES];

    [uut setSelectedIndexByComponentID:@"componentId"];
    XCTAssertTrue(uut.selectedIndex == 1);
}

- (void)testSetSelectedIndex_ShouldSetSelectedIndexWithCurrentTabIndex {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.bottomTabs.currentTabIndex = [[IntNumber alloc] initWithValue:@(1)];

    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:nil
                                                                            rootViewCreator:nil
                                                                               eventEmitter:nil
                                                                                  presenter:nil
                                                                                    options:nil
                                                                             defaultOptions:nil];
    RNNBottomTabsController *uut =
        [RNNBottomTabsController createWithChildren:@[ [UIViewController new], vc ]
                                            options:options];
    [uut viewWillAppear:YES];

    XCTAssertTrue(uut.selectedIndex == 1);
}

- (void)testDidSelectViewController_emitEventOnTabPress {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:nil
                                                                            rootViewCreator:nil
                                                                               eventEmitter:nil
                                                                                  presenter:nil
                                                                                    options:nil
                                                                             defaultOptions:nil];
    RNNBottomTabsController *uut =
        [RNNBottomTabsController createWithChildren:@[ [UIViewController new], vc ]
                                            options:options];
    [uut viewWillAppear:YES];

    [[(id)uut.eventEmitter expect] sendBottomTabSelected:@(1) unselected:@(0)];
    [uut setSelectedViewController:vc];
    [uut tabBarController:uut didSelectViewController:vc];
    [(id)uut.eventEmitter verify];
}

- (void)testTabLongPress_ShouldEmitEvent {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:nil
                                                                            rootViewCreator:nil
                                                                               eventEmitter:nil
                                                                                  presenter:nil
                                                                                    options:nil
                                                                             defaultOptions:nil];
    RNNBottomTabsController *uut =
        [RNNBottomTabsController createWithChildren:@[ [UIViewController new], vc ]
                                            options:options];
    [uut viewWillAppear:YES];
    [uut.tabBar layoutSubviews];
    [[(id)uut.eventEmitter expect] sendBottomTabLongPressed:@(1)];
    UIView *secondTabItemView = [uut.tabBar tabBarItemViewAtIndex:1];
    [uut handleTabBarLongPress:secondTabItemView.center];
    [(id)uut.eventEmitter verify];
}

- (void)testOnViewDidLayoutSubviews_ShouldUpdateDotIndicatorForChildren {
    id dotIndicator = [OCMockObject
        partialMockForObject:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:nil]];
    RNNComponentViewController *vc = [[RNNComponentViewController alloc] initWithLayoutInfo:nil
                                                                            rootViewCreator:nil
                                                                               eventEmitter:nil
                                                                                  presenter:nil
                                                                                    options:nil
                                                                             defaultOptions:nil];
    RNNBottomTabsController *uut =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:nil
                                             defaultOptions:nil
                                                  presenter:nil
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:dotIndicator
                                               eventEmitter:nil
                                       childViewControllers:@[ [UIViewController new], vc ]
                                         bottomTabsAttacher:nil];

    [[dotIndicator expect] bottomTabsDidLayoutSubviews:uut];
    [uut viewDidLayoutSubviews];
    [dotIndicator verify];
}

- (void)testShouldSelectViewController_returnTrueForMoreTab {
    XCTAssertTrue([self.uut tabBarController:self.uut
                  shouldSelectViewController:UIViewController.new]);
}

- (void)testShouldSelectViewController_returnTrueByDefault {
    [self.uut viewWillAppear:NO];
    XCTAssertTrue([self.uut tabBarController:self.uut
                  shouldSelectViewController:self.uut.childViewControllers[0]]);
}

- (void)testShouldSelectViewController_selectTabOnPressFalse {
    [self.uut viewWillAppear:NO];
    self.uut.childViewControllers[0].options.bottomTab.selectTabOnPress = [Bool withValue:NO];
    XCTAssertFalse([self.uut tabBarController:self.uut
                   shouldSelectViewController:self.uut.childViewControllers[0]]);
}

- (void)testShouldSelectViewController_emitEvent {
    [self.uut viewWillAppear:NO];
    [[self.mockEventEmitter expect] sendBottomTabPressed:@(0)];
    [self.uut tabBarController:self.uut
        shouldSelectViewController:self.uut.childViewControllers[0]];
    [self.mockEventEmitter verify];
}

- (void)testInit_shouldCreateTabBarItems {
    id dotIndicator = [OCMockObject
        partialMockForObject:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:nil]];
    RNNNavigationOptions *vc1Options = [RNNNavigationOptions emptyOptions];
    vc1Options.bottomTab.text = [Text withValue:@"VC 1"];
    RNNComponentViewController *vc1 = [RNNComponentViewController createWithComponentId:@"VC 1"
                                                                         initialOptions:vc1Options];

    RNNNavigationOptions *vc2Options = [RNNNavigationOptions emptyOptions];
    vc2Options.bottomTab.text = [Text withValue:@"VC 2"];
    RNNComponentViewController *vc2 = [RNNComponentViewController createWithComponentId:@"VC 2"
                                                                         initialOptions:vc2Options];
    RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:RNNNavigationOptions.new
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ vc2 ]];

    __unused RNNBottomTabsController *uut = [[RNNBottomTabsController alloc]
           initWithLayoutInfo:nil
                      creator:nil
                      options:nil
               defaultOptions:nil
                    presenter:nil
           bottomTabPresenter:[BottomTabPresenterCreator
                                  createWithDefaultOptions:RNNNavigationOptions.emptyOptions]
        dotIndicatorPresenter:dotIndicator
                 eventEmitter:nil
         childViewControllers:@[ vc1, stack ]
           bottomTabsAttacher:nil];

    XCTAssert([vc1.tabBarItem.title isEqualToString:@"VC 1"]);
    XCTAssert([stack.tabBarItem.title isEqualToString:@"VC 2"]);
}

@end
