#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "RNNTabBarController.h"

@interface RNNTabBarPresenterTest : XCTestCase

@property(nonatomic, strong) RNNTabBarPresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) id boundViewController;

@end

@implementation RNNTabBarPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[RNNTabBarPresenter new]];
    self.boundViewController = [OCMockObject partialMockForObject:[RNNTabBarController new]];
    [self.uut bindViewController:self.boundViewController];
    self.options = [[RNNNavigationOptions alloc] initEmptyOptions];
}

- (void)testApplyOptions_shouldSetDefaultEmptyOptions {
    RNNNavigationOptions *emptyOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    [[self.boundViewController expect] rnn_setTabBarTestID:nil];
    [[self.boundViewController expect] rnn_setTabBarBackgroundColor:nil];
    [[self.boundViewController expect] rnn_setTabBarTranslucent:NO];
    [[self.boundViewController expect] rnn_setTabBarHideShadow:NO];
    [[self.boundViewController expect] rnn_setTabBarStyle:UIBarStyleDefault];
    [[self.boundViewController expect] rnn_setTabBarVisible:YES animated:NO];
    [self.uut applyOptions:emptyOptions];
    [self.boundViewController verify];
}

- (void)testApplyOptions_shouldApplyOptions {
    RNNNavigationOptions *initialOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
    initialOptions.bottomTabs.testID = [[Text alloc] initWithValue:@"testID"];
    initialOptions.bottomTabs.backgroundColor = [[Color alloc] initWithValue:[UIColor redColor]];
    initialOptions.bottomTabs.translucent = [[Bool alloc] initWithValue:@(0)];
    initialOptions.bottomTabs.hideShadow = [[Bool alloc] initWithValue:@(1)];
    initialOptions.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
    initialOptions.bottomTabs.barStyle = [[Text alloc] initWithValue:@"black"];

    [[self.boundViewController expect] rnn_setTabBarTestID:@"testID"];
    [[self.boundViewController expect] rnn_setTabBarBackgroundColor:[UIColor redColor]];
    [[self.boundViewController expect] rnn_setTabBarTranslucent:NO];
    [[self.boundViewController expect] rnn_setTabBarHideShadow:YES];
    [[self.boundViewController expect] rnn_setTabBarStyle:UIBarStyleBlack];
    [[self.boundViewController expect] rnn_setTabBarVisible:NO animated:NO];

    [self.uut applyOptions:initialOptions];
    [self.boundViewController verify];
}

- (void)testViewDidLayoutSubviews_appliesBadgeOnNextRunLoop {
    id uut = [self uut];
    [[uut expect] applyDotIndicator];
    [uut viewDidLayoutSubviews];
    [[NSRunLoop mainRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:0.01]];
    [uut verify];
}

- (void)testApplyDotIndicator_callsAppliesBadgeWithEachChild {
    id uut = [self uut];
    id child1 = [UIViewController new];
    id child2 = [UIViewController new];

    [[uut expect] applyDotIndicator:child1];
    [[uut expect] applyDotIndicator:child2];
    [[self boundViewController] addChildViewController:child1];
    [[self boundViewController] addChildViewController:child2];

    [uut applyDotIndicator];
    [uut verify];
}

@end
