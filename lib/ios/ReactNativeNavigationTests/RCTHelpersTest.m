#import <XCTest/XCTest.h>
#import <React/RCTFont.h>
#import "RCTHelpers.h"

@interface RCTHelpersTest : XCTestCase

@end

@implementation RCTHelpersTest

- (void)setUp {
    [super setUp];
}

- (void)testTextAttributesFromDictionary {
	NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
	
	dictionary[@"textColor"] = @(0xFFFF0000);
	dictionary[@"textFontFamily"] = @"HelveticaNeue";
	dictionary[@"textFontWeight"] = @"bold";
	dictionary[@"textFontSize"] = @(20);
	dictionary[@"textFontStyle"] = @"italic";
	dictionary[@"textShadowColor"] = @(0x00000000);
	dictionary[@"textShadowOffset"] = @{@"height": @(3), @"width": @(10)};
	dictionary[@"textShadowBlurRadius"] = @(0.3f);
	dictionary[@"textShowShadow"] = @(NO);
	
	NSMutableDictionary *textAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text"];
	
	XCTAssertTrue([textAttributes[NSFontAttributeName] isEqual:[RCTFont updateFont:[UIFont systemFontOfSize:[UIFont systemFontSize]] withFamily:@"HelveticaNeue" size:@(20) weight:@"bold" style:@"italic" variant:nil scaleMultiplier:1]]);
	XCTAssertTrue([textAttributes[NSForegroundColorAttributeName] isEqual:[RCTConvert UIColor:dictionary[@"textColor"]]]);
	XCTAssertNil(textAttributes[NSShadowAttributeName]);
}

- (void)testTextAttributesFromDictionary_shadow {
	NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
	
	dictionary[@"textShadowColor"] = @(0x00000000);
	dictionary[@"textShadowOffset"] = @{@"height": @(3), @"width": @(10)};
	dictionary[@"textShadowBlurRadius"] = @(0.3f);
	dictionary[@"textShowShadow"] = @(YES);
	
	NSMutableDictionary *textAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text"];
	
	NSShadow *shadow = [NSShadow new];
	shadow.shadowColor = [RCTConvert UIColor:dictionary[@"textShadowColor"]];
	shadow.shadowOffset = [RCTConvert CGSize:dictionary[@"textShadowOffset"]];
	shadow.shadowBlurRadius = [RCTConvert CGFloat:dictionary[@"textShadowBlurRadius"]];
	
	XCTAssertTrue([textAttributes[NSShadowAttributeName] isEqual:shadow]);
	
	dictionary[@"textShadowColor"] = nil;
	textAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text"];
	
	shadow = [NSShadow new];
	shadow.shadowOffset = [RCTConvert CGSize:dictionary[@"textShadowOffset"]];
	shadow.shadowBlurRadius = [RCTConvert CGFloat:dictionary[@"textShadowBlurRadius"]];
	
	XCTAssertTrue([textAttributes[NSShadowAttributeName] isEqual:shadow]);
	
	dictionary[@"textShadowColor"] = nil;
	dictionary[@"textShadowOffset"] = nil;
	textAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text"];
	
	shadow = [NSShadow new];
	shadow.shadowBlurRadius = [RCTConvert CGFloat:dictionary[@"textShadowBlurRadius"]];
	
	XCTAssertTrue([textAttributes[NSShadowAttributeName] isEqual:shadow]);
}

- (void)testTextAttributesFromDictionary_baseFont {
	NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
	
	NSMutableDictionary *textAttributes = [RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text" baseFont:[UIFont systemFontOfSize: 40.f]];
	
	XCTAssertTrue([textAttributes[NSFontAttributeName] isEqual:[RCTFont updateFont:[UIFont systemFontOfSize:40.f] withFamily:nil size:nil weight:nil style:nil variant:nil scaleMultiplier:1]]);
}

- (void)testlLabelWithFrame_textAttributes {
	NSMutableDictionary* textAttributes = [NSMutableDictionary new];
	textAttributes[NSForegroundColorAttributeName] = [UIColor redColor];
	textAttributes[NSFontAttributeName] = [UIFont fontWithName:@"HelveticaNeue" size:20];
	
	NSString *text = @"text";
	CGRect frame = CGRectMake(10, 10, 200, 100);
	
	UILabel *titleLabel = [RCTHelpers labelWithFrame:frame textAttributes:textAttributes andText:text];
	
	XCTAssertTrue(CGRectEqualToRect(titleLabel.frame, CGRectMake(10, 10, 34, 23.5)));
	XCTAssertTrue([[titleLabel.attributedText attributesAtIndex:0 longestEffectiveRange:nil inRange:NSMakeRange(0, text.length)] isEqual:textAttributes]);
}

- (void)testlLabelWithFrame_textAttributesFromDictionary {
	NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
	
	dictionary[@"textColor"] = @(0xFFFF0000);
	dictionary[@"textFontFamily"] = @"HelveticaNeue";
	dictionary[@"textFontWeight"] = @"bold";
	dictionary[@"textFontSize"] = @(20);
	
	NSString *text = @"text";
	CGRect frame = CGRectMake(10, 10, 200, 100);
	
	UILabel *titleLabel = [RCTHelpers labelWithFrame:frame textAttributesFromDictionary:dictionary baseFont:[UIFont systemFontOfSize:[UIFont systemFontSize]] andText:text];
	
	XCTAssertTrue([[titleLabel.attributedText attributesAtIndex:0 longestEffectiveRange:nil inRange:NSMakeRange(0, text.length)] isEqual:[RCTHelpers textAttributesFromDictionary:dictionary withPrefix:@"text"]]);
}

@end
