#import "RNNComponentPresenter.h"
#import "RNNComponentViewController+Utils.h"
#import "RNNStackController.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNBottomTabsController.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <XCTest/XCTest.h>

@interface UIViewController_LayoutProtocolTest : XCTestCase

@property(nonatomic, retain) UIViewController *uut;

@end

@implementation UIViewController_LayoutProtocolTest

- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
    _uut.layoutInfo = [[RNNLayoutInfo alloc] init];
    _uut.layoutInfo.componentId = @"componentId";
}

- (void)testInitWithLayoutApplyDefaultOptions {
    RNNComponentPresenter *presenter = [[RNNComponentPresenter alloc] init];
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    defaultOptions.modalPresentationStyle = [[Text alloc] initWithValue:@"default"];

    RNNComponentViewController *uut =
        [[RNNComponentViewController alloc] initWithLayoutInfo:nil
                                                       creator:nil
                                                       options:options
                                                defaultOptions:defaultOptions
                                                     presenter:presenter
                                                  eventEmitter:nil
                                          childViewControllers:nil];
    XCTAssertEqual(uut.modalPresentationStyle, UIModalPresentationPageSheet);
}

- (void)testInitWithLayoutInfoShouldSetChildViewControllers {
    UIViewController *child1 = [UIViewController new];
    UIViewController *child2 = [UIViewController new];
    NSArray *childViewControllers = @[ child1, child2 ];
    UINavigationController *uut =
        [[UINavigationController alloc] initWithLayoutInfo:nil
                                                   creator:nil
                                                   options:nil
                                            defaultOptions:nil
                                                 presenter:nil
                                              eventEmitter:nil
                                      childViewControllers:childViewControllers];

    XCTAssertEqual(uut.viewControllers.count, 2);
}

- (void)testInitBottomTabsWithLayoutInfoShouldNotSetChildViewControllers {
    UIViewController *child1 = [UIViewController new];
    UIViewController *child2 = [UIViewController new];
    NSArray *childViewControllers = @[ child1, child2 ];
    RNNBottomTabsController *uut =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:nil
                                             defaultOptions:nil
                                                  presenter:nil
                                               eventEmitter:nil
                                       childViewControllers:childViewControllers];

    XCTAssertEqual(uut.viewControllers.count, 0);
}

- (void)testLoadChildrenShouldSetChildViewControllers {
    UIViewController *child1 = [UIViewController new];
    UIViewController *child2 = [UIViewController new];
    NSArray *childViewControllers = @[ child1, child2 ];
    UINavigationController *uut =
        [[UINavigationController alloc] initWithLayoutInfo:nil
                                                   creator:nil
                                                   options:nil
                                            defaultOptions:nil
                                                 presenter:nil
                                              eventEmitter:nil
                                      childViewControllers:childViewControllers];
    [uut loadChildren:childViewControllers];
    XCTAssertEqual(uut.viewControllers[0], child1);
    XCTAssertEqual(uut.viewControllers[1], child2);
}

- (void)testResolveOptions {
    RNNNavigationOptions *childOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *parentOptions = [RNNNavigationOptions emptyOptions];
    parentOptions.bottomTab.text = [[Text alloc] initWithValue:@"text"];
    parentOptions.bottomTab.selectedIconColor = [[Color alloc] initWithValue:UIColor.redColor];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    defaultOptions.bottomTab.text = [[Text alloc] initWithValue:@"default text"];
    defaultOptions.bottomTab.selectedIconColor = [[Color alloc] initWithValue:UIColor.blueColor];

    RNNComponentViewController *child =
        [[RNNComponentViewController alloc] initWithLayoutInfo:[RNNLayoutInfo new]
                                                       creator:nil
                                                       options:childOptions
                                                defaultOptions:defaultOptions
                                                     presenter:[RNNComponentPresenter new]
                                                  eventEmitter:nil
                                          childViewControllers:nil];
    RNNStackController *parent =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:parentOptions
                                        defaultOptions:defaultOptions
                                             presenter:[RNNStackPresenter new]
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    XCTAssertEqual([parent getCurrentChild], child);
    XCTAssertEqual([[parent resolveOptions].bottomTab.text get], @"text");
    XCTAssertEqual([[parent resolveOptions].bottomTab.selectedIconColor get], UIColor.redColor);
}

- (void)testMergeOptions_invokedOnParentViewController {
    id parent = [OCMockObject partialMockForObject:[RNNStackController new]];
    RNNStackController *uut = [[RNNStackController alloc] initWithLayoutInfo:nil
                                                                     creator:nil
                                                                     options:nil
                                                              defaultOptions:nil
                                                                   presenter:nil
                                                                eventEmitter:nil
                                                        childViewControllers:nil];

    RNNNavigationOptions *toMerge = [RNNNavigationOptions emptyOptions];
    [(UIViewController *)[parent expect] mergeChildOptions:toMerge child:uut];

    [parent addChildViewController:uut];

    [uut mergeOptions:toMerge];
    [parent verify];
}

