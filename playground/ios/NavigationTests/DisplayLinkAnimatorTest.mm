#import "DisplayLinkAnimator.h"
#import <XCTest/XCTest.h>

@interface DisplayLinkAnimator (Testing)
- (void)updateAnimators:(NSTimeInterval)elapsed;
@end

@interface RNNTestDisplayLinkDelegate : NSObject <DisplayLinkAnimatorDelegate>
@property(nonatomic) NSTimeInterval maxDuration;
@property(nonatomic) NSUInteger updates;
@property(nonatomic) NSUInteger endings;
@end

@implementation RNNTestDisplayLinkDelegate
- (void)updateAnimations:(NSTimeInterval)elapsed { self.updates++; }
- (void)end { self.endings++; }
@end

@interface DisplayLinkAnimatorTest : XCTestCase
@end

@implementation DisplayLinkAnimatorTest
- (void)testFinishingAnimatorDoesNotSkipTheNextAnimator {
    RNNTestDisplayLinkDelegate *first = [RNNTestDisplayLinkDelegate new];
    first.maxDuration = 1;
    RNNTestDisplayLinkDelegate *second = [RNNTestDisplayLinkDelegate new];
    second.maxDuration = 2;
    DisplayLinkAnimator *animator = [[DisplayLinkAnimator alloc]
        initWithDisplayLinkAnimators:@[ first, second ] duration:2];
    [animator updateAnimators:1];
    XCTAssertEqual(first.endings, 1u);
    XCTAssertEqual(second.updates, 1u);
    [animator updateAnimators:2];
    XCTAssertEqual(first.endings, 1u);
    XCTAssertEqual(second.endings, 1u);
}

- (void)testAdjacentCompletedAnimatorsEndInTheSameTick {
    RNNTestDisplayLinkDelegate *first = [RNNTestDisplayLinkDelegate new];
    RNNTestDisplayLinkDelegate *second = [RNNTestDisplayLinkDelegate new];
    first.maxDuration = second.maxDuration = 1;
    DisplayLinkAnimator *animator = [[DisplayLinkAnimator alloc]
        initWithDisplayLinkAnimators:@[ first, second ] duration:1];
    [animator updateAnimators:1];
    [animator updateAnimators:2];
    XCTAssertEqual(first.endings, 1u);
    XCTAssertEqual(second.endings, 1u);
}
@end
