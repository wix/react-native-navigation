#import <ReactNativeNavigation/RNNButtonOptions.h>
#import <XCTest/XCTest.h>

@interface RNNButtonOptionsTest : XCTestCase

@end

@implementation RNNButtonOptionsTest

- (void)testInitWithDict_shouldParseBackgroundColor {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xffffff00),
    }];

    XCTAssertTrue(options.backgroundColor.hasValue);
    XCTAssertTrue([options.backgroundColor.get isEqual:UIColor.yellowColor]);
}

- (void)testInitWithDict_shouldNotRequireBackgroundColor {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"text" : @"title",
    }];

    XCTAssertFalse(options.backgroundColor.hasValue);
}

- (void)testCopy_shouldCopyBackgroundColor {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xffffff00),
    }];

    RNNButtonOptions *copied = [options copy];

    XCTAssertTrue(copied.backgroundColor.hasValue);
    XCTAssertTrue([copied.backgroundColor.get isEqual:UIColor.yellowColor]);
}

- (void)testMergeOptions_shouldMergeBackgroundColor {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xffffff00),
    }];
    RNNButtonOptions *mergeOptions = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xffff0000),
    }];

    [options mergeOptions:mergeOptions];

    XCTAssertTrue([options.backgroundColor.get isEqual:UIColor.redColor]);
}

- (void)testMergeOptions_shouldNotOverwriteBackgroundColorWhenAbsent {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xffffff00),
    }];
    RNNButtonOptions *mergeOptions = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
    }];

    [options mergeOptions:mergeOptions];

    XCTAssertTrue([options.backgroundColor.get isEqual:UIColor.yellowColor]);
}

- (void)testWithDefault_shouldFallBackToDefaultBackgroundColor {
    RNNButtonOptions *options = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
    }];
    RNNButtonOptions *defaults = [[RNNButtonOptions alloc] initWithDict:@{
        @"id" : @"buttonId",
        @"backgroundColor" : @(0xff00ff00),
    }];

    RNNButtonOptions *result = [options withDefault:defaults];

    XCTAssertTrue([result.backgroundColor.get isEqual:UIColor.greenColor]);
}

@end