- (void)testMergeOptions_presenterIsInvokedWithResolvedOptions {
    id parent = [OCMockObject partialMockForObject:[RNNStackController new]];
    id presenter = [OCMockObject partialMockForObject:[RNNStackPresenter new]];
    RNNNavigationOptions *toMerge = [RNNNavigationOptions emptyOptions];
    toMerge.topBar.title.color = [[Color alloc] initWithValue:[UIColor redColor]];

    [[presenter expect] mergeOptions:toMerge
                     resolvedOptions:[OCMArg checkWithBlock:^(id value) {
                       RNNNavigationOptions *options = (RNNNavigationOptions *)value;
                       XCTAssertEqual([options.topBar.title.text get], @"Initial title");
                       XCTAssertEqual([options.bottomTab.text get], @"Child tab text");
                       return YES;
                     }]];

    RNNNavigationOptions *childOptions = [RNNNavigationOptions emptyOptions];
    childOptions.bottomTab.text = [[Text alloc] initWithValue:@"Child tab text"];
    RNNComponentViewController *child =
        [[RNNComponentViewController alloc] initWithLayoutInfo:[RNNLayoutInfo new]
                                                       creator:nil
                                                       options:childOptions
                                                defaultOptions:nil
                                                     presenter:[RNNComponentPresenter new]
                                                  eventEmitter:nil
                                          childViewControllers:nil];
    RNNNavigationOptions *initialOptions = [RNNNavigationOptions emptyOptions];
    initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"Initial title"];
    RNNStackController *uut = [[RNNStackController alloc] initWithLayoutInfo:[RNNLayoutInfo new]
                                                                     creator:nil
                                                                     options:initialOptions
                                                              defaultOptions:nil
                                                                   presenter:presenter
                                                                eventEmitter:nil
                                                        childViewControllers:@[ child ]];
    [parent addChildViewController:uut];

    [uut mergeOptions:toMerge];
    [presenter verify];
}

- (void)testMergeOptions_mergedIntoCurrentOptions {
    UIViewController *uut =
        [[UIViewController alloc] initWithLayoutInfo:nil
                                             creator:nil
                                             options:[RNNNavigationOptions emptyOptions]
                                      defaultOptions:nil
                                           presenter:nil
                                        eventEmitter:nil
                                childViewControllers:nil];
    RNNNavigationOptions *toMerge = [RNNNavigationOptions emptyOptions];
    toMerge.topBar.title.text = [[Text alloc] initWithValue:@"merged"];

    [uut mergeOptions:toMerge];
    XCTAssertEqual(uut.resolveOptions.topBar.title.text.get, @"merged");
}

- (void)testLayout_shouldExtendedLayoutIncludesOpaqueBars {
    UIViewController *component =
        [[UIViewController alloc] initWithLayoutInfo:nil
                                             creator:nil
                                             options:[RNNNavigationOptions emptyOptions]
                                      defaultOptions:nil
                                           presenter:nil
                                        eventEmitter:nil
                                childViewControllers:nil];
    UINavigationController *stack =
        [[UINavigationController alloc] initWithLayoutInfo:nil
                                                   creator:nil
                                                   options:[RNNNavigationOptions emptyOptions]
                                            defaultOptions:nil
                                                 presenter:nil
                                              eventEmitter:nil
                                      childViewControllers:nil];
    UITabBarController *tabBar =
        [[UITabBarController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:[RNNNavigationOptions emptyOptions]
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:nil];

    XCTAssertTrue(component.extendedLayoutIncludesOpaqueBars);
    XCTAssertTrue(stack.extendedLayoutIncludesOpaqueBars);
    XCTAssertTrue(tabBar.extendedLayoutIncludesOpaqueBars);
}

- (void)testConstants_shouldReturnNavigationBarHeight_visible {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.largeTitle.visible = [Bool withValue:YES];
    UIViewController *vc1 = [RNNComponentViewController createWithComponentId:@"vc1"];
    UIViewController *vc2 = [RNNComponentViewController createWithComponentId:@"vc2"
                                                               initialOptions:options];
    RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:options
                                        defaultOptions:nil
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ vc1 ]];
    RNNStackController *stack2 =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:options
                                        defaultOptions:nil
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ vc2 ]];

    RNNBottomTabsController *bottomTabs =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:nil
                                             defaultOptions:nil
                                                  presenter:RNNBasePresenter.new
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:nil
                                       childViewControllers:@[ stack, stack2 ]
                                         bottomTabsAttacher:nil];

    XCTAssertEqual([bottomTabs getTopBarHeight], stack.navigationBar.frame.size.height);
}

- (void)testConstants_shouldReturnNavigationBarHeight_invisible {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.visible = [Bool withValue:NO];
    UIViewController *vc1 = [RNNComponentViewController createWithComponentId:@"vc1"];
    UIViewController *vc2 = [RNNComponentViewController createWithComponentId:@"vc2"
                                                               initialOptions:options];
    RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:RNNNavigationOptions.emptyOptions
                                        defaultOptions:RNNNavigationOptions.emptyOptions
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ vc1 ]];
    RNNStackController *stack2 =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:nil
                                               options:RNNNavigationOptions.emptyOptions
                                        defaultOptions:RNNNavigationOptions.emptyOptions
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ vc2 ]];

    RNNBottomTabsController *bottomTabs =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:nil
                                             defaultOptions:nil
                                                  presenter:RNNBasePresenter.new
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:nil
                                       childViewControllers:@[ stack, stack2 ]
                                         bottomTabsAttacher:nil];
    [vc1 viewWillAppear:NO];
    XCTAssertNotEqual([bottomTabs getTopBarHeight], 0);

    [bottomTabs setSelectedIndex:1];
    [vc2 viewWillAppear:NO];
    XCTAssertEqual([bottomTabs getTopBarHeight], 0);
}

@end
