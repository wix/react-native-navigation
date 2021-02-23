#import "BottomTabPresenterCreator.h"
#import "BottomTabsPresenterCreator.h"
#import "RNNBottomTabsController.h"
#import "RNNBottomTabsPresenter.h"
#import "RNNComponentViewController.h"
#import "RNNDotIndicatorPresenter.h"
#import "UITabBarController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNBottomTabsAppearancePresenterTest : XCTestCase

@property(nonatomic, strong) RNNBottomTabsPresenter *uut;
@property(nonatomic, strong) NSArray<UIViewController *> *children;
@property(nonatomic, strong) id dotIndicatorPresenter;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) id boundViewController;

@end

@implementation RNNBottomTabsAppearancePresenterTest

- (void)setUp {
    [super setUp];
    self.children = @[ [[RNNComponentViewController alloc]
        initWithLayoutInfo:nil
           rootViewCreator:nil
              eventEmitter:nil
                 presenter:[[RNNComponentPresenter alloc] initWithDefaultOptions:nil]
                   options:nil
            defaultOptions:nil] ];
    self.dotIndicatorPresenter = [OCMockObject
        partialMockForObject:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:nil]];
    self.uut = [OCMockObject
        partialMockForObject:[BottomTabsPresenterCreator createWithDefaultOptions:nil]];
    self.boundViewController = [OCMockObject
        partialMockForObject:[[RNNBottomTabsController alloc]
                                    initWithLayoutInfo:nil
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:self.uut
                                    bottomTabPresenter:[BottomTabPresenterCreator
                                                           createWithDefaultOptions:nil]
                                 dotIndicatorPresenter:self.dotIndicatorPresenter
                                          eventEmitter:nil
                                  childViewControllers:self.children
                                    bottomTabsAttacher:nil]];
    [self.boundViewController viewWillAppear:YES];
    [self.uut bindViewController:self.boundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
}

- (void)testApplyOptions_shouldSetDefaultEmptyOptions {
    RNNNavigationOptions *emptyOptions = [RNNNavigationOptions emptyOptions];
    [[self.boundViewController expect] setTabBarTestID:nil];
    [[(id)self.uut expect] applyBackgroundColor:nil translucent:NO];
    [[self.boundViewController expect] setTabBarHideShadow:NO];
    [[self.boundViewController expect] setTabBarVisible:YES];
    [[self.boundViewController expect] setTabBarStyle:UIBarStyleDefault];
    [self.uut applyOptions:emptyOptions];
    [self.boundViewController verify];
}

- (void)testApplyOptions_shouldApplyOptions {
    RNNNavigationOptions *initialOptions = [RNNNavigationOptions emptyOptions];
    initialOptions.bottomTabs.testID = [[Text alloc] initWithValue:@"testID"];
    initialOptions.bottomTabs.backgroundColor = [[Color alloc] initWithValue:[UIColor redColor]];
    initialOptions.bottomTabs.translucent = [[Bool alloc] initWithValue:@(0)];
    initialOptions.bottomTabs.hideShadow = [[Bool alloc] initWithValue:@(1)];
    initialOptions.bottomTabs.visible = [[Bool alloc] initWithValue:@(0)];
    initialOptions.bottomTabs.barStyle = [[Text alloc] initWithValue:@"black"];

    [[self.boundViewController expect] setTabBarTestID:@"testID"];
    [[(id)self.uut expect] applyBackgroundColor:nil translucent:[UIColor redColor]];
    [[self.boundViewController expect] setTabBarHideShadow:YES];
    [[self.boundViewController expect] setTabBarStyle:UIBarStyleBlack];

    [self.uut applyOptions:initialOptions];
    [self.boundViewController verify];
}

- (void)testApplyOptions_shouldRestoreHiddenTabBar {
    RNNNavigationOptions *initialOptions = [RNNNavigationOptions emptyOptions];
    initialOptions.bottomTabs.visible = [[Bool alloc] initWithValue:@(1)];

    [[self.boundViewController expect] setTabBarVisible:YES];

    [self.uut applyOptions:initialOptions];
    [self.boundViewController verify];
}

