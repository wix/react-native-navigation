#import <ReactNativeNavigation/RNNSearchBarOptions.h>
#import <XCTest/XCTest.h>

@interface RNNSearchBarOptionsTest : XCTestCase

@end

@implementation RNNSearchBarOptionsTest

- (void)testInitWithDict {
    RNNSearchBarOptions *options = [[RNNSearchBarOptions alloc] initWithDict:@{
        @"visible" : @(1),
        @"focus" : @(1),
        @"hideOnScroll" : @(1),
        @"obscuresBackgroundDuringPresentation" : @(1),
        @"backgroundColor" : @(0xff0000ff),
        @"tintColor" : @(0xff0000ff),
        @"placeholder" : @"placeholder",
        @"cancelText" : @"cancelText",
    }];

    XCTAssertTrue(options.visible.get);
    XCTAssertTrue(options.focus.get);
    XCTAssertTrue(options.hideOnScroll.get);
    XCTAssertTrue(options.obscuresBackgroundDuringPresentation.get);
    XCTAssertTrue([options.backgroundColor.get isEqual:UIColor.blueColor]);
    XCTAssertTrue([options.tintColor.get isEqual:UIColor.blueColor]);
    XCTAssertTrue([options.placeholder.get isEqualToString:@"placeholder"]);
    XCTAssertTrue([options.cancelText.get isEqualToString:@"cancelText"]);
}

- (void)testMergeOptions {
    RNNSearchBarOptions *options = [[RNNSearchBarOptions alloc] initWithDict:@{
        @"visible" : @(1),
        @"focus" : @(1),
        @"hideOnScroll" : @(1),
        @"obscuresBackgroundDuringPresentation" : @(1),
        @"backgroundColor" : @(0xff0000ff),
        @"tintColor" : @(0xff0000ff),
        @"placeholder" : @"placeholder",
        @"cancelText" : @"cancelText",
    }];
    RNNSearchBarOptions *mergeOptions = [[RNNSearchBarOptions alloc] initWithDict:@{
        @"visible" : @(0),
        @"focus" : @(0),
        @"hideOnScroll" : @(0),
        @"obscuresBackgroundDuringPresentation" : @(0),
        @"backgroundColor" : @(0xff00ff00),
        @"tintColor" : @(0xff00ff00),
        @"placeholder" : @"mergedPlaceholder",
        @"cancelText" : @"mergedCancelText",
    }];

    [options mergeOptions:mergeOptions];

    XCTAssertFalse(options.visible.get);
    XCTAssertFalse(options.focus.get);
    XCTAssertFalse(options.hideOnScroll.get);
    XCTAssertFalse(options.obscuresBackgroundDuringPresentation.get);
    XCTAssertTrue([options.backgroundColor.get isEqual:UIColor.greenColor]);
    XCTAssertTrue([options.tintColor.get isEqual:UIColor.greenColor]);
    XCTAssertTrue([options.placeholder.get isEqualToString:@"mergedPlaceholder"]);
    XCTAssertTrue([options.cancelText.get isEqualToString:@"mergedCancelText"]);
}

@end
