
#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNExternalComponentStore.h"

@interface RNNExternalComponentStoreTest : XCTestCase

@property (nonatomic, strong) RNNExternalComponentStore *store;

@end

@implementation RNNExternalComponentStoreTest

- (void)setUp {
	[super setUp];
	
	self.store = [RNNExternalComponentStore new];
}

- (void)testGetExternalComponentShouldRetrunSavedComponent {
	UIViewController* testVC = [UIViewController new];
	RNNLayoutInfo* layoutInfo = [[RNNLayoutInfo alloc] init];
	layoutInfo.name = @"extId1";
	[self.store registerExternalComponent:layoutInfo.name callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
		return testVC;
	}];
	
	UIViewController* savedComponent = [self.store getExternalComponent:layoutInfo bridge:nil];
	XCTAssertEqual(testVC, savedComponent);
}


@end
