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
        defaultButtonStyle:nil];
    XCTAssertTrue(_viewController.navigationItem.leftBarButtonItems.count == 0);

    [_uut applyRightButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId"}] ]
         defaultButtonStyle:nil];
    XCTAssertTrue(_viewController.navigationItem.rightBarButtonItems.count == 0);
}

- (void)testApplyButtons_shouldAddButtonWithTitle {
    [_uut applyLeftButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}] ]
        defaultButtonStyle:nil];
    XCTAssertTrue(_viewController.navigationItem.leftBarButtonItems.count == 1);

    [_uut applyRightButtons:@[ [self buttonWithDict:@{@"id" : @"buttonId", @"text" : @"title"}] ]
         defaultButtonStyle:nil];
    XCTAssertTrue(_viewController.navigationItem.rightBarButtonItems.count == 1);
}

- (void)testApplyButtons_shouldCreateCustomButtonView {
    RNNButtonOptions *button = [self buttonWithDict:@{@"id" : @"buttonId"}];
    button.icon = [Image withValue:UIImage.new];
    button.iconBackground.color = [Color withValue:UIColor.blackColor];
    [_uut applyLeftButtons:@[ button ] defaultButtonStyle:nil];
    XCTAssertNotNil([_viewController.navigationItem.leftBarButtonItems.lastObject customView]);
}

- (RNNButtonOptions *)buttonWithDict:(NSDictionary *)buttonDict {
    return [[RNNButtonOptions alloc] initWithDict:buttonDict];
}

@end
