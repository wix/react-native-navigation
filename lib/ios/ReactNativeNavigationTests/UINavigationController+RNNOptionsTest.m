#import <XCTest/XCTest.h>
#import "UINavigationController+RNNOptions.h"

@interface UINavigationController_RNNOptionsTest : XCTestCase

@end

@implementation UINavigationController_RNNOptionsTest

- (void)setUp {
    [super setUp];
}

- (void)testSetBackButtonIcon_withColor_shouldSetColor {
	UIViewController* viewController = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:viewController];
	UIColor* color = [UIColor blackColor];
	
	[nav rnn_setBackButtonIcon:nil withColor:color title:nil];
	XCTAssertEqual(color, viewController.navigationItem.backBarButtonItem.tintColor);
}

- (void)testSetBackButtonIcon_withColor_shouldSetTitle {
	UIViewController* viewController = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:viewController];
	NSString* title = @"Title";
	
	[nav rnn_setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, viewController.navigationItem.backBarButtonItem.title);
}

- (void)testSetBackButtonIcon_withColor_shouldSetIcon {
	UIViewController* viewController = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:viewController];
	UIImage* icon = [UIImage new];
	
	[nav rnn_setBackButtonIcon:icon withColor:nil title:nil];
	XCTAssertEqual(icon, viewController.navigationItem.backBarButtonItem.image);
}

- (void)testSetBackButtonIcon_shouldSetTitleOnPreviousViewControllerIfExists {
	UIViewController* viewController = [UIViewController new];
	UIViewController* viewController2 = [UIViewController new];
	UINavigationController* nav = [[UINavigationController alloc] init];
	[nav setViewControllers:@[viewController, viewController2]];
	NSString* title = @"Title";

	[nav rnn_setBackButtonIcon:nil withColor:nil title:title];
	XCTAssertEqual(title, viewController.navigationItem.backBarButtonItem.title);
}


@end
