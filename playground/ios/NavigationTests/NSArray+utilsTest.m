#import <ReactNativeNavigation/NSArray+utils.h>
#import <XCTest/XCTest.h>

@interface NSArray_utilsTest : XCTestCase
@property(nonatomic, retain) NSArray *uut;
@end

@implementation NSArray_utilsTest

- (void)testIntersectByValue {
    NSArray *uut = @[ @{@"test" : @(1)} ];
    NSArray *intersection = [uut intersect:@[ @{@"test" : @(1)} ] withPropertyName:@"test"];
    XCTAssertTrue(intersection.count > 0);
}

- (void)testIntersectByReference {
    NSObject *byRef = [NSObject new];
    NSArray *uut = @[ @{@"test" : byRef} ];
    NSArray *intersection = [uut intersect:@[ @{@"test" : byRef} ] withPropertyName:@"test"];
    XCTAssertTrue(intersection.count > 0);
}

- (void)testDifference {
    NSObject *byRef = [NSObject new];
    NSArray *uut = @[ @{@"test" : byRef}, @{@"test" : [NSObject new]} ];
    NSArray *difference = [uut difference:@[ @{@"test" : byRef} ] withPropertyName:@"test"];
    XCTAssertTrue(difference.count == 1);
}

@end
