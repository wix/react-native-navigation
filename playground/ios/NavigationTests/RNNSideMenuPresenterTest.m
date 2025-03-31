#import "RNNSideMenuPresenter.h"
#import "RNNSideMenuController.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNSideMenuPresenterTest : XCTestCase

@property(nonatomic, strong) RNNSideMenuPresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) id boundViewController;

@end

@implementation RNNSideMenuPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNSideMenuPresenter alloc] init];
    self.boundViewController =
        [OCMockObject partialMockForObject:[[RNNSideMenuController alloc]
                                                 initWithLayoutInfo:nil
                                                            creator:nil
                                               childViewControllers:@[
                                                   [self createChildVC:RNNSideMenuChildTypeCenter]
                                               ]
                                                            options:nil
                                                     defaultOptions:nil
                                                          presenter:nil
                                                       eventEmitter:nil]];
    [self.uut bindViewController:self.boundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
}

- (RNNSideMenuChildVC *)createChildVC:(RNNSideMenuChildType)type {
    return [[RNNSideMenuChildVC alloc] initWithLayoutInfo:nil
                                                  creator:nil
                                                  options:nil
                                           defaultOptions:nil
                                                presenter:nil
                                             eventEmitter:nil
                                      childViewController:[UIViewController new]
                                                     type:type];
}

- (void)testApplyOptionsShouldSetDefaultValues {
    [[self.boundViewController expect] side:MMDrawerSideLeft enabled:YES];
    [[self.boundViewController expect] side:MMDrawerSideRight enabled:YES];
    [[self.boundViewController expect] setShouldStretchLeftDrawer:YES];
    [[self.boundViewController expect] setShouldStretchRightDrawer:YES];
    [[self.boundViewController expect] setAnimationVelocityLeft:840.0f];
    [[self.boundViewController expect] setAnimationVelocityRight:840.0f];
    [[self.boundViewController reject] side:MMDrawerSideLeft width:0];
    [[self.boundViewController reject] side:MMDrawerSideRight width:0];
    [[self.boundViewController expect] setAnimationType:nil];

    [self.uut applyOptions:self.options];

    [self.boundViewController verify];
}

- (void)testApplyOptionsShouldSetInitialValues {
    self.options.sideMenu.left.enabled = [[Bool alloc] initWithBOOL:NO];
    self.options.sideMenu.right.enabled = [[Bool alloc] initWithBOOL:NO];
    self.options.sideMenu.left.shouldStretchDrawer = [[Bool alloc] initWithBOOL:NO];
    self.options.sideMenu.right.shouldStretchDrawer = [[Bool alloc] initWithBOOL:NO];
    self.options.sideMenu.right.animationVelocity = [[Double alloc] initWithValue:@(100.0f)];
    self.options.sideMenu.left.animationVelocity = [[Double alloc] initWithValue:@(100.0f)];

    [[self.boundViewController expect] side:MMDrawerSideLeft enabled:NO];
    [[self.boundViewController expect] side:MMDrawerSideRight enabled:NO];
    [[self.boundViewController expect] setShouldStretchLeftDrawer:NO];
    [[self.boundViewController expect] setShouldStretchRightDrawer:NO];
    [[self.boundViewController expect] setAnimationVelocityLeft:100.0f];
    [[self.boundViewController expect] setAnimationVelocityRight:100.0f];

    [self.uut applyOptions:self.options];

    [self.boundViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetWidthOptions {
    self.options.sideMenu.right.width = [[Double alloc] initWithValue:@(100.0f)];
    self.options.sideMenu.left.width = [[Double alloc] initWithValue:@(100.0f)];

    [[self.boundViewController expect] side:MMDrawerSideLeft width:100.0f];
    [[self.boundViewController expect] side:MMDrawerSideRight width:100.0f];

    [self.uut applyOptionsOnInit:self.options];

    [self.boundViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetDefaultDrawerGestureMode {
    [[self.boundViewController expect]
        setOpenDrawerGestureModeMask:MMOpenDrawerGestureModeBezelPanningCenterView];
    [self.uut applyOptionsOnInit:self.options];
    [self.boundViewController verify];
}

- (void)testApplyOptionsOnInitShouldSetBezelDrawerGestureMode {
    self.options.sideMenu.openGestureMode =
        [[SideMenuOpenMode alloc] initWithValue:@(MMOpenDrawerGestureModeNone)];
    [[self.boundViewController expect] setOpenDrawerGestureModeMask:MMOpenDrawerGestureModeNone];
    [self.uut applyOptionsOnInit:self.options];
    [self.boundViewController verify];
}

- (void)testBackgroundColor_validColor {
    UIColor *inputColor = [RCTConvert UIColor:@(0xFFFF0000)];
    self.options.layout.backgroundColor = [[Color alloc] initWithValue:inputColor];
    [self.uut applyOptions:self.options];
    UIColor *expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
    XCTAssertTrue([((UIViewController *)self.boundViewController).view.backgroundColor
        isEqual:expectedColor]);
}

@end
