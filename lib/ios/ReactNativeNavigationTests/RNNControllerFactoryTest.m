
#import <XCTest/XCTest.h>
#import "RNNControllerFactory.h"
#import "RNNRootViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNTabBarController.h"
#import "RNNNavigationController.h"

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
	self.factory = [[RNNControllerFactory alloc] initWithRootViewCreator:self.creator store:self.store eventEmitter:nil];
}

- (void)tearDown {
	[super tearDown];
}

- (void)testCreateLayout_EmptyLayout {
	XCTAssertThrows([self.factory createLayoutAndSaveToStore:@{}]);
}


- (void)testCreateLayout_ContainerLayout {
	
	id ans = [self.factory createLayoutAndSaveToStore:
			  @{@"id": @"cntId",
				@"type": @"Container",
				@"data": @{},
				@"children": @[]}];
	XCTAssertTrue([ans isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_ContainerStackLayout {
	id ans = [self.factory createLayoutAndSaveToStore:
			  @{@"id": @"cntId",
				@"type": @"ContainerStack",
				@"data": @{},
				@"children": @[]}];
	XCTAssertTrue([ans isMemberOfClass:[RNNNavigationController class]]);
}

- (void)testCreateLayout_ContainerStackLayoutRecursive {
	UINavigationController* ans = (UINavigationController*) [self.factory createLayoutAndSaveToStore:
															 @{@"id": @"cntId",
															   @"type": @"ContainerStack",
															   @"data": @{},
															   @"children": @[
																	   @{@"id": @"cntId_2",
																		 @"type": @"Container",
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
																	@"type": @"ContainerStack",
																	@"data": @{},
																	@"children": @[
																			@{@"id": @"cntId_3",
																			  @"type": @"Container",
																			  @"data": @{},
																			  @"children": @[]}]}]}];
	
	XCTAssertTrue([tabBar isMemberOfClass:[RNNTabBarController class]]);
	XCTAssertTrue(tabBar.childViewControllers.count == 1);
	XCTAssertTrue([tabBar.childViewControllers[0] isMemberOfClass:[RNNNavigationController class]]);
	
	UINavigationController *navController = tabBar.childViewControllers[0];
	XCTAssertTrue(navController.childViewControllers.count == 1);
	XCTAssertTrue([navController.childViewControllers[0] isMemberOfClass:[RNNRootViewController class]]);
	
	
}


- (void)testCreateLayout_ContainerSideMenuLayoutCenterLeftRight {
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
																				 @"type": @"Container",
																				 @"data": @{},
																				 @"children": @[]}]},
																	 @{@"id": @"cntI_4",
																	   @"type": @"SideMenuLeft",
																	   @"data": @{},
																	   @"children": @[
																			   @{@"id": @"cntId_5",
																				 @"type": @"Container",
																				 @"data": @{},
																				 @"children": @[]}]},
																	 @{@"id": @"cntI_6",
																	   @"type": @"SideMenuRight",
																	   @"data": @{},
																	   @"children": @[
																			   @{@"id": @"cntId_7",
																				 @"type": @"Container",
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

- (void)testCreateLayout_ContainerSideMenuLayoutCenterTabBar {
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
																				 @"type": @"BottomTabs",
																				 @"data": @{},
																				 @"children": @[
																						 @{@"id": @"cntId_4",
																						   @"type": @"ContainerStack",
																						   @"data": @{},
																						   @"children": @[
																								   @{@"id": @"cntId_2",
																									 @"type": @"Container",
																									 @"data": @{},
																									 @"children": @[]}]}]}]}]}
														   ];
	
	XCTAssertTrue([ans isMemberOfClass:[RNNSideMenuController class]]);
	XCTAssertTrue([ans isKindOfClass:[UIViewController class]]);
	XCTAssertTrue([ans.center isMemberOfClass:[RNNSideMenuChildVC class]]);
	
	RNNSideMenuChildVC *center = (RNNSideMenuChildVC*)ans.center;
	XCTAssertTrue(center.type == RNNSideMenuChildTypeCenter);
	XCTAssertTrue([center.child isMemberOfClass:[RNNTabBarController class]]);
	
	UITabBarController *tabbBar = (UITabBarController*)center.child;
	XCTAssertTrue(tabbBar.viewControllers.count == 1);
	
	UINavigationController *navController = (UINavigationController*)(tabbBar.viewControllers[0]);
	XCTAssertTrue([navController isMemberOfClass:[RNNNavigationController class]]);
	XCTAssertTrue(navController.viewControllers.count == 1);
	
	RNNRootViewController *rootViewController = (RNNRootViewController*)navController.viewControllers[0];
	XCTAssertTrue([rootViewController isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testNavigationOptions_default {
	UIViewController *ans = [self.factory createLayoutAndSaveToStore: @{@"id": @"cntId_2",
																		@"type": @"Container",
																		@"data": @{@"navigationOptions": @{}},
																		@"children": @[]}];
	XCTAssertFalse([ans prefersStatusBarHidden]);
}

- (void)testNavigationOptions_true {
	UIViewController *ans = [self.factory createLayoutAndSaveToStore: @{@"id": @"cntId_2",
																	    @"type": @"Container",
																		@"data": @{
																			@"navigationOptions": @{
																					@"statusBarHidden": @(1)
																					}
																			},
																		@"children": @[]}];
	XCTAssertTrue([ans prefersStatusBarHidden]);
}

- (void)testNavigationOptions_false {
	UIViewController *ans = [self.factory createLayoutAndSaveToStore: @{@"id": @"cntId_2",
																		@"type": @"Container",
																		@"data": @{
																				@"navigationOptions": @{
																						@"statusBarHidden": @(0)
																						}
																				},
																		@"children": @[]}];
	XCTAssertFalse([ans prefersStatusBarHidden]);
}

- (void)testCreateLayout_addContainerToStore {
	NSString *containerId = @"cntId";
	UIViewController *ans = [self.factory createLayoutAndSaveToStore:
							 @{@"id": containerId,
							   @"type": @"Container",
							   @"data": @{},
							   @"children": @[]}];
	
	UIViewController *storeAns = [self.store findContainerForId:containerId];
	XCTAssertEqualObjects(ans, storeAns);
}



@end
