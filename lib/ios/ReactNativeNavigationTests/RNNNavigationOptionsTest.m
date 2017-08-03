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
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBarBackgroundColor" : @(0xff0000ff)}];
	XCTAssertTrue(options.topBarBackgroundColor);
}
-(void)testReturnsNilWhenStyleDoesNotExist{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBarBackgroundColor" : @(0xff0000ff)}];
	XCTAssertNil(options.topBarTextColor);
}

-(void)testChangeRNNNavigationOptionsDynamically{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBarBackgroundColor" : @(0xff0000ff)}];
	NSDictionary* dynamicOptions = @{@"topBarTextColor" : @(0xffff00ff), @"title" : @"hello"};
    [options mergeWith:dynamicOptions];
	XCTAssertTrue([options.title isEqual:@"hello"]);	
}

-(void)testChangeRNNNavigationOptionsWithInvalidProperties{
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:@{@"topBarBackgroundColor" : @(0xff0000ff)}];
	NSDictionary* dynamicOptions = @{@"titleeeee" : @"hello"};
	XCTAssertThrows([options mergeWith:dynamicOptions]);
}
@end
