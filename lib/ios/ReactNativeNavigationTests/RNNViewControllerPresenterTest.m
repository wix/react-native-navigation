#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNViewControllerPresenter.h"
#import "UIViewController+RNNOptions.h"

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

- (void)testApplyOptions_backgroundImageDefaultNil {
	[self.uut applyOptions:self.options];
	XCTAssertNil(((UIImageView *)self.bindedViewController.view.subviews[0]).image);
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
	[self.uut applyOptions:self.options];
	[(id)self.bindedViewController verify];
}

@end
