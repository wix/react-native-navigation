#import <XCTest/XCTest.h>
#import "RNNNavigationOptions.h"

@interface RNNNavigationOptionsTest : XCTestCase

@end

@implementation RNNNavigationOptionsTest

- (void)setUp {
    [super setUp];
}

-(void)testInitCreatesInstanceType{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	XCTAssertTrue([options isKindOfClass:[RNNNavigationOptions class]]);
}
-(void)testAddsStyleFromDictionaryWithInit{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"backgroundColor" : @(0xff0000ff)}}];
	XCTAssertTrue(options.topBar.backgroundColor);
}
-(void)testThrowsWhenStyleDoesNotExist{
	XCTAssertThrows([[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"someColor" : @(0xff0000ff)}}]);
}

-(void)testChangeRNNNavigationOptionsDynamically{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"backgroundColor" : @(0xff0000ff)}}];
	NSDictionary* dynamicOptions = @{@"topBar": @{@"textColor" : @(0xffff00ff), @"title" : @"hello"}};
	[options mergeWith:dynamicOptions];
	XCTAssertTrue([options.topBar.title isEqual:@"hello"]);
}

-(void)testChangeRNNNavigationOptionsWithInvalidProperties{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBar": @{@"backgroundColor" : @(0xff0000ff)}}];
	NSDictionary* dynamicOptions = @{@"topBar": @{@"titleeeee" : @"hello"}};
	XCTAssertThrows([options mergeWith:dynamicOptions]);
}

@end
