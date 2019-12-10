#import <XCTest/XCTest.h>
#import "UINavigationController+RNNOptions.h"

@interface UINavigationController_RNNOptionsTest : XCTestCase

@end

@implementation UINavigationController_RNNOptionsTest

- (void)setUp {
    [super setUp];
}

- (void)testSetBackButtonIcon_withColor_shouldSetColor {
	UIViewController* vc = [UIViewController new];
	UINavigationController* uut = [[UINavigationController alloc] initWithRootViewController:vc];
	UIColor* color = [UIColor blackColor];

	[uut setBackButtonIcon:nil withColor:color title:nil showTitle:nil];
	XCTAssertEqual(color, vc.navigationItem.backBarButtonItem.tintColor);
}

- (void)testSetBackButtonIcon_withColor_shouldSetTitle {
	UIViewController* vc = [UIViewController new];
	UINavigationController* uut = [[UINavigationController alloc] initWithRootViewController:vc];
    NSString* title = @"Title";

    [uut setBackButtonIcon:nil withColor:nil title:title showTitle:YES];
	XCTAssertEqual(title, vc.navigationItem.backBarButtonItem.title);
}

//- (void)testSetBackButtonIcon_withColor_shouldSetIcon {
//	UIViewController* vc = [UIViewController new];
//	UINavigationController* uut = [[UINavigationController alloc] initWithRootViewController:vc];
//    UIImage* icon = [UIImage new];
//
//    [uut setBackButtonIcon:icon withColor:nil title:nil showTitle:nil];
//	XCTAssertEqual(icon, vc.navigationItem.backBarButtonItem.image);
//}

- (void)testSetBackButtonIcon_shouldSetTitleOnPreviousViewControllerIfExists {
	UIViewController* viewController1 = [UIViewController new];
	UIViewController* viewController2 = [UIViewController new];
	UINavigationController* uut = [[UINavigationController alloc] init];
	[uut setViewControllers:@[viewController1, viewController2]];
	NSString* title = @"Title";

	[uut setBackButtonIcon:nil withColor:nil title:title showTitle:YES];
	XCTAssertEqual(title, viewController1.navigationItem.backBarButtonItem.title);
}

@end
