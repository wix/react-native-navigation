#import <XCTest/XCTest.h>
#import "RNNNavigationOptions.h"

@interface RNNNavigationOptionsTest : XCTestCase

@end

@implementation RNNNavigationOptionsTest

- (void)setUp {
    [super setUp];
}

- (void)testInitCreatesInstanceType {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	XCTAssertTrue([options isKindOfClass:[RNNNavigationOptions class]]);
}
- (void)testAddsStyleFromDictionaryWithInit {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"background" : @{@"color" : @(0xff0000ff)}}}];
	XCTAssertTrue(options.topBar.background.color);
}

- (void)testChangeRNNNavigationOptionsDynamically {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"background" : @{@"color" : @(0xff0000ff)}}}];
	NSDictionary* dynamicOptionsDict = @{@"topBar": @{@"textColor" : @(0xffff00ff), @"title" : @{@"text": @"hello"}}};
	RNNNavigationOptions* dynamicOptions = [[RNNNavigationOptions alloc] initWithDict:dynamicOptionsDict];
	[options overrideOptions:dynamicOptions];

	XCTAssertTrue([options.topBar.title.text.get isEqual:@"hello"]);
}

- (void)testChangeRNNNavigationOptionsWithInvalidProperties {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"background" : @{@"color" : @(0xff0000ff)}}}];
	NSDictionary* dynamicOptionsDict = @{@"topBar": @{@"titleeeee" : @"hello"}};
	RNNNavigationOptions* dynamicOptions = [[RNNNavigationOptions alloc] initWithDict:dynamicOptionsDict];
	XCTAssertNoThrow([options overrideOptions:dynamicOptions]);
}

- (void)testWithDefault {
	RNNNavigationOptions * options = [[RNNNavigationOptions alloc] initWithDict:@{
			@"topBar": @{
					@"subtitle" : @{@"text": @"hey"}
			},
			@"bottomTab": @{
					@"selectedIconColor": @(0xff000000)
			}
	}];
	RNNNavigationOptions * defaultOptions = [[RNNNavigationOptions alloc] initWithDict:@{
	        @"topBar": @{
                    @"subtitle": @{@"text": @"ho"},
                    @"title": @{@"text": @"hello"}
            },
			@"bottomTab": @{
	        		@"selectedIconColor": @(0xff0000ff)
	        }
	}];

	RNNNavigationOptions * withDefault = [options withDefault:defaultOptions];
	XCTAssertEqual(withDefault.topBar.subtitle.text.get, @"hey");
	XCTAssertEqual(withDefault.bottomTab.selectedIconColor.get, options.bottomTab.selectedIconColor.get);
}

//
//- (void)test_applyDefaultOptions {
//	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initEmptyOptions];
//	UIViewController* viewController = [UIViewController new];
//	UINavigationController* navigationController = [[UINavigationController alloc] initWithRootViewController:viewController];
//	UITabBarController* tabBarController = [[UITabBarController alloc] init];
//	[tabBarController setViewControllers:@[navigationController]];
//	
//	[options applyDefaultOptionsOn:viewController];
//	
//	XCTAssertFalse(navigationController.navigationBar.hidden);
//	XCTAssertFalse(navigationController.navigationBar.translucent);
//	XCTAssertFalse(navigationController.navigationBar.clipsToBounds);
//	XCTAssertFalse(navigationController.hidesBarsOnSwipe);
//	XCTAssertTrue(navigationController.navigationBar.barStyle == UIBarStyleDefault);
//	
//	XCTAssertNil(tabBarController.tabBar.barTintColor);
//	XCTAssertTrue(tabBarController.tabBar.barStyle == UIBarStyleDefault);
//	XCTAssertFalse(tabBarController.tabBar.translucent);
//}

@end
