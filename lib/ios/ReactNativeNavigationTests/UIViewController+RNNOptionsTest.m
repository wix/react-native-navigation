#import <XCTest/XCTest.h>
#import "UIViewController+RNNOptions.h"
#import <OCMock/OCMock.h>

@interface UIViewController_RNNOptionsTest : XCTestCase

@property (nonatomic, retain) id uut;

@end

@implementation UIViewController_RNNOptionsTest

- (void)setUp {
    [super setUp];
	self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
}

- (void)test_setTabBarItemBadge_shouldSetValidValue {
	NSString* badgeValue = @"badge";
	[self.uut rnn_setTabBarItemBadge:badgeValue];
	XCTAssertEqual([self.uut tabBarItem].badgeValue, badgeValue);
}

- (void)test_setTabBarItemBadge_shouldResetWhenValueIsEmptyString {
	[self.uut rnn_setTabBarItemBadge:@"badge"];
	NSString* badgeValue = @"";
	[self.uut rnn_setTabBarItemBadge:badgeValue];
	XCTAssertEqual([self.uut tabBarItem].badgeValue, nil);
}

- (void)test_setTabBarItemBadge_shouldResetWhenValueIsNullObject {
	[self.uut rnn_setTabBarItemBadge:@"badge"];
	NSNull* nullBadgeValue = [NSNull new];
	[self.uut rnn_setTabBarItemBadge:nullBadgeValue];
	XCTAssertEqual([self.uut tabBarItem].badgeValue, nil);
}

- (void)testSetDrawBehindTopBarTrue_shouldSetExtendedLayoutTrue {
    [[self.uut expect] setExtendedLayoutIncludesOpaqueBars:YES];
    [self.uut rnn_setDrawBehindTopBar:YES];
    [self.uut verify];
}

- (void)testSetDrawBehindTabBarTrue_shouldSetExtendedLayoutTrue {
    [[self.uut expect] setExtendedLayoutIncludesOpaqueBars:YES];
    [self.uut rnn_setDrawBehindTabBar:YES];
    [self.uut verify];
}

- (void)testSetDrawBehindTopBarFalse_shouldNotCallExtendedLayout {
    [[self.uut reject] setExtendedLayoutIncludesOpaqueBars:NO];
    [self.uut rnn_setDrawBehindTopBar:NO];
    [self.uut verify];
}

- (void)testSetDrawBehindTabBarFalse_shouldNotCallExtendedLayout {
    [[self.uut reject] setExtendedLayoutIncludesOpaqueBars:NO];
    [self.uut rnn_setDrawBehindTabBar:NO];
    [self.uut verify];
}

- (void)testSetDrawBehindTopBarFalse_shouldSetCorrectEdgesForExtendedLayout {
	[self.uut rnn_setDrawBehindTopBar:NO];
    UIRectEdge expectedRectEdge = UIRectEdgeLeft | UIRectEdgeBottom | UIRectEdgeRight;
    XCTAssertEqual([self.uut edgesForExtendedLayout], expectedRectEdge);
}

- (void)testSetDrawBehindTapBarFalse_shouldSetCorrectEdgesForExtendedLayout {
    [self.uut rnn_setDrawBehindTabBar:NO];
	UIRectEdge expectedRectEdge = UIRectEdgeTop | UIRectEdgeLeft | UIRectEdgeRight;
	XCTAssertEqual([self.uut edgesForExtendedLayout], expectedRectEdge);
}

- (void)testSetBackgroundImageShouldNotAddViewIfImageNil {
	NSUInteger subviewsCount = [[[self.uut view] subviews] count];
	[self.uut rnn_setBackgroundImage:nil];
	XCTAssertEqual([[[self.uut view] subviews] count], subviewsCount);
}

- (void)testSetBackgroundImageShouldAddUIImageViewSubview {
	NSUInteger subviewsCount = [[[self.uut view] subviews] count];
	[self.uut rnn_setBackgroundImage:[UIImage new]];
	XCTAssertEqual([[[self.uut view] subviews] count], subviewsCount+1);
}

- (void)testSetBackgroundImageShouldAddUIImageViewSubviewWithImage {
    UIImage* image = [UIImage new];
    [self.uut rnn_setBackgroundImage:image];
    UIImageView* imageView = [[[self.uut view] subviews] firstObject];
    XCTAssertEqual(imageView.image, image);
}

@end
