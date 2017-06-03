
#import <XCTest/XCTest.h>
#import "RNNStore.h"

@interface RNNStoreTest : XCTestCase

@property (nonatomic, strong) RNNStore *store;

@end

@implementation RNNStoreTest

- (void)setUp {
	[super setUp];
	
	self.store = [RNNStore new];
}


- (void)testFindContainerForId_setAndGetsimpleContainerId {
	NSString *containerId1 = @"cntId1";
	NSString *containerId2 = @"cntId2";
	
	UIViewController *vc1 = [UIViewController new];
	UIViewController *vc2 = [UIViewController new];
	
	[self.store setContainer:vc1 containerId:containerId1];
	[self.store setContainer:vc2 containerId:containerId2];
	
	UIViewController *ans = [self.store findContainerForId:containerId1];
	
	XCTAssertEqualObjects(vc1, ans);
	XCTAssertNotEqualObjects(vc2, ans);
}

- (void)testSetContainer_setNilContainerId {
	NSString *containerId1 = nil;
	UIViewController *vc1 = [UIViewController new];
	[self.store setContainer:vc1 containerId:containerId1];
	XCTAssertNil([self.store findContainerForId:containerId1]);
	
}

- (void)testSetContainer_setDoubleContainerId {
	NSString *containerId1 = @"cntId1";
	
	UIViewController *vc1 = [UIViewController new];
	UIViewController *vc2 = [UIViewController new];
	
	[self.store setContainer:vc1 containerId:containerId1];
	
	UIViewController *ans = [self.store findContainerForId:containerId1];
	XCTAssertEqualObjects(vc1, ans);
	XCTAssertThrows([self.store setContainer:vc2 containerId:containerId1]);
}

- (void)testRemoveContainer_removeExistContainer {
	NSString *containerId1 = @"cntId1";
	UIViewController *vc1 = [UIViewController new];
	
	[self.store setContainer:vc1 containerId:containerId1];
	
	UIViewController *ans = [self.store findContainerForId:containerId1];
	XCTAssertEqualObjects(vc1, ans);
	
	[self.store removeContainer:containerId1];
	XCTAssertNil([self.store findContainerForId:containerId1]);
}

-(void)testPopWillRemoveVcFromStore {
	NSString *vcId = @"cnt_vc_2";
	
	[self setContainerAndRelease:vcId];
	
	
	XCTAssertNil([self.store findContainerForId:vcId]);
}


-(void)testRemoveContainerByInstance {
	NSString *containerId1 = @"cntId1";
	UIViewController *vc1 = [UIViewController new];
	
	[self.store setContainer:vc1 containerId:containerId1];
	[self.store removeContainerByViewControllerInstance:vc1];
	
	XCTAssertNil([self.store findContainerForId:@"cntId1"]);
}


#pragma mark - private


-(void)setContainerAndRelease:(NSString*)vcId {
	@autoreleasepool {
		UIViewController *vc2 = [UIViewController new];
		[self.store setContainer:vc2 containerId:vcId];
		
		XCTAssertNotNil([self.store findContainerForId:vcId]);
	}
}



@end
