
#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNStore.h"

@interface RNNStoreTest : XCTestCase

@property (nonatomic, strong) RNNStore *store;

@end

@implementation RNNStoreTest

- (void)setUp {
	[super setUp];
	
	self.store = [RNNStore new];
}


- (void)testFindComponentForId_setAndGetsimpleComponentId {
	NSString *componentId1 = @"cntId1";
	NSString *componentId2 = @"cntId2";
	
	UIViewController *vc1 = [UIViewController new];
	UIViewController *vc2 = [UIViewController new];
	
	[self.store setComponent:vc1 componentId:componentId1];
	[self.store setComponent:vc2 componentId:componentId2];
	
	UIViewController *ans = [self.store findComponentForId:componentId1];
	
	XCTAssertEqualObjects(vc1, ans);
	XCTAssertNotEqualObjects(vc2, ans);
}

- (void)testSetComponent_setNilComponentId {
	NSString *componentId1 = nil;
	UIViewController *vc1 = [UIViewController new];
	[self.store setComponent:vc1 componentId:componentId1];
	XCTAssertNil([self.store findComponentForId:componentId1]);
	
}

- (void)testSetComponent_setDoubleComponentId {
	NSString *componentId1 = @"cntId1";
	
	UIViewController *vc1 = [UIViewController new];
	UIViewController *vc2 = [UIViewController new];
	
	[self.store setComponent:vc1 componentId:componentId1];
	
	UIViewController *ans = [self.store findComponentForId:componentId1];
	XCTAssertEqualObjects(vc1, ans);
	XCTAssertThrows([self.store setComponent:vc2 componentId:componentId1]);
}

- (void)testRemoveComponent_removeExistComponent {
	NSString *componentId1 = @"cntId1";
	UIViewController *vc1 = [UIViewController new];
	
	[self.store setComponent:vc1 componentId:componentId1];
	
	UIViewController *ans = [self.store findComponentForId:componentId1];
	XCTAssertEqualObjects(vc1, ans);
	
	[self.store removeComponent:componentId1];
	XCTAssertNil([self.store findComponentForId:componentId1]);
}

- (void)testPopWillRemoveVcFromStore {
	NSString *vcId = @"cnt_vc_2";
	
	[self setComponentAndRelease:vcId];
	
	
	XCTAssertNil([self.store findComponentForId:vcId]);
}


- (void)testRemoveComponentByInstance {
	NSString *componentId1 = @"cntId1";
	UIViewController *vc1 = [UIViewController new];
	
	[self.store setComponent:vc1 componentId:componentId1];
	[self.store removeComponentByViewControllerInstance:vc1];
	
	XCTAssertNil([self.store findComponentForId:@"cntId1"]);
}

- (void)testRemoveAllComponentsFromWindowShouldRemoveComponentsInWindow {
	UIViewController* overlayVC = [self createMockedViewControllerWithWindow:[UIWindow new]];
	
	NSString* overlayId = @"overlayId";
	[self.store setComponent:overlayVC componentId:overlayId];
	[self.store removeAllComponentsFromWindow:overlayVC.view.window];
	XCTAssertNil([self.store findComponentForId:overlayId]);
}

- (void)testRemoveAllComponentsFromWindowShouldNotRemoveComponentsInOtherWindows {
	UIViewController* overlayVC = [self createMockedViewControllerWithWindow:[UIWindow new]];
	UIViewController* componentVC = [self createMockedViewControllerWithWindow:[UIWindow new]];
	
	NSString* componentId = @"componentId";
	NSString* overlayId = @"overlayId";
	[self.store setComponent:overlayVC componentId:overlayId];
	[self.store setComponent:componentVC componentId:componentId];
	
	[self.store removeAllComponentsFromWindow:componentVC.view.window];
	XCTAssertNil([self.store findComponentForId:componentId]);
	XCTAssertNotNil([self.store findComponentForId:overlayId]);
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

- (void)testComponentKeyForInstance_UknownComponentShouldReturnNil {
	id result = [self.store componentKeyForInstance:[UIViewController new]];
	XCTAssertNil(result);
}

- (void)testCleanStore {
	[self.store clean];
	XCTAssertFalse(self.store.isReadyToReceiveCommands);
}

#pragma mark - private


-(void)setComponentAndRelease:(NSString*)vcId {
	@autoreleasepool {
		UIViewController *vc2 = [UIViewController new];
		[self.store setComponent:vc2 componentId:vcId];
		
		XCTAssertNotNil([self.store findComponentForId:vcId]);
	}
}

- (UIViewController *)createMockedViewControllerWithWindow:(UIWindow *)window {
	UIWindow* overlayWindow = [UIWindow new];
	id mockedViewController = [OCMockObject partialMockForObject:[UIViewController new]];
	id mockedView = [OCMockObject partialMockForObject:[UIView new]];
	OCMStub([mockedView window]).andReturn(overlayWindow);
	OCMStub([mockedViewController view]).andReturn(mockedView);
	return mockedViewController;
}


@end
