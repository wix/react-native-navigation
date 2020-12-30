#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/NullNumber.h>
#import <ReactNativeNavigation/RNNUIBarButtonItem.h>
#import <XCTest/XCTest.h>

@interface RNNUIBarButtonItemTest : XCTestCase

@end

@implementation RNNUIBarButtonItemTest

- (void)testInitWithStyleOptions {
    CGSize size = CGSizeMake(40, 40);
    UIColor *backgroundColor = UIColor.redColor;
    CGFloat cornerRadius = 10;

    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.width = buttonOptions.iconBackground.height =
        [Number withValue:@(40)];
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    buttonOptions.iconBackground.cornerRadius = [Number withValue:@(cornerRadius)];
    RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                           onPress:^(NSString *buttonId){
                                           }];

    UIButton *button = barButtonItem.customView;
    XCTAssertEqual(button.backgroundColor, backgroundColor);
    XCTAssertEqual(button.layer.cornerRadius, cornerRadius);
    XCTAssertTrue(CGSizeEqualToSize(button.frame.size, size));
}

- (void)testInitWithStyleOptions_shouldUseDefaultIconImageSize {
    CGSize size = CGSizeMake(20, 20);
    UIColor *backgroundColor = UIColor.redColor;
    CGFloat cornerRadius = 10;
    UIImage *image = [self imageWithSize:size];

    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.width = NullNumber.new;
    buttonOptions.iconBackground.height = NullNumber.new;
    buttonOptions.icon = [Image withValue:image];
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    buttonOptions.iconBackground.cornerRadius = [Number withValue:@(cornerRadius)];
    RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                           onPress:^(NSString *buttonId){
                                           }];

    UIButton *button = barButtonItem.customView;
    XCTAssertEqual(button.backgroundColor, backgroundColor);
    XCTAssertEqual(button.layer.cornerRadius, cornerRadius);
    XCTAssertTrue(CGSizeEqualToSize(button.frame.size, size));
}

- (UIImage *)imageWithSize:(CGSize)size {
    UIGraphicsBeginImageContextWithOptions(size, YES, 0);
    [[UIColor whiteColor] setFill];
    UIRectFill(CGRectMake(0, 0, size.width, size.height));
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

@end
