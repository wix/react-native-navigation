#import <XCTest/XCTest.h>
#import "RNNFontAttributesCreator.h"

@interface RNNFontAttributesCreatorTest : XCTestCase

@end

@implementation RNNFontAttributesCreatorTest

- (void)testCreateWithFontFamily_shouldCreateAttributes {
	NSString* familyName = @"Helvetica";
	NSNumber* fontSize = @(20);
	UIColor* fontColor = UIColor.blueColor;
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithFontFamily:familyName fontSize:fontSize fontWeight:nil color:fontColor];
	UIFont* font = attributes[NSFontAttributeName];
	XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
	XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithFontFamily_shouldIgnoreFontFamilyWhenFontWeightIsNotNil {
	NSString* familyName = @"Helvetica";
	NSString* fontWeight = @"bold";
	NSNumber* fontSize = @(20);
	UIColor* fontColor = UIColor.blueColor;
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithFontFamily:familyName fontSize:fontSize fontWeight:fontWeight color:fontColor];
	UIFont* font = attributes[NSFontAttributeName];
	XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertFalse([familyName isEqualToString:font.familyName]);
	XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithFontFamilyWithDefault_shouldCreateDefaultAttributes {
	NSString* familyName = @"Helvetica";
	NSNumber* defaultFontSize = @(20);
	UIColor* defaultFontColor = UIColor.blueColor;
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithFontFamily:familyName fontSize:nil defaultFontSize:defaultFontSize fontWeight:nil color:nil defaultColor:defaultFontColor];
	UIFont* font = attributes[NSFontAttributeName];
	XCTAssertEqual(attributes[NSForegroundColorAttributeName], defaultFontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
	XCTAssertEqual(font.pointSize, defaultFontSize.floatValue);
}

- (void)testCreateWithDictionary_shouldCreateAttributes {
	NSString* familyName = @"Helvetica";
	NSNumber* fontSize = @(20);
	UIColor* fontColor = UIColor.blueColor;
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithDictionary:@{} fontFamily:familyName fontSize:fontSize defaultFontSize:nil fontWeight:nil color:fontColor defaultColor:nil];
	UIFont* font = attributes[NSFontAttributeName];
	XCTAssertEqual(attributes[NSForegroundColorAttributeName], fontColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
	XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithDictionary_shouldMergeWithDictionary {
	NSString* familyName = @"Helvetica";
	NSNumber* fontSize = @(20);
	NSDictionary* dictionary = @{NSForegroundColorAttributeName: UIColor.redColor};
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithDictionary:dictionary fontFamily:familyName fontSize:fontSize defaultFontSize:nil fontWeight:nil color:nil defaultColor:nil];
	UIFont* font = attributes[NSFontAttributeName];
	XCTAssertEqual(attributes[NSForegroundColorAttributeName], UIColor.redColor);
    XCTAssertTrue([familyName isEqualToString:font.familyName]);
	XCTAssertEqual(font.pointSize, fontSize.floatValue);
}

- (void)testCreateWithFontFamily_shouldCreateSystemFontWhenOnlySizeAvailable {
	NSNumber* fontSize = @(20);
	
	NSDictionary* attributes = [RNNFontAttributesCreator createWithFontFamily:nil fontSize:fontSize fontWeight:nil color:nil];
	UIFont* font = attributes[NSFontAttributeName];
	NSString* systemFontFamilyName = [[UIFont systemFontOfSize:20] familyName];
	XCTAssertEqual(font.pointSize, fontSize.floatValue);
	XCTAssertTrue([font.familyName isEqualToString:systemFontFamilyName]);
}


@end
