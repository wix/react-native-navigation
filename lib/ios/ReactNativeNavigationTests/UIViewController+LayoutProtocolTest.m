#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "UIViewController+LayoutProtocol.h"

@interface UIViewController_LayoutProtocolTest : XCTestCase

@property (nonatomic, retain) UIViewController* uut;

@end

@implementation UIViewController_LayoutProtocolTest

- (void)setUp {
	[super setUp];
	self.uut = [OCMockObject partialMockForObject:[UIViewController new]];
    self.uut.layoutInfo = [[RNNLayoutInfo alloc] init];
    self.uut.layoutInfo.componentId = @"componentId";
}

- (void)testSetStoreShouldSaveComponent {
    RNNStore* store = [[RNNStore alloc] init];
    [self.uut setStore:store];
    XCTAssertNotNil([store findComponentForId:self.uut.layoutInfo.componentId]);
}

- (void)testDeallocShouldRemoveComponentFromStore {
    RNNStore* store = [[RNNStore alloc] init];
    [self.uut setStore:store];
    XCTAssertNotNil([store findComponentForId:self.uut.layoutInfo.componentId]);
    self.uut = nil;
    XCTAssertNil([store findComponentForId:self.uut.layoutInfo.componentId]);
}


@end
