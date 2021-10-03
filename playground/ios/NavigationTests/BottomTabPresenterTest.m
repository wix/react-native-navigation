#import "RNNBasePresenter.h"
#import "RNNBottomTabsController+Helpers.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/BottomTabPresenter.h>
#import <ReactNativeNavigation/BottomTabPresenterCreator.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <XCTest/XCTest.h>

@interface RNNBottomTabPresenterTest : XCTestCase

@property(nonatomic, strong) BottomTabPresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNBottomTabsController *boundViewController;
@property(nonatomic, strong) RNNComponentViewController *componentViewController;
@property(nonatomic, strong) id mockBoundViewController;

@end

@implementation RNNBottomTabPresenterTest

- (void)setUp {
    [super setUp];
    self.uut =
        [BottomTabPresenterCreator createWithDefaultOptions:RNNNavigationOptions.emptyOptions];
    self.componentViewController = [RNNComponentViewController new];
    self.boundViewController =
        [RNNBottomTabsController createWithChildren:@[ self.componentViewController ]];
    self.mockBoundViewController = [OCMockObject partialMockForObject:self.boundViewController];
    [self.uut bindViewController:self.mockBoundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
}

- (void)tearDown {
    [super tearDown];
    [self.mockBoundViewController stopMocking];
    self.boundViewController = nil;
}

- (void)testApplyOptions_shouldSetTabBarItemBadgeWithValue {
    self.options.bottomTab.badge = [[Text alloc] initWithValue:@"badge"];
    [self.uut applyOptions:self.options child:self.componentViewController];
    XCTAssertEqual(self.componentViewController.tabBarItem.badgeValue, @"badge");
}

- (void)testMergeOptions_shouldSetTabBarItemColorWithDefaultOptions {
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    defaultOptions.bottomTab.selectedIconColor = [Color withColor:UIColor.greenColor];
    self.uut.defaultOptions = defaultOptions;

    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    mergeOptions.bottomTab.text = [[Text alloc] initWithValue:@"title"];

    [self.uut mergeOptions:mergeOptions
           resolvedOptions:self.options
                     child:self.componentViewController];
    XCTAssertEqual(self.componentViewController.tabBarItem.title, @"title");
}

- (void)testMergeOptions_shouldCreateNewTabBarItemInstance {
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    defaultOptions.bottomTab.selectedIconColor = [Color withColor:UIColor.greenColor];
    self.uut.defaultOptions = defaultOptions;

    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    mergeOptions.bottomTab.text = [[Text alloc] initWithValue:@"title"];

    UITabBarItem *currentTabBarItem = self.componentViewController.tabBarItem;
    [self.uut mergeOptions:mergeOptions
           resolvedOptions:self.options
                     child:self.componentViewController];
    UITabBarItem *newTabBarItem = self.componentViewController.tabBarItem;
    XCTAssertNotEqual(currentTabBarItem, newTabBarItem);
}

@end
