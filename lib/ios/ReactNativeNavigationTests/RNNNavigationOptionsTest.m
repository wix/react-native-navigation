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


@end