- (void)testApplyOptionsOnInit_alwaysShow_shouldNotCenterTabImages {
    RNNNavigationOptions *initialOptions = [RNNNavigationOptions emptyOptions];
    initialOptions.bottomTabs.titleDisplayMode = [[Text alloc] initWithValue:@"alwaysShow"];
    [[self.boundViewController reject] centerTabItems];
    [self.uut applyOptionsOnInit:initialOptions];
    [self.boundViewController verify];
}

- (void)testApplyOptions_shouldApplyOptionsOnInit_alwaysHide_shouldCenterTabImages {
    RNNNavigationOptions *initialOptions = [RNNNavigationOptions emptyOptions];
    initialOptions.bottomTabs.titleDisplayMode = [[Text alloc] initWithValue:@"alwaysHide"];
    [[self.boundViewController expect] centerTabItems];
    [self.uut applyOptionsOnInit:initialOptions];
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

- (void)testTabBarBackgroundColor {
    UIColor *tabBarBackgroundColor = [UIColor redColor];
    [self.uut setTabBarBackgroundColor:tabBarBackgroundColor];
    XCTAssertTrue([self.children.lastObject.tabBarItem.standardAppearance.backgroundColor
        isEqual:tabBarBackgroundColor]);
}

- (void)testApplyOptions_applyTabBarShadowDefaultValues {
    UITabBar *tabBar = ((UITabBarController *)self.boundViewController).tabBar;
    CGFloat defaultOpacity = tabBar.layer.shadowOpacity;
    CGColorRef defaultColor = tabBar.layer.shadowColor;
    CGFloat defaultRadius = tabBar.layer.shadowRadius;

    [self.uut applyOptions:self.options];
    XCTAssertTrue(tabBar.layer.shadowRadius == defaultRadius);
    XCTAssertTrue(tabBar.layer.shadowOpacity == defaultOpacity);
    XCTAssertTrue(tabBar.layer.shadowColor == defaultColor);
}

- (void)testApplyOptions_applyTabBarShadowRadius {
    self.options.bottomTabs.shadow.radius = [Number withValue:@(10.0)];
    [self.uut applyOptions:self.options];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowRadius ==
                  10.0);
}

- (void)testApplyOptions_applyTabBarShadowColor {
    self.options.bottomTabs.shadow.color = [Color withValue:UIColor.redColor];
    [self.uut applyOptions:self.options];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowColor ==
                  UIColor.redColor.CGColor);
}

- (void)testApplyOptions_applyTabBarShadowOpacity {
    self.options.bottomTabs.shadow.opacity = [Number withValue:@(0.5)];
    [self.uut applyOptions:self.options];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowOpacity ==
                  0.5);
}

- (void)testMergeOptions_applyTabBarShadowRadius {
    RNNNavigationOptions *mergeOptions = RNNNavigationOptions.emptyOptions;
    mergeOptions.bottomTabs.shadow.radius = [Number withValue:@(10.0)];
    [self.uut mergeOptions:mergeOptions resolvedOptions:nil];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowRadius ==
                  10.0);
}

- (void)testMergeOptions_applyTabBarShadowColor {
    RNNNavigationOptions *mergeOptions = RNNNavigationOptions.emptyOptions;
    mergeOptions.bottomTabs.shadow.color = [Color withValue:UIColor.redColor];
    [self.uut mergeOptions:mergeOptions resolvedOptions:nil];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowColor ==
                  UIColor.redColor.CGColor);
}

- (void)testMergeOptions_applyTabBarShadowOpacity {
    RNNNavigationOptions *mergeOptions = RNNNavigationOptions.emptyOptions;
    mergeOptions.bottomTabs.shadow.opacity = [Number withValue:@(0.5)];
    [self.uut mergeOptions:mergeOptions resolvedOptions:nil];
    XCTAssertTrue(((UITabBarController *)self.boundViewController).tabBar.layer.shadowOpacity ==
                  0.5);
}

@end
