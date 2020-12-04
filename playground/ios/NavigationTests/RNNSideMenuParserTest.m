#import "RCTConvert+SideMenuOpenGestureMode.h"
#import "SideMenuOpenGestureModeParser.h"
#import <XCTest/XCTest.h>

@interface RNNSideMenuParserTest : XCTestCase

@end

@implementation RNNSideMenuParserTest

- (void)setUp {
    [super setUp];
}

- (void)testParseBezelOpenModeReturnDrawerGestureModeBezel {
    NSDictionary *dict = @{@"openMode" : @"bezel"};
    SideMenuOpenMode *openMode = [SideMenuOpenGestureModeParser parse:dict key:@"openMode"];
    XCTAssertEqual(openMode.get.integerValue, MMOpenDrawerGestureModeBezelPanningCenterView);
}

- (void)testParseEntireScreenOpenModeReturnDrawerGestureModeAll {
    NSDictionary *dict = @{@"openMode" : @"entireScreen"};
    SideMenuOpenMode *openMode = [SideMenuOpenGestureModeParser parse:dict key:@"openMode"];
    XCTAssertEqual(openMode.get.integerValue, MMOpenDrawerGestureModeAll);
}

@end
