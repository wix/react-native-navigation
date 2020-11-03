#import "RNNFontAttributesCreator.h"
#import <XCTest/XCTest.h>

@interface RNNFontAttributesCreatorTest : XCTestCase

@end

@implementation RNNFontAttributesCreatorTest

- (void)testCreateWithFontFamily_shouldCreateAttributes {
    NSString *familyName = @"Helvetica";
    NSNumber *fontSize = @(20);
    UIColor *fontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createWithFontFamily:familyName
                                                                     fontSize:fontSize
                                                                   fontWeight:nil
                                                                        color:fontColor];
    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithFontFamily_shouldResolveFontFamilyWithFontWeight {
    NSString *familyName = @"Courier";
    NSString *fontWeight = @"bold";
    NSNumber *fontSize = @(20);
    UIColor *fontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createWithFontFamily:familyName
                                                                     fontSize:fontSize
                                                                   fontWeight:fontWeight
                                                                        color:fontColor];
    UIFont *font = attributes[NSFontAttributeName];

    XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue(
        [[font.fontDescriptor objectForKey:UIFontDescriptorFaceAttribute] isEqualToString:@"Bold"]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithFontFamilyWithDefault_shouldCreateDefaultAttributes {
    NSString *familyName = @"Helvetica";
    NSNumber *defaultFontSize = @(20);
    UIColor *defaultFontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createWithFontFamily:familyName
                                                                     fontSize:nil
                                                              defaultFontSize:defaultFontSize
                                                                   fontWeight:nil
                                                                        color:nil
                                                                 defaultColor:defaultFontColor];
    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(attributes[NSForegroundColorAttributeName], defaultFontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
    XCTAssertEqual(font.pointSize, defaultFontSize.floatValue);
}

- (void)testcreateFromDictionary_shouldCreateAttributes {
    NSString *familyName = @"Helvetica";
    NSNumber *fontSize = @(20);
    UIColor *fontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:@{}
                                                                   fontFamily:familyName
                                                                     fontSize:fontSize
                                                              defaultFontSize:nil
                                                                   fontWeight:nil
                                                                        color:fontColor
                                                                 defaultColor:nil];
    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateFromDictionary_shouldMergeWithDictionary {
    NSString *familyName = @"Helvetica";
    NSNumber *fontSize = @(20);
    NSDictionary *dictionary = @{NSForegroundColorAttributeName : UIColor.redColor};

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:dictionary
                                                                   fontFamily:familyName
                                                                     fontSize:fontSize
                                                              defaultFontSize:nil
                                                                   fontWeight:nil
                                                                        color:nil
                                                                 defaultColor:nil];
    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateFromDictionary_shouldOverrideColor {
    NSString *familyName = @"Helvetica";
    NSNumber *fontSize = @(20);
    NSDictionary *dictionary = @{NSForegroundColorAttributeName : UIColor.redColor};

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:dictionary
                                                                   fontFamily:familyName
                                                                     fontSize:fontSize
                                                              defaultFontSize:nil
                                                                   fontWeight:nil
                                                                        color:nil
                                                                 defaultColor:nil];
    XCTAssertEqual(attributes[NSForegroundColorAttributeName], nil);
}

- (void)testCreateWithFontFamily_shouldNotChangeFontFamilyWhenOnlySizeAvailable {
    NSNumber *fontSize = @(20);
    UIFont *initialFont = [UIFont systemFontOfSize:10 weight:UIFontWeightHeavy];
    NSMutableDictionary *initialAttributes = [NSMutableDictionary new];
    initialAttributes[NSFontAttributeName] = initialFont;

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:initialAttributes
                                                                   fontFamily:nil
                                                                     fontSize:fontSize
                                                              defaultFontSize:nil
                                                                   fontWeight:nil
                                                                        color:nil
                                                                 defaultColor:nil];

    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
    XCTAssertTrue([font.familyName isEqualToString:initialFont.familyName]);
}

@end
