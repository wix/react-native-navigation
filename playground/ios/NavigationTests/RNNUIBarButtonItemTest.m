#import <ReactNativeNavigation/RNNUIBarButtonItem.h>
#import <XCTest/XCTest.h>

@interface RNNUIBarButtonItemTest : XCTestCase

@end

@implementation RNNUIBarButtonItemTest

- (void)testInitWithStyleOptions {
    CGSize size = CGSizeMake(40, 40);
    UIColor *backgroundColor = UIColor.redColor;
    CGFloat cornerRadius = 10;
    UIEdgeInsets insets = UIEdgeInsetsMake(10, 10, 10, 10);
    RNNUIBarButtonItem *barButtonItem = [[RNNUIBarButtonItem alloc] init:@"buttonId"
                                                                    icon:UIImage.new
                                                                    size:size
                                                         backgroundColor:backgroundColor
                                                            cornerRadius:cornerRadius
                                                                  insets:insets];

    UIButton *button = barButtonItem.customView;
    XCTAssertEqual(button.backgroundColor, backgroundColor);
    XCTAssertEqual(button.layer.cornerRadius, cornerRadius);
    XCTAssertTrue(CGSizeEqualToSize(button.frame.size, size));
    XCTAssertTrue(CGRectEqualToRect(button.imageView.frame, CGRectMake(10, 10, 20, 20)));
}

@end
