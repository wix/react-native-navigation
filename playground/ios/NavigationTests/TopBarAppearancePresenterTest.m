#import "RNNComponentViewController+Utils.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <ReactNativeNavigation/RNNStackController.h>
#import <ReactNativeNavigation/TopBarAppearancePresenter.h>
#import <XCTest/XCTest.h>

@interface TopBarAppearancePresenterTest : XCTestCase

@end

@implementation TopBarAppearancePresenterTest {
    TopBarAppearancePresenter *_uut;
    RNNStackController *_stack;
    RNNComponentViewController *_componentViewController;
}

- (void)setUp {
    [super setUp];
    _componentViewController = [RNNComponentViewController createWithComponentId:@"componentId"];
    _uut = [[TopBarAppearancePresenter alloc] initWithNavigationController:_stack];
    _stack = [[RNNStackController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:[RNNNavigationOptions emptyOptions]
                                             defaultOptions:[RNNNavigationOptions emptyOptions]
                                                  presenter:_uut
                                               eventEmitter:nil
                                       childViewControllers:@[ _componentViewController ]];
}

- (void)testMergeOptions_shouldMergeWithDefault {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    defaultOptions.topBar.title.color = [Color withColor:UIColor.redColor];

    mergeOptions.topBar.title.fontSize = [Number withValue:@(21)];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(_stack.childViewControllers.lastObject.navigationItem.standardAppearance
                       .titleTextAttributes[NSForegroundColorAttributeName],
                   UIColor.redColor);
    UIFont *font = _stack.childViewControllers.lastObject.navigationItem.standardAppearance
                       .titleTextAttributes[NSFontAttributeName];
    XCTAssertEqual(font.pointSize, 21);
}

- (void)testMergeOptions_shouldShowBorder {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.noBorder = [Bool withValue:NO];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor,
        [[UINavigationBarAppearance new] shadowColor]);
}

- (void)testMergeOptions_shouldHideBorder {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.noBorder = [Bool withValue:YES];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor, nil);
}

- (void)testMergeOptions_shouldSetBorderColor {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.noBorder = [Bool withValue:NO];
    mergeOptions.topBar.borderColor = [Color withValue:UIColor.blueColor];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor,
        UIColor.blueColor);
}

- (void)testShowBorder_shouldPreserveBorderColor {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];

    mergeOptions.topBar.noBorder = [Bool withValue:NO];
    mergeOptions.topBar.borderColor = [Color withValue:UIColor.blueColor];

    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor,
        UIColor.blueColor);

    mergeOptions.topBar.noBorder = [Bool withValue:YES];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor, nil);

    mergeOptions.topBar.noBorder = [Bool withValue:NO];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor,
        UIColor.blueColor);
}

- (void)testApplyOptions_shouldApplyTitleAppearance {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.title.color = [Color withColor:UIColor.redColor];
    options.topBar.title.fontSize = [Number withValue:@(21)];

    [_uut applyOptions:options.topBar];
    XCTAssertEqual(_stack.childViewControllers.lastObject.navigationItem.standardAppearance
                       .titleTextAttributes[NSForegroundColorAttributeName],
                   UIColor.redColor);
    XCTAssertEqual([_stack.childViewControllers.lastObject.navigationItem.standardAppearance
                           .titleTextAttributes[NSFontAttributeName] pointSize],
                   21);

    XCTAssertEqual(_stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance
                       .titleTextAttributes[NSForegroundColorAttributeName],
                   UIColor.redColor);
    XCTAssertEqual([_stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance
                           .titleTextAttributes[NSFontAttributeName] pointSize],
                   21);
}

- (void)testMergeOptions_shouldShowScrollEdgeBorder {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.scrollEdgeAppearance.noBorder = [Bool withValue:NO];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance.shadowColor,
        [[UINavigationBarAppearance new] shadowColor]);
}

- (void)testMergeOptions_shouldHideScrollEdgeBorder {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.noBorder = [Bool withValue:YES];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.standardAppearance.shadowColor, nil);
}

- (void)testMergeOptions_shouldSetScrollEdgeBorderColor {
    RNNNavigationOptions *mergeOptions = [RNNNavigationOptions emptyOptions];
    RNNNavigationOptions *defaultOptions = [RNNNavigationOptions emptyOptions];

    mergeOptions.topBar.scrollEdgeAppearance.noBorder = [Bool withValue:NO];
    mergeOptions.topBar.scrollEdgeAppearance.borderColor = [Color withValue:UIColor.blueColor];
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:defaultOptions];
    [_uut mergeOptions:mergeOptions.topBar withDefault:withDefault.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance.shadowColor,
        UIColor.blueColor);
}

- (void)testApplyOptions_shouldHideScrollEdgeBorder {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.scrollEdgeAppearance.noBorder = [Bool withValue:YES];
    options.topBar.scrollEdgeAppearance.borderColor = [Color withColor:UIColor.redColor];

    [_uut applyOptions:options.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance.shadowColor,
        nil);
}

- (void)testApplyOptions_shouldShowScrollEdgeBorderWithDefaultColor {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.scrollEdgeAppearance.noBorder = [Bool withValue:NO];

    [_uut applyOptions:options.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance.shadowColor,
        [[UINavigationBarAppearance new] shadowColor]);
}

- (void)testApplyOptions_shouldShowScrollEdgeBorderWithColor {
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.topBar.scrollEdgeAppearance.noBorder = [Bool withValue:NO];
    options.topBar.scrollEdgeAppearance.borderColor = [Color withColor:UIColor.redColor];

    [_uut applyOptions:options.topBar];
    XCTAssertEqual(
        _stack.childViewControllers.lastObject.navigationItem.scrollEdgeAppearance.shadowColor,
        UIColor.redColor);
}

@end
