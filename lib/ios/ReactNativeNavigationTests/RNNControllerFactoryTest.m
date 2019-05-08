
#import <XCTest/XCTest.h>
#import "RNNControllerFactory.h"
#import "RNNRootViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopTabsViewController.h"
#import "RNNSplitViewController.h"

@interface RNNControllerFactoryTest : XCTestCase

@property (nonatomic, strong) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) RNNControllerFactory *factory;
@property (nonatomic, strong) RNNExternalComponentStore *store;

@end

@implementation RNNControllerFactoryTest

- (void)setUp {
	[super setUp];
	self.creator = nil;
	self.store = [RNNExternalComponentStore new];
	self.factory = [[RNNControllerFactory alloc] initWithRootViewCreator:self.creator eventEmitter:nil store:self.store componentRegistry:nil andBridge:nil];
}

- (void)tearDown {
	[super tearDown];
}

- (void)testCreateLayout_EmptyLayout {
	XCTAssertThrows([self.factory createLayout:@{}]);
}

- (void)testCreateLayout_ComponentLayout {
	NSDictionary* layout = @{@"id": @"cntId",
							 @"type": @"Component",
							 @"data": @{},
							 @"children": @[]};
	id ans = [self.factory createLayout:layout];
	XCTAssertTrue([ans isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_ExternalComponentLayout {
	[_store registerExternalComponent:@"externalComponent" callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
		return [UIViewController new];
	}];
	
	NSDictionary* layout =  @{@"id": @"cntId",
							  @"type": @"ExternalComponent",
							  @"data": @{@"name": @"externalComponent"},
							  @"children": @[]};
	id ans = [self.factory createLayout:layout];
	XCTAssertTrue([ans isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_ComponentStackLayout {
	NSDictionary* layout = @{@"id": @"cntId",
							 @"type": @"Stack",
							 @"data": @{},
							 @"children": @[]};
	id ans = [self.factory createLayout:layout];
	XCTAssertTrue([ans isMemberOfClass:[RNNNavigationController class]]);
}

- (void)testCreateLayout_SplitViewLayout {
	NSDictionary* layout = @{@"id": @"cntId",
							 @"type": @"SplitView",
							 @"data": @{},
							 @"children": @[
									 @{@"id": @"cntId_2",
									   @"type": @"Component",
									   @"data": @{},
									   @"children": @[]},
									 @{@"id": @"cntId_3",
									   @"type": @"Component",
									   @"data": @{},
									   @"children": @[]}]};
	id ans = [self.factory createLayout:layout];
	XCTAssertTrue([ans isMemberOfClass:[RNNSplitViewController class]]);
}

- (void)testCreateLayout_ComponentStackLayoutRecursive {
	NSDictionary* layout = @{@"id": @"cntId",
							 @"type": @"Stack",
							 @"data": @{},
							 @"children": @[
									 @{@"id": @"cntId_2",
									   @"type": @"Component",
									   @"data": @{},
									   @"children": @[]}]};
	RNNNavigationController* ans = (RNNNavigationController*) [self.factory createLayout:layout];
	
	XCTAssertTrue([ans isMemberOfClass:[RNNNavigationController class]]);
	XCTAssertTrue(ans.childViewControllers.count == 1);
	XCTAssertTrue([ans.childViewControllers[0] isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_BottomTabsLayout {
	NSDictionary* layout = @{
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
												 @"children": @[]}]}]};
	RNNTabBarController* tabBar = (RNNTabBarController*) [self.factory createLayout:layout];
	
	XCTAssertTrue([tabBar isMemberOfClass:[RNNTabBarController class]]);
	XCTAssertTrue(tabBar.childViewControllers.count == 1);
	XCTAssertTrue([tabBar.childViewControllers[0] isMemberOfClass:[RNNNavigationController class]]);
	
	UINavigationController *navController = tabBar.childViewControllers[0];
	XCTAssertTrue(navController.childViewControllers.count == 1);
	XCTAssertTrue([navController.childViewControllers[0] isMemberOfClass:[RNNRootViewController class]]);
}

- (void)testCreateLayout_TopTabsLayout {
	NSDictionary* layout = @{
							 @"id": @"cntId",
							 @"type": @"TopTabs",
							 @"data": @{},
							 @"children": @[
									 @{@"id": @"cntId_2",
									   @"type": @"Stack",
									   @"data": @{},
									   @"children": @[
											   @{@"id": @"cntId_3",
												 @"type": @"Component",
												 @"data": @{},
												 @"children": @[]}]}]};
	RNNTopTabsViewController* tabBar = (RNNTopTabsViewController*) [self.factory createLayout:layout];
	
	XCTAssertTrue([tabBar isMemberOfClass:[RNNTopTabsViewController class]]);
}

- (void)testCreateLayout_ComponentSideMenuLayoutCenterLeftRight {
	NSDictionary* layout = @{@"id": @"cntId",
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
												 @"children": @[]}]}]};
	RNNSideMenuController *ans = (RNNSideMenuController*) [self.factory createLayout:layout];
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

@end
