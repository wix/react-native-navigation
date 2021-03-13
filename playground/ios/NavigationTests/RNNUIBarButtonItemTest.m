#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/NullNumber.h>
#import <ReactNativeNavigation/RNNUIBarButtonItem.h>
#import <XCTest/XCTest.h>

@interface RNNUIBarButtonItemTest : XCTestCase
@property(nonatomic, strong) RNNIconCreator *iconCreator;
@property(nonatomic, strong) id mockedIconDrawer;
@end

@implementation RNNUIBarButtonItemTest

- (void)setUp {
    _mockedIconDrawer = [OCMockObject partialMockForObject:RNNIconDrawer.new];
    _iconCreator = [[RNNIconCreator alloc] initWithIconDrawer:_mockedIconDrawer];
}

- (void)testInitCustomIcon_shouldDraw {
    CGSize size = CGSizeMake(40, 40);
    UIColor *backgroundColor = UIColor.redColor;
    UIColor *tineColor = UIColor.blueColor;
    CGFloat cornerRadius = 10;
    UIImage *image = UIImage.new;
    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.icon = [Image withValue:image];
    buttonOptions.color = [Color withValue:tineColor];
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.width = buttonOptions.iconBackground.height =
        [Number withValue:@(40)];
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    buttonOptions.iconBackground.cornerRadius = [Number withValue:@(cornerRadius)];
    [[_mockedIconDrawer expect] draw:image
                          imageColor:tineColor
                     backgroundColor:backgroundColor
                                size:size
                        cornerRadius:cornerRadius];
    __unused RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                       iconCreator:_iconCreator
                                           onPress:^(NSString *buttonId){
                                           }];
    [_mockedIconDrawer verify];
}

- (void)testInitCustomIcon_shouldFallbackToEnabledColors {
    UIColor *backgroundColor = UIColor.redColor;
    UIColor *tineColor = UIColor.blueColor;
    UIImage *image = UIImage.new;
    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.enabled = [Bool withValue:NO];
    buttonOptions.icon = [Image withValue:image];
    buttonOptions.color = [Color withValue:tineColor];
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    [[_mockedIconDrawer expect] draw:image
                          imageColor:tineColor
                     backgroundColor:backgroundColor
                                size:image.size
                        cornerRadius:0];
    __unused RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                       iconCreator:_iconCreator
                                           onPress:^(NSString *buttonId){
                                           }];
    [_mockedIconDrawer verify];
}

- (void)testInitCustomIcon_shouldUseDisabledColors {
    UIColor *backgroundColor = UIColor.redColor;
    UIColor *disabledBackgroundColor = UIColor.brownColor;
    UIColor *tineColor = UIColor.blueColor;
    UIColor *disabledTintColor = UIColor.yellowColor;
    UIImage *image = UIImage.new;
    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.enabled = [Bool withValue:NO];
    buttonOptions.icon = [Image withValue:image];
    buttonOptions.color = [Color withValue:tineColor];
    buttonOptions.disabledColor = [Color withValue:disabledTintColor];
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    buttonOptions.iconBackground.disabledColor = [Color withValue:disabledBackgroundColor];

    [[_mockedIconDrawer expect] draw:image
                          imageColor:disabledTintColor
                     backgroundColor:disabledBackgroundColor
                                size:image.size
                        cornerRadius:0];
    __unused RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                       iconCreator:_iconCreator
                                           onPress:^(NSString *buttonId){
                                           }];
    [_mockedIconDrawer verify];
}

- (void)testInitCustomIcon_shouldReceiveSize {
    CGSize size = CGSizeMake(40, 40);
    UIColor *backgroundColor = UIColor.redColor;
    UIImage *image = UIImage.new;
    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.icon = [Image withValue:image];
    buttonOptions.iconBackground = RNNIconBackgroundOptions.new;
    buttonOptions.iconBackground.width = buttonOptions.iconBackground.height =
        [Number withValue:@(40)];
    buttonOptions.iconBackground.color = [Color withValue:backgroundColor];
    RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initCustomIcon:buttonOptions
                                       iconCreator:_iconCreator
                                           onPress:^(NSString *buttonId){
                                           }];

    UIButton *button = barButtonItem.customView;
    XCTAssertTrue(CGSizeEqualToSize(button.frame.size, size));
}

- (void)testInitCustomIcon_shouldUseDefaultIconImageSize {
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
                                       iconCreator:_iconCreator
                                           onPress:^(NSString *buttonId){
                                           }];

    UIButton *button = barButtonItem.customView;
    XCTAssertTrue(CGSizeEqualToSize(button.frame.size, size));
}

- (void)testInitWithIcon_ShouldApplyTintColor {
    UIColor *buttonColor = UIColor.redColor;

    RNNButtonOptions *buttonOptions = RNNButtonOptions.new;
    buttonOptions.icon = [Image withValue:UIImage.new];
    buttonOptions.color = [Color withColor:buttonColor];
    RNNUIBarButtonItem *barButtonItem =
        [[RNNUIBarButtonItem alloc] initWithIcon:buttonOptions
                                         onPress:^(NSString *buttonId){
                                         }];

    XCTAssertEqual(barButtonItem.tintColor, buttonColor);
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
