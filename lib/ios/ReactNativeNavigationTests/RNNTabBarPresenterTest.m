#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "RNNTabBarController.h"

@interface RNNTabBarPresenterTest : XCTestCase

@property (nonatomic, strong) RNNTabBarPresenter *uut;
@property (nonatomic, strong) RNNNavigationOptions *options;
@property (nonatomic, strong) id bindedViewController;

@end

@implementation RNNTabBarPresenterTest

- (void)setUp {
    [super setUp];
	self.uut = [[RNNTabBarPresenter alloc] init];
	self.bindedViewController = [OCMockObject partialMockForObject:[RNNTabBarController new]];
	[self.uut bindViewController:self.bindedViewController];
	self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_shouldSetDefaultEmptyOptions {
	RNNNavigationOptions* emptyOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	[[self.bindedViewController expect] rnn_setTabBarTestID:nil];
	[[self.bindedViewController expect] rnn_setTabBarBackgroundColor:nil];
	[[self.bindedViewController expect] rnn_setTabBarTranslucent:NO];
	[[self.bindedViewController expect] rnn_setTabBarHideShadow:NO];
    [[self.bindedViewController expect] rnn_setTabBarStyle:UIBarStyleDefault];
	[[self.bindedViewController expect] rnn_setTabBarVisible:YES animated:NO];
	[self.uut applyOptions:emptyOptions];
	[self.bindedViewController verify];
}

- (void)testApplyOptions_shouldApplyOptions {
	RNNNavigationOptions* initialOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	initialOptions.bottomTabs.testID = [[Text alloc] initWithValue:@"testID"];
	initialOptions.bottomTabs.backgroundColor = [[Color alloc] initWithValue:[UIColor redColor]];
	initialOptions.bottomTabs.translucent = [[Bool alloc] initWithValue:@(0)];
	initialOptions.bottomTabs.hideShadow = [[Bool alloc] initWithValue:@(1)];
	initialOptions.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
	initialOptions.bottomTabs.barStyle = [[Text alloc] initWithValue:@"black"];
	
	[[self.bindedViewController expect] rnn_setTabBarTestID:@"testID"];
	[[self.bindedViewController expect] rnn_setTabBarBackgroundColor:[UIColor redColor]];
	[[self.bindedViewController expect] rnn_setTabBarTranslucent:NO];
	[[self.bindedViewController expect] rnn_setTabBarHideShadow:YES];
	[[self.bindedViewController expect] rnn_setTabBarStyle:UIBarStyleBlack];
	[[self.bindedViewController expect] rnn_setTabBarVisible:NO animated:NO];
	
	[self.uut applyOptions:initialOptions];
	[self.bindedViewController verify];
}


@end
