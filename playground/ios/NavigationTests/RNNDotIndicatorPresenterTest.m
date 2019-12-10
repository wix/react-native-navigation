#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNDotIndicatorPresenter.h"
#import "DotIndicatorOptions.h"
#import "RNNBottomTabsController.h"
#import "RNNComponentViewController.h"
#import "RNNTestBase.h"
#import "UITabBarController+RNNUtils.h"

@interface RNNDotIndicatorPresenterTest : RNNTestBase
@property(nonatomic, strong) id uut;
@property(nonatomic, strong) RNNComponentViewController *child;
@property(nonatomic, strong) id bottomTabs;
@end

@implementation RNNDotIndicatorPresenterTest
- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[RNNDotIndicatorPresenter new]];
    self.bottomTabs = [OCMockObject partialMockForObject:[RNNBottomTabsController new]];
    self.child = [self createChild];
    [self.bottomTabs addChildViewController:self.child];

    [self setupTopLevelUI:self.bottomTabs];
}

- (void)tearDown {
    [self tearDownTopLevelUI:_bottomTabs];
    [super tearDown];
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

- (void)testApply_indicatorIsRemovedIfNotVisible {
    [self applyIndicator];
    XCTAssertTrue([self tabHasIndicator]);

    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:NO];
    [[self uut] apply:self.child :options];

    XCTAssertFalse([self tabHasIndicator]);
}

- (void)testApply_invisibleIndicatorIsNotAdded {
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

- (void)testApply_itRemovesPreviousDotIndicator {
    NSUInteger childCountBeforeApplyingIndicator = [[_bottomTabs getTabView:0] subviews].count;
    [self applyIndicator];
    NSUInteger childCountAfterApplyingIndicatorOnce = [[_bottomTabs getTabView:0] subviews].count;
    XCTAssertEqual(childCountBeforeApplyingIndicator + 1, childCountAfterApplyingIndicatorOnce);

    [self applyIndicator:[UIColor greenColor]];
    NSUInteger childCountAfterApplyingIndicatorTwice = [[_bottomTabs getTabView:0] subviews].count;
    XCTAssertEqual([[self getIndicator] backgroundColor], [UIColor greenColor]);
    XCTAssertEqual(childCountAfterApplyingIndicatorOnce, childCountAfterApplyingIndicatorTwice);
}

- (void)testApply_itRemovesPreviousIndicator {
    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:YES];
    options.color = [[Color alloc] initWithValue:[UIColor redColor]];
    options.size = [[Number alloc] initWithValue:[[NSNumber alloc] initWithInt:8]];

    [[self uut] apply:self.child :options];
    XCTAssertTrue([self tabHasIndicator]);

    options.visible = [[Bool alloc] initWithBOOL:NO];
    [[self uut] apply:self.child :options];
    XCTAssertFalse([self tabHasIndicator]);
}

- (void)testApply_indicatorIsAlignedToTopRightOfIcon {
    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:YES];
    options.size = [[Number alloc] initWithValue:[[NSNumber alloc] initWithInt:8]];
    [[self uut] apply:self.child :options];
    UIView *indicator = [self getIndicator];
    UIView * icon = [_bottomTabs getTabIcon:0];

    NSArray<NSLayoutConstraint *> *alignmentConstraints = [_bottomTabs getTabView:0].constraints;
    XCTAssertEqual([alignmentConstraints count], 2);
    XCTAssertEqual([alignmentConstraints[0] constant], -4);
    XCTAssertEqual([alignmentConstraints[0] firstItem], indicator);
    XCTAssertEqual([alignmentConstraints[0] secondItem], icon);
    XCTAssertEqual([alignmentConstraints[0] firstAttribute], NSLayoutAttributeLeft);
    XCTAssertEqual([alignmentConstraints[0] secondAttribute], NSLayoutAttributeRight);

    XCTAssertEqual([alignmentConstraints[1] constant], -4);
    XCTAssertEqual([alignmentConstraints[1] firstItem], indicator);
    XCTAssertEqual([alignmentConstraints[1] secondItem], icon);
    XCTAssertEqual([alignmentConstraints[1] firstAttribute], NSLayoutAttributeTop);
    XCTAssertEqual([alignmentConstraints[1] secondAttribute], NSLayoutAttributeTop);

    NSArray *sizeConstraints = indicator.constraints;
    XCTAssertEqual([sizeConstraints count], 2);
    XCTAssertEqual([sizeConstraints[0] constant], 8);
    XCTAssertEqual([sizeConstraints[1] constant], 8);
}

- (void)applyIndicator {
    [self applyIndicator:[UIColor redColor]];
}

- (void)applyIndicator:(UIColor *) color {
    DotIndicatorOptions *options = [DotIndicatorOptions new];
    options.visible = [[Bool alloc] initWithBOOL:YES];
    options.color = [[Color alloc] initWithValue:color];
    [[self uut] apply:self.child :options];
}

- (RNNComponentViewController *)createChild {
    RNNNavigationOptions *options = [RNNNavigationOptions new];
    options.bottomTab = [RNNBottomTabOptions new];
    id img = [OCMockObject partialMockForObject:[UIImage new]];

    options.bottomTab.icon = [[Image alloc] initWithValue:img];
    return [[RNNComponentViewController alloc] initWithLayoutInfo:nil rootViewCreator:nil eventEmitter:nil presenter:[RNNComponentPresenter new] options:options defaultOptions:nil];
}

- (BOOL)tabHasIndicator {
    return [self.child tabBarItem].tag > 0;
}

- (UIView *)getIndicator {
    return [self tabHasIndicator] ? [[((UITabBarController *) _bottomTabs) tabBar] viewWithTag:_child.tabBarItem.tag] : nil;
}
@end
