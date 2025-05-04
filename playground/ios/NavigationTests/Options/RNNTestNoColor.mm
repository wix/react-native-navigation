#import "NoColor.h"
#import <XCTest/XCTest.h>

@interface RNNTestNoColor : XCTestCase

@property(nonatomic, retain) NoColor *uut;

@end

@implementation RNNTestNoColor
- (void)setUp {
    [super setUp];
    self.uut = [[NoColor alloc] init];
}

- (void)testHasValue_alwaysReturnsYES {
    XCTAssertEqual([_uut hasValue], YES);
}

- (void)testGet_returnsNil {
    XCTAssertEqual(_uut.get, (UIColor *)nil);
}

@end