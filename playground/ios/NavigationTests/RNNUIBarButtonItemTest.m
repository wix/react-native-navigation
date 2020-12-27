#import <OCMock/OCMock.h>
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

@end
