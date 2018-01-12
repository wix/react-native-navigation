
#import <XCTest/XCTest.h>
#import "RNNControllerFactory.h"
#import "RNNRootViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"

@interface RNNControllerFactoryTest : XCTestCase

@property (nonatomic, strong) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) RNNControllerFactory *factory;
@property (nonatomic, strong) RNNStore *store;

@end

@implementation RNNControllerFactoryTest

- (void)setUp {
	[super setUp];
	self.creator = nil;
	self.store = [RNNStore new];
	self.factory = [[RNNControllerFactory alloc] initWithRootViewCreator:self.creator store:self.store eventEmitter:nil andBridge:nil];
}

- (void)tearDown {
	[super tearDown];
}

- (void)testCreateLayout_EmptyLayout {
	XCTAssertThrows([self.factory createLayoutAndSaveToStore:@{}]);
}


- (void)testCreateLayout_ComponentLayout {
	
	id ans = [self.factory createLayoutAndSaveToStore:
			  @{@"id": @"cntId",
				@"type": @"Component",
				@"data": @{},
				@"children": @[]}];
	XCTAssertTrue([ans isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_ComponentStackLayout {
	id ans = [self.factory createLayoutAndSaveToStore:
			  @{@"id": @"cntId",
				@"type": @"Stack",
				@"data": @{},
				@"children": @[]}];
	XCTAssertTrue([ans isMemberOfClass:[RNNNavigationController class]]);
}

- (void)testCreateLayout_ComponentStackLayoutRecursive {
	RNNNavigationController* ans = (RNNNavigationController*) [self.factory createLayoutAndSaveToStore:
															 @{@"id": @"cntId",
															   @"type": @"Stack",
															   @"data": @{},
															   @"children": @[
																	   @{@"id": @"cntId_2",
																		 @"type": @"Component",
																		 @"data": @{},
																		 @"children": @[]}]}];
	
	XCTAssertTrue([ans isMemberOfClass:[RNNNavigationController class]]);
	XCTAssertTrue(ans.childViewControllers.count == 1);
	XCTAssertTrue([ans.childViewControllers[0] isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_BottomTabsLayout {
	RNNTabBarController* tabBar = (RNNTabBarController*) [self.factory createLayoutAndSaveToStore:
														@{
														  @"id": @"cntId",
														  @"type": @"BottomTabs",
														  @"data": @{},
														  @"children": @[
																  @{@"id": @"cntId_2",
																	@"type": @"Stack",
																	@"data": @{},
																	@"children": @[
																			@{@"id": @"cntId_3",
																			  @"type": @"Component",
																			  @"data": @{},
																			  @"children": @[]}]}]}];
	
	XCTAssertTrue([tabBar isMemberOfClass:[RNNTabBarController class]]);
	XCTAssertTrue(tabBar.childViewControllers.count == 1);
	XCTAssertTrue([tabBar.childViewControllers[0] isMemberOfClass:[RNNNavigationController class]]);
	
	UINavigationController *navController = tabBar.childViewControllers[0];
	XCTAssertTrue(navController.childViewControllers.count == 1);
	XCTAssertTrue([navController.childViewControllers[0] isMemberOfClass:[RNNRootViewController class]]);
	
	
}


- (void)testCreateLayout_ComponentSideMenuLayoutCenterLeftRight {
	RNNSideMenuController *ans = (RNNSideMenuController*) [self.factory createLayoutAndSaveToStore:
														   @{@"id": @"cntId",
															 @"type": @"SideMenuRoot",
															 @"data": @{},
															 @"children": @[
																	 @{@"id": @"cntI_2",
																	   @"type": @"SideMenuCenter",
																	   @"data": @{},
																	   @"children": @[
																			   @{@"id": @"cntId_3",
																				 @"type": @"Component",
																				 @"data": @{},
																				 @"children": @[]}]},
																	 @{@"id": @"cntI_4",
																	   @"type": @"SideMenuLeft",
																	   @"data": @{},
																	   @"children": @[
																			   @{@"id": @"cntId_5",
																				 @"type": @"Component",
																				 @"data": @{},
																				 @"children": @[]}]},
																	 @{@"id": @"cntI_6",
																	   @"type": @"SideMenuRight",
																	   @"data": @{},
																	   @"children": @[
																			   @{@"id": @"cntId_7",
																				 @"type": @"Component",
																				 @"data": @{},
																				 @"children": @[]}]}]}];
	XCTAssertTrue([ans isMemberOfClass:[RNNSideMenuController class]]);
	XCTAssertTrue([ans isKindOfClass:[UIViewController class]]);
	XCTAssertTrue([ans.center isMemberOfClass:[RNNSideMenuChildVC class]]);
	RNNSideMenuChildVC *center = (RNNSideMenuChildVC*)ans.center;
	XCTAssertTrue(center.type == RNNSideMenuChildTypeCenter);
	XCTAssertTrue([center.child isMemberOfClass:[RNNRootViewController class]]);
	
	RNNSideMenuChildVC *left = (RNNSideMenuChildVC*)ans.left;
	XCTAssertTrue(left.type == RNNSideMenuChildTypeLeft);
	XCTAssertTrue([left.child isMemberOfClass:[RNNRootViewController class]]);
	
	RNNSideMenuChildVC *right = (RNNSideMenuChildVC*)ans.right;
	XCTAssertTrue(right.type == RNNSideMenuChildTypeRight);
	XCTAssertTrue([right.child isMemberOfClass:[RNNRootViewController class]]);
}




- (void)testCreateLayout_addComponentToStore {
	NSString *componentId = @"cntId";
	UIViewController *ans = [self.factory createLayoutAndSaveToStore:
							 @{@"id": componentId,
							   @"type": @"Component",
							   @"data": @{},
							   @"children": @[]}];
	
	UIViewController *storeAns = [self.store findComponentForId:componentId];
	XCTAssertEqualObjects(ans, storeAns);
}



@end
