#import "RNNSideMenuController.h"
#import "RNNComponentViewController.h"
#import "RNNTestRootViewCreator.h"
#import <XCTest/XCTest.h>

@interface RNNSideMenuControllerTest : XCTestCase
@property(nonatomic, strong) RNNSideMenuController *uut;
@property(nonatomic, strong) RNNTestRootViewCreator *creator;
@property(nonatomic, strong) RNNSideMenuChildVC *centerVC;
@property(nonatomic, strong) RNNSideMenuChildVC *leftVC;
@property(nonatomic, strong) RNNSideMenuChildVC *rightVC;
@end

@implementation RNNSideMenuControllerTest

- (void)setUp {
    [super setUp];
    _creator = [[RNNTestRootViewCreator alloc] init];
    _leftVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil
                                                     creator:nil
                                                     options:[RNNNavigationOptions emptyOptions]
                                              defaultOptions:nil
                                                   presenter:[RNNBasePresenter new]
                                                eventEmitter:nil
                                         childViewController:self.generateComponent
                                                        type:RNNSideMenuChildTypeLeft];
    [_leftVC.presenter bindViewController:_leftVC];
    _rightVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil
                                                      creator:nil
                                                      options:[RNNNavigationOptions emptyOptions]
                                               defaultOptions:nil
                                                    presenter:[RNNBasePresenter new]
                                                 eventEmitter:nil
                                          childViewController:self.generateComponent
                                                         type:RNNSideMenuChildTypeRight];
    [_rightVC.presenter bindViewController:_rightVC];
    _centerVC = [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil
                                                       creator:nil
                                                       options:[RNNNavigationOptions emptyOptions]
                                                defaultOptions:nil
                                                     presenter:[RNNBasePresenter new]
                                                  eventEmitter:nil
                                           childViewController:self.generateComponent
                                                          type:RNNSideMenuChildTypeCenter];
    [_centerVC.presenter bindViewController:_centerVC];
    self.uut = [[RNNSideMenuController alloc]
          initWithLayoutInfo:nil
                     creator:nil
        childViewControllers:@[ _leftVC, _centerVC, _rightVC ]
                     options:[RNNNavigationOptions emptyOptions]
              defaultOptions:nil
                   presenter:[[RNNSideMenuPresenter alloc] initWithDefaultOptions:nil]
                eventEmitter:nil];
}

- (RNNComponentViewController *)generateComponent {
    return [[RNNComponentViewController alloc]
        initWithLayoutInfo:nil
           rootViewCreator:_creator
              eventEmitter:nil
                 presenter:[RNNComponentPresenter new]
                   options:[[RNNNavigationOptions alloc] initWithDict:@{}]
            defaultOptions:nil];
}

- (void)testSetSideMenuWidthShouldUpdateLeftReactViewFrameWidth {
    [self.uut side:MMDrawerSideLeft width:100];
    XCTAssertEqual(self.uut.left.child.view.frame.size.width, 100.f);
}

- (void)testSetSideMenuWidthShouldUpdateRightReactViewFrameWidth {
    [self.uut side:MMDrawerSideRight width:150];
    XCTAssertEqual(self.uut.right.child.view.frame.size.width, 150.f);
}

- (void)testGetCurrentChild {
    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
    XCTAssertEqual(_uut.getCurrentChild, _centerVC);

    [_uut openDrawerSide:MMDrawerSideLeft
                animated:NO
              completion:(void (^)(BOOL)) ^ { XCTAssertEqual(_uut.getCurrentChild, _leftVC); }];

    [_uut closeDrawerAnimated:NO completion:nil];
    [_uut openDrawerSide:MMDrawerSideRight
                animated:NO
              completion:(void (^)(BOOL)) ^ {
                  XCTAssertEqual(_uut.getCurrentChild, _rightVC);
                  [expectation fulfill];
              }];

    [self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testResolveOptions {
    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];

    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.sideMenu.left.visible = [[Bool alloc] initWithBOOL:YES];
    [_centerVC mergeOptions:options];

    XCTAssertTrue(_uut.resolveOptions.sideMenu.left.visible);

    [_uut openDrawerSide:MMDrawerSideLeft
                animated:NO
              completion:^(BOOL finished) {
                XCTAssertTrue(_uut.resolveOptions.sideMenu.left.visible);
                [expectation fulfill];
              }];

    [self waitForExpectationsWithTimeout:1 handler:nil];
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

@end
