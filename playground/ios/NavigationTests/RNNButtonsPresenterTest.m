#import "RNNComponentViewController+Utils.h"
#import <ReactNativeNavigation/RNNButtonsPresenter.h>
#import <XCTest/XCTest.h>

@interface RNNButtonsPresenterTest : XCTestCase

@property(nonatomic, retain) RNNButtonsPresenter *uut;
@property(nonatomic, retain) UIViewController *viewController;

@end

@implementation RNNButtonsPresenterTest

- (void)setUp {
    _viewController = UIViewController.new;
    __unused UINavigationController *navigationController =
        [[UINavigationController alloc] initWithRootViewController:_viewController];
    _uut = [[RNNButtonsPresenter alloc] initWithComponentRegistry:nil eventEmitter:nil];
    [_uut bindViewController:_viewController];
}

- (void)testApplyButtons_shouldNotAddEmptyButton {
    [_uut applyLeftButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId"}] ]
                defaultColor:nil
        defaultDisabledColor:nil
                    animated:NO];
    XCTAssertTrue(_viewController.navigationItem.leftBarButtonItems.count == 0);

    [_uut applyRightButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId"}] ]
                defaultColor:nil
        defaultDisabledColor:nil
                    animated:NO];
    XCTAssertTrue(_viewController.navigationItem.rightBarButtonItems.count == 0);
}

- (void)testApplyButtons_shouldAddButtonWithTitle {
    [_uut applyLeftButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}] ]
                defaultColor:nil
        defaultDisabledColor:nil
                    animated:NO];
    XCTAssertTrue(_viewController.navigationItem.leftBarButtonItems.count == 1);

    [_uut applyRightButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}] ]
                defaultColor:nil
        defaultDisabledColor:nil
                    animated:NO];
    XCTAssertTrue(_viewController.navigationItem.rightBarButtonItems.count == 1);
}

- (void)testApplyButtons_shouldCreateCustomButtonView {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId"}];
    button.icon = [Image withValue:UIImage.new];
    button.iconBackground.color = [Color withValue:UIColor.blackColor];
    [_uut applyLeftButtons:@[ button ] defaultColor:nil defaultDisabledColor:nil animated:NO];
    XCTAssertNotNil([_viewController.navigationItem.leftBarButtonItems.lastObject customView]);
}

- (void)testApplyLeftButtonColor_shouldApplyTintColor {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId"}];
    button.icon = [Image withValue:UIImage.new];
    [_uut applyLeftButtons:@[ button ] defaultColor:nil defaultDisabledColor:nil animated:NO];
    [_uut applyLeftButtonsColor:UIColor.redColor];
    XCTAssertEqual(_viewController.navigationItem.leftBarButtonItems.firstObject.tintColor,
                   UIColor.redColor);
}

- (void)testApplyLeftButtonColor_shouldApplyTextAttributesColor {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}];
    [_uut applyLeftButtons:@[ button ] defaultColor:nil defaultDisabledColor:nil animated:NO];
    [_uut applyLeftButtonsColor:UIColor.redColor];
    XCTAssertEqual([[_viewController.navigationItem.leftBarButtonItems.firstObject
                       titleTextAttributesForState:UIControlStateNormal]
                       valueForKey:NSForegroundColorAttributeName],
                   UIColor.redColor);
    XCTAssertEqual([[_viewController.navigationItem.leftBarButtonItems.firstObject
                       titleTextAttributesForState:UIControlStateHighlighted]
                       valueForKey:NSForegroundColorAttributeName],
                   UIColor.redColor);
}

- (void)testApplyRightButtonColor_shouldApplyTintColor {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId"}];
    button.icon = [Image withValue:UIImage.new];
    [_uut applyRightButtons:@[ button ] defaultColor:nil defaultDisabledColor:nil animated:NO];
    [_uut applyRightButtonsColor:UIColor.redColor];
    XCTAssertEqual(_viewController.navigationItem.rightBarButtonItems.firstObject.tintColor,
                   UIColor.redColor);
}

- (void)testApplyRightButtonColor_shouldApplyTextAttributesColor {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}];
    [_uut applyRightButtons:@[ button ] defaultColor:nil defaultDisabledColor:nil animated:NO];
    [_uut applyRightButtonsColor:UIColor.redColor];
    XCTAssertEqual([[_viewController.navigationItem.rightBarButtonItems.firstObject
                       titleTextAttributesForState:UIControlStateNormal]
                       valueForKey:NSForegroundColorAttributeName],
                   UIColor.redColor);
    XCTAssertEqual([[_viewController.navigationItem.rightBarButtonItems.firstObject
                       titleTextAttributesForState:UIControlStateHighlighted]
                       valueForKey:NSForegroundColorAttributeName],
                   UIColor.redColor);
}

- (RNNButtonOptions *)buttonWithDict:(NSDictionary *)buttonDict {
    return [[RNNButtonOptions alloc] initWithDict:buttonDict];
}

@end
