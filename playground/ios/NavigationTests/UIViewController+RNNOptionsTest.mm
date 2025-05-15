#import "RNNBottomTabOptions.h"
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface UIViewController_RNNOptionsTest : XCTestCase

@property(nonatomic, retain) id uut;

@end

@implementation UIViewController_RNNOptionsTest

- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
}

- (void)test_setTabBarItemBadge_shouldSetValidValue {
    [self.uut setTabBarItemBadge:@"badge"];
    XCTAssertEqual([self.uut tabBarItem].badgeValue, @"badge");
}

- (void)test_setTabBarItemBadge_shouldResetWhenValueIsEmptyString {
    [self.uut setTabBarItemBadge:@"badge"];

    [self.uut setTabBarItemBadge:@""];
    XCTAssertEqual([self.uut tabBarItem].badgeValue, nil);
}

- (void)test_setTabBarItemBadge_shouldResetWhenValueIsNullObject {
    [self.uut setTabBarItemBadge:@"badge"];
    [self.uut setTabBarItemBadge:(id)[NSNull new]];
    XCTAssertEqual([self.uut tabBarItem].badgeValue, nil);
}

//- (void)testSetDrawBehindTopBarFalse_shouldNotCallExtendedLayout {
//    [[self.uut reject] setExtendedLayoutIncludesOpaqueBars:NO];
//    [self.uut setDrawBehindTopBar:NO];
//    [self.uut verify];
//}
//
//- (void)testSetDrawBehindTabBarFalse_shouldNotCallExtendedLayout {
//    [[self.uut reject] setExtendedLayoutIncludesOpaqueBars:NO];
//    [self.uut setDrawBehindTabBar:NO];
//    [self.uut verify];
//}
//
//- (void)testSetDrawBehindTopBarFalse_shouldSetCorrectEdgesForExtendedLayout {
//    [self.uut setDrawBehindTopBar:NO];
//    UIRectEdge expectedRectEdge = UIRectEdgeLeft | UIRectEdgeBottom | UIRectEdgeRight;
//    XCTAssertEqual([self.uut edgesForExtendedLayout], expectedRectEdge);
//}
//
//- (void)testSetDrawBehindTapBarFalse_shouldSetCorrectEdgesForExtendedLayout {
//    [self.uut setDrawBehindTabBar:NO];
//    UIRectEdge expectedRectEdge = UIRectEdgeTop | UIRectEdgeLeft | UIRectEdgeRight;
//    XCTAssertEqual([self.uut edgesForExtendedLayout], expectedRectEdge);
//}

- (void)testSetBackgroundImageShouldNotAddViewIfImageNil {
    NSUInteger subviewsCount = [[[self.uut view] subviews] count];
    [self.uut setBackgroundImage:nil];
    XCTAssertEqual([[[self.uut view] subviews] count], subviewsCount);
}

- (void)testSetBackgroundImageShouldAddUIImageViewSubview {
    NSUInteger subviewsCount = [[[self.uut view] subviews] count];
    [self.uut setBackgroundImage:[UIImage new]];
    XCTAssertEqual([[[self.uut view] subviews] count], subviewsCount + 1);
}

- (void)testSetBackgroundImageShouldAddUIImageViewSubviewWithImage {
    UIImage *image = [UIImage new];
    [self.uut setBackgroundImage:image];
    UIImageView *imageView = [[[self.uut view] subviews] firstObject];
    XCTAssertEqual(imageView.image, image);
}

@end
