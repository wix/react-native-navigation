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
	NSDictionary* dynamicOptions = @{@"topBar": @{@"textColor" : @(0xffff00ff), @"title" : @{@"text": @"hello"}}};
	[options mergeWith:dynamicOptions];
	XCTAssertTrue([options.topBar.title.text isEqual:@"hello"]);
}

- (void)testChangeRNNNavigationOptionsWithInvalidProperties {
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"background" : @{@"color" : @(0xff0000ff)}}}];
	NSDictionary* dynamicOptions = @{@"topBar": @{@"titleeeee" : @"hello"}};
	XCTAssertNoThrow([options mergeWith:dynamicOptions]);
}

@end
