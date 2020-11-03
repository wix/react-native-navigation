#import "ColorParser.h"
#import "NoColor.h"
#import "NullColor.h"
#import <XCTest/XCTest.h>

@interface ColorParserTest : XCTestCase
@end

@implementation ColorParserTest

- (void)setUp {
    [super setUp];
}

- (void)testParse_NSNumberColor {
    UIColor *expectedColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
    NSDictionary *colorDict = @{@"colorKey" : @(0xffffffff)};
    Color *color = [ColorParser parse:colorDict key:@"colorKey"];
    XCTAssertTrue([color.get isEqual:expectedColor]);
}

- (void)testParse_nilColor {
    NSDictionary *colorDict = @{};
    Color *color = [ColorParser parse:colorDict key:@"colorKey"];
    XCTAssertTrue([color isKindOfClass:NullColor.class]);
}

- (void)testParse_NoColor {
    NSDictionary *colorDict = @{@"colorKey" : @"NoColor"};
    Color *color = [ColorParser parse:colorDict key:@"colorKey"];
    XCTAssertTrue([color isKindOfClass:NoColor.class]);
}

@end
