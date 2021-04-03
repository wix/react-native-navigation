#import "RNNNavigationOptions.h"
#import <XCTest/XCTest.h>

@interface RNNNavigationOptionsTest : XCTestCase

@end

@implementation RNNNavigationOptionsTest

- (void)setUp {
    [super setUp];
}

- (void)testInitCreatesInstanceType {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{}];
    XCTAssertTrue([options isKindOfClass:[RNNNavigationOptions class]]);
}
- (void)testAddsStyleFromDictionaryWithInit {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc]
        initWithDict:@{@"topBar" : @{@"background" : @{@"color" : @(0xff0000ff)}}}];
    XCTAssertTrue(options.topBar.background.color);
}

- (void)testAddsFontToBackButtonOptions {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc]
        initWithDict:@{@"topBar" : @{@"backButton" : @{@"fontFamily" : @"HelveticaNeue"}}}];
    XCTAssertTrue([options.topBar.backButton.fontFamily.get isEqual:@"HelveticaNeue"]);
}

- (void)testChangeRNNNavigationOptionsDynamically {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc]
        initWithDict:@{@"topBar" : @{@"background" : @{@"color" : @(0xff0000ff)}}}];
    NSDictionary *dynamicOptionsDict =
        @{@"topBar" : @{@"textColor" : @(0xffff00ff), @"title" : @{@"text" : @"hello"}}};
    RNNNavigationOptions *dynamicOptions =
        [[RNNNavigationOptions alloc] initWithDict:dynamicOptionsDict];
    [options mergeOptions:dynamicOptions];

    XCTAssertTrue([options.topBar.title.text.get isEqual:@"hello"]);
}

- (void)testChangeRNNNavigationOptionsWithInvalidProperties {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc]
        initWithDict:@{@"topBar" : @{@"background" : @{@"color" : @(0xff0000ff)}}}];
    NSDictionary *dynamicOptionsDict = @{@"topBar" : @{@"titleeeee" : @"hello"}};
    RNNNavigationOptions *dynamicOptions =
        [[RNNNavigationOptions alloc] initWithDict:dynamicOptionsDict];
    XCTAssertNoThrow([options mergeOptions:dynamicOptions]);
}

- (void)testWithDefault {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"hey"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff000000)}
    }];
    RNNNavigationOptions *defaultOptions = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"ho"}, @"title" : @{@"text" : @"hello"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff0000ff)}
    }];

    RNNNavigationOptions *withDefault = [options withDefault:defaultOptions];
    XCTAssertEqual(withDefault.topBar.subtitle.text.get, @"hey");
    XCTAssertEqual(withDefault.bottomTab.selectedIconColor.get,
                   options.bottomTab.selectedIconColor.get);
}

- (void)testWithDefault_shouldReturnCopiedObject {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"hey"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff000000)}
    }];
    RNNNavigationOptions *defaultOptions = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"ho"}, @"title" : @{@"text" : @"hello"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff0000ff)}
    }];

    RNNNavigationOptions *withDefault = [options withDefault:defaultOptions];
    XCTAssertTrue(withDefault != options);
    XCTAssertTrue(withDefault != defaultOptions);
}

- (void)testMergeOptions_shouldOverridePreviousOptions {
    RNNNavigationOptions *options = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"ho"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff000000)}
    }];
    RNNNavigationOptions *mergeOptions = [[RNNNavigationOptions alloc] initWithDict:@{
        @"topBar" : @{@"subtitle" : @{@"text" : @"hey"}, @"title" : @{@"text" : @"hello"}},
        @"bottomTab" : @{@"selectedIconColor" : @(0xff0000ff)}
    }];

    RNNNavigationOptions *merged = [options mergeOptions:mergeOptions];
    XCTAssertTrue([merged.topBar.subtitle.text.get isEqualToString:@"hey"]);
    XCTAssertTrue([merged.topBar.title.text.get isEqualToString:@"hello"]);
}

@end
