#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNDotIndicatorPresenter.h"
#import "DotIndicatorOptions.h"
#import "RNNTabBarController.h"
#import "RNNRootViewController.h"
#import "RNNTestBase.h"
#import "UITabBarController+RNNUtils.h"

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
    [self applyIndicator];
    XCTAssertTrue([self tabHasIndicator]);
}

- (void)testApply_indicatorIsRemovedIsNotVisible {
    [self applyIndicator];
    XCTAssertTrue([self tabHasIndicator]);

    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:NO];
    [[self uut] apply:self.child :options];

    XCTAssertFalse([self tabHasIndicator]);
}

- (void)testApply_itDoesNotRecreateIfEqualToCurrentlyVisibleIndicator {
    [self applyIndicator];
    UIView *indicator1 = [self getIndicator];

    [self applyIndicator];
    UIView *indicator2 = [self getIndicator];
    XCTAssertEqualObjects(indicator1, indicator2);
}

- (void)testApply_itAddsIndicatorToCorrectTabView {
    [self applyIndicator];
    UIView *indicator1 = [self getIndicator];
    XCTAssertEqualObjects([indicator1 superview], [_bottomTabs getTabView:0]);
}

- (void)applyIndicator {
    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:YES];
    [[self uut] apply:self.child :options];
}

- (RNNRootViewController *)createChild {
    RNNNavigationOptions *options = [RNNNavigationOptions new];
    options.bottomTab = [RNNBottomTabOptions new];
    id img = [OCMockObject partialMockForObject:[UIImage new]];

    options.bottomTab.icon = [[Image alloc] initWithValue:img];
    return [OCMockObject partialMockForObject:[[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[RNNViewControllerPresenter new] options:options defaultOptions:nil]];
}

- (BOOL)tabHasIndicator {
    return [self.child tabBarItem].tag > 0;
}

- (UIView *)getIndicator {
    return [self tabHasIndicator] ? [[((UITabBarController *) _bottomTabs) tabBar] viewWithTag:_child.tabBarItem.tag] : nil;
}
@end
