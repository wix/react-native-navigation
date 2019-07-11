#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNDotIndicatorPresenter.h"
#import "DotIndicatorOptions.h"
#import "RNNTabBarController.h"
#import "RNNRootViewController.h"
#import "RNNTestBase.h"

@interface RNNDotIndicatorPresenterTest : RNNTestBase
@property(nonatomic, strong) id uut;
@property(nonatomic, strong) RNNRootViewController *child;
@property(nonatomic, strong) id bottomTabs;
@end

@implementation RNNDotIndicatorPresenterTest
- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[RNNDotIndicatorPresenter new]];
    self.bottomTabs = [OCMockObject partialMockForObject:[RNNTabBarController new]];
    self.child = [self createChild];
    [self.bottomTabs addChildViewController:self.child];

    [self setupTopLevelUI:self.bottomTabs];
}

- (void)testApply_doesNothingIfDoesNotHaveValue {
    DotIndicatorOptions *empty = [DotIndicatorOptions new];
    [[self uut] apply:self.child :empty];
    XCTAssertFalse([self tabHasIndicator]);
}

- (void)testApply_indicatorIsAddedToTabView {
    [[NSRunLoop mainRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:0.01]];
    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:YES];
    [[self uut] apply:self.child :options];
    XCTAssertTrue([self tabHasIndicator]);
}

- (RNNRootViewController *)createChild {
    RNNNavigationOptions *options = [RNNNavigationOptions new];
    options.bottomTab = [RNNBottomTabOptions new];
    options.bottomTab.icon = [[Image alloc] initWithValue:[UIImage new]];
    return [OCMockObject partialMockForObject:[[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[RNNViewControllerPresenter new] options:options defaultOptions:nil]];
}

- (BOOL)tabHasIndicator {
    return [self.child tabBarItem].tag > 0;
}
@end
