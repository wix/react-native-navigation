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
                                                                        color:fontColor
                                                                     centered:NO];
    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
    NSParagraphStyle *paragraphStyle = attributes[NSParagraphStyleAttributeName];
    XCTAssertEqual(paragraphStyle.alignment, NSTextAlignmentNatural);
}

- (void)testCreateWithFontFamily_shouldResolveFontFamilyWithFontWeight {
    NSString *familyName = @"Courier";
    NSString *fontWeight = @"bold";
    NSNumber *fontSize = @(20);
    UIColor *fontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createWithFontFamily:familyName
                                                                     fontSize:fontSize
                                                                   fontWeight:fontWeight
                                                                        color:fontColor
                                                                     centered:NO];
    UIFont *font = attributes[NSFontAttributeName];

    XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue(
        [[font.fontDescriptor objectForKey:UIFontDescriptorFaceAttribute] isEqualToString:@"Bold"]);
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testcreateFromDictionary_shouldCreateAttributes {
    NSString *familyName = @"Helvetica";
    NSNumber *fontSize = @(20);
    UIColor *fontColor = UIColor.blueColor;

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:@{}
                                                                   fontFamily:familyName
                                                                     fontSize:fontSize
                                                                   fontWeight:nil
                                                                        color:fontColor
                                                                     centered:NO];
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
                                                                   fontWeight:nil
                                                                        color:nil
                                                                     centered:NO];
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
                                                                   fontWeight:nil
                                                                        color:nil
                                                                     centered:NO];
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
                                                                   fontWeight:nil
                                                                        color:nil
                                                                     centered:NO];

    UIFont *font = attributes[NSFontAttributeName];
    XCTAssertEqual(font.pointSize, fontSize.floatValue);
    XCTAssertTrue([font.familyName isEqualToString:initialFont.familyName]);
}

- (void)testCreateWithFontFamily_shouldCenter {
    NSNumber *fontSize = @(20);
    UIFont *initialFont = [UIFont systemFontOfSize:10 weight:UIFontWeightHeavy];
    NSMutableDictionary *initialAttributes = [NSMutableDictionary new];
    initialAttributes[NSFontAttributeName] = initialFont;

    NSDictionary *attributes = [RNNFontAttributesCreator createFromDictionary:initialAttributes
                                                                   fontFamily:nil
                                                                     fontSize:fontSize
                                                                   fontWeight:nil
                                                                        color:nil
                                                                     centered:YES];

    NSParagraphStyle *paragraphStyle = attributes[NSParagraphStyleAttributeName];
    XCTAssertEqual(paragraphStyle.alignment, NSTextAlignmentCenter);
}

@end
