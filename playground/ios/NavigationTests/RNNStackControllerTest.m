#import "RNNComponentViewController+Utils.h"
#import "RNNTestRootViewCreator.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <ReactNativeNavigation/RNNStackController.h>
#import <XCTest/XCTest.h>

@interface RNNStackControllerTest : XCTestCase

@property(nonatomic, strong) RNNStackController *uut;

@end

@implementation RNNStackControllerTest {
    RNNComponentViewController *_vc1;
    id _vc2Mock;
    RNNComponentViewController *_vc2;
    UIViewController *_vc3;
    RNNNavigationOptions *_options;
    RNNTestRootViewCreator *_creator;
    RNNEventEmitter *_eventEmitter;
    id _presenter;
}

- (void)setUp {
    [super setUp];
    _presenter = [OCMockObject partialMockForObject:[[RNNStackPresenter alloc] init]];
    _eventEmitter = [OCMockObject niceMockForClass:[RNNEventEmitter class]];
    _creator = [[RNNTestRootViewCreator alloc] init];
    _vc1 = [[RNNComponentViewController alloc]
        initWithLayoutInfo:[RNNLayoutInfo new]
           rootViewCreator:nil
              eventEmitter:nil
                 presenter:[OCMockObject partialMockForObject:[[RNNComponentPresenter alloc] init]]
                   options:[RNNNavigationOptions emptyOptions]
            defaultOptions:[RNNNavigationOptions emptyOptions]];
    _vc2 =
        [[RNNComponentViewController alloc] initWithLayoutInfo:[RNNLayoutInfo new]
                                               rootViewCreator:nil
                                                  eventEmitter:nil
                                                     presenter:[[RNNComponentPresenter alloc] init]
                                                       options:[RNNNavigationOptions emptyOptions]
                                                defaultOptions:[RNNNavigationOptions emptyOptions]];
    _vc2Mock = [OCMockObject partialMockForObject:_vc2];
    _vc3 = [UIViewController new];
    _options = [OCMockObject partialMockForObject:[RNNNavigationOptions emptyOptions]];
    self.uut = [[RNNStackController alloc] initWithLayoutInfo:nil
                                                      creator:_creator
                                                      options:_options
                                               defaultOptions:nil
                                                    presenter:_presenter
                                                 eventEmitter:_eventEmitter
                                         childViewControllers:@[ _vc1, _vc2 ]];
}

- (void)testInitWithLayoutInfo_shouldBindPresenter {
    XCTAssertNotNil(self.uut.presenter);
}

- (void)testInitWithLayoutInfo_shouldPreferLargeTitle {
    XCTAssertTrue(self.uut.navigationBar.prefersLargeTitles);
}

- (void)testInitWithLayoutInfo_shouldSetMultipleViewControllers {
    self.uut = [[RNNStackController alloc] initWithLayoutInfo:nil
                                                      creator:_creator
                                                      options:[RNNNavigationOptions emptyOptions]
                                               defaultOptions:nil
                                                    presenter:[RNNStackPresenter new]
                                                 eventEmitter:nil
                                         childViewControllers:@[ _vc1, _vc2 ]];
    XCTAssertTrue(self.uut.viewControllers.count == 2);
}

- (void)testChildViewControllerForStatusBarStyle_shouldReturnTopViewController {
    XCTAssertTrue(self.uut.childViewControllerForStatusBarStyle == self.uut.topViewController);
}

- (void)testCurrentChild_shouldReturnTopViewController {
    XCTAssertTrue(self.uut.getCurrentChild == self.uut.topViewController);
}

- (void)testGetLeafViewController_shouldReturnTopViewController {
    XCTAssertTrue(self.uut.getCurrentChild == self.uut.topViewController);
}

- (void)testCurrentChild_shouldReturnLastChildWithLayoutInfo {
    [self.uut addChildViewController:[UIViewController new]];
    XCTAssertTrue(self.uut.getCurrentChild != self.uut.topViewController);
    XCTAssertTrue(self.uut.getCurrentChild ==
                  self.uut.childViewControllers[self.uut.childViewControllers.count - 2]);
}

- (void)testPreferredStatusBarStyle_shouldReturnLeafPreferredStatusBarStyle {
    self.uut.getCurrentChild.resolveOptions.statusBar.style = [[Text alloc] initWithValue:@"light"];
    XCTAssertTrue(self.uut.preferredStatusBarStyle ==
                  self.uut.getCurrentChild.preferredStatusBarStyle);
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

- (void)testPopGestureEnabled_false {
    NSNumber *popGestureEnabled = @(0);
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.popGesture = [[Bool alloc] initWithValue:popGestureEnabled];

    self.uut = [self createNavigationControllerWithOptions:options];
    [self.uut viewWillAppear:false];

    XCTAssertFalse(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testPopGestureEnabled_true {
    NSNumber *popGestureEnabled = @(1);
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.popGesture = [[Bool alloc] initWithValue:popGestureEnabled];

    self.uut = [self createNavigationControllerWithOptions:options];
    [self.uut onChildWillAppear];

    XCTAssertTrue(self.uut.interactivePopGestureRecognizer.enabled);
}

- (void)testRootBackgroundImage {
    UIImage *rootBackgroundImage = [[UIImage alloc] init];
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.rootBackgroundImage = [[Image alloc] initWithValue:rootBackgroundImage];

    self.uut = [self createNavigationControllerWithOptions:options];
    [self.uut onChildWillAppear];

    XCTAssertTrue([[(UIImageView *)self.uut.view.subviews[0] image] isEqual:rootBackgroundImage]);
}

- (void)testTopBarBackgroundClipToBounds_true {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.background.clipToBounds = [[Bool alloc] initWithValue:@(1)];

    self.uut = [self createNavigationControllerWithOptions:options];
    [self.uut onChildWillAppear];

    XCTAssertTrue(self.uut.navigationBar.clipsToBounds);
}

- (void)testTopBarBackgroundClipToBounds_false {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.background.clipToBounds = [[Bool alloc] initWithValue:@(0)];

    self.uut = [self createNavigationControllerWithOptions:options];

    XCTAssertFalse(self.uut.navigationController.navigationBar.clipsToBounds);
}

- (void)testSupportedOrientationsShouldReturnCurrentChildSupportedOrientations {
    XCTAssertEqual(self.uut.supportedInterfaceOrientations,
                   self.uut.getCurrentChild.supportedInterfaceOrientations);
}

- (void)testPopViewControllerReturnLastChildViewController {
    RNNStackController *uut = [RNNStackController new];
    [uut setViewControllers:@[ _vc1, _vc2 ]];
    XCTAssertEqual([uut popViewControllerAnimated:NO], _vc2);
}

- (void)testPopViewControllerSetTopBarBackgroundForPoppingViewController {
    _options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];
    [_vc1 mergeOptions:_options];

    [self.uut popViewControllerAnimated:NO];
    [_vc1 viewWillAppear:YES];
    XCTAssertEqual(
        _vc1.resolveOptions.topBar.background.color.get,
        self.uut.childViewControllers.lastObject.navigationItem.standardAppearance.backgroundColor);
}

- (void)testPopViewControllerShouldInvokeApplyOptionsBeforePoppingForDestinationViewController {
    RNNStackController *uut = [RNNStackController new];
    [uut setViewControllers:@[ _vc1, _vc2 ]];

    [[(id)uut.presenter expect] applyOptionsBeforePopping:[OCMArg any]];
    [uut popViewControllerAnimated:NO];
    [(id)uut.presenter verify];
}

- (void)testPopViewController_ShouldEmitScreenPoppedEvent {
    RNNStackController *uut = [RNNStackController new];
    [uut setViewControllers:@[ _vc1, _vc2 ]];

    [[(id)uut.eventEmitter expect] sendScreenPoppedEvent:_vc2.layoutInfo.componentId];
    [uut popViewControllerAnimated:NO];
    [(id)uut.eventEmitter verify];
}

- (void)testMergeOptionsShouldOverrideOptionsState {
    RNNNavigationOptions *overrideOptions = [RNNNavigationOptions emptyOptions];
    [(RNNNavigationOptions *)[(id)self.uut.options expect] mergeOptions:overrideOptions];
    [self.uut mergeOptions:overrideOptions];
    [(id)self.uut.options verify];
}

- (void)testMergeChildOptionsShouldUpdatePresenterForVisibleChild {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];

    [[_presenter expect] mergeOptions:options resolvedOptions:[OCMArg any]];
    [self.uut mergeChildOptions:options child:self.uut.childViewControllers.lastObject];
    [_presenter verify];
}

- (void)testMergeChildOptionsShouldNotUpdatePresenterForInvisibleChild {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];

    [[_presenter reject] mergeOptions:options resolvedOptions:self.uut.resolveOptions];
    [self.uut mergeChildOptions:options child:self.uut.childViewControllers.firstObject];
    [_presenter verify];
}

- (void)testOnChildWillAppear_shouldSetBackButtonTestID {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.backButton.testID = [Text withValue:@"TestID"];
    RNNComponentViewController *pushedController =
        [RNNComponentViewController createWithComponentId:@"pushedController"];
    pushedController.options.topBar.backButton.testID = [Text withValue:@"TestID"];
    [[[UIApplication sharedApplication] keyWindow] setRootViewController:_uut];
    [_uut pushViewController:pushedController animated:NO];
    [pushedController viewDidAppear:YES];
    XCTAssertTrue([[[_uut.navigationBar.subviews[1] subviews][0]
        valueForKey:@"accessibilityIdentifier"] isEqualToString:@"TestID"]);
}

- (RNNStackController *)createNavigationControllerWithOptions:(RNNNavigationOptions *)options {
    RNNStackController *nav =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:_creator
                                               options:options
                                        defaultOptions:nil
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ _vc1 ]];
    return nav;
}

@end
