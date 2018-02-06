#import <XCTest/XCTest.h>
#import "RNNStore.h"
#import "RNNNavigationStackManager.h"

@interface MockUINavigationController : UINavigationController
@property (nonatomic, strong) NSArray* willReturnVCs;
@end

@implementation MockUINavigationController

-(NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController animated:(BOOL)animated {
	return self.willReturnVCs;
}

-(NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated {
	return self.willReturnVCs;
}

@end


@interface RNNNavigationStackManagerTest : XCTestCase

@property (nonatomic, strong) RNNStore *store;
@property (nonatomic, strong) RNNNavigationStackManager *uut;
@property (nonatomic, strong) MockUINavigationController *nvc;
@property (nonatomic, strong) UIViewController *vc1;
@property (nonatomic, strong) UIViewController *vc2;
@property (nonatomic, strong) UIViewController *vc3;

@end

@implementation RNNNavigationStackManagerTest

- (void)setUp {
    [super setUp];
	self.store = [RNNStore new];
	self.uut = [[RNNNavigationStackManager alloc] initWithStore:self.store];
	
	self.nvc = [[MockUINavigationController alloc] init];
	self.vc1 = [RNNRootViewController new];
	self.vc2 = [RNNRootViewController new];
	self.vc3 = [RNNRootViewController new];
	NSArray *vcArray = @[self.vc1, self.vc2, self.vc3];
	[self.nvc setViewControllers:vcArray];
	
	[self.store setComponent:self.vc1 componentId:@"vc1"];
	[self.store setComponent:self.vc2 componentId:@"vc2"];
	[self.store setComponent:self.vc3 componentId:@"vc3"];
	
	
}


- (void)testPop_removeTopVCFromStore {
	[self.uut pop:@"vc3" withTransitionOptions:nil];
	XCTAssertNil([self.store findComponentForId:@"vc3"]);
	XCTAssertNotNil([self.store findComponentForId:@"vc2"]);
	XCTAssertNotNil([self.store findComponentForId:@"vc1"]);
}

- (void)testPopToSpecificVC_removeAllPopedVCFromStore {
	self.nvc.willReturnVCs = @[self.vc2, self.vc3];
	[self.uut popTo:@"vc1"];
	
	XCTAssertNil([self.store findComponentForId:@"vc2"]);
	XCTAssertNil([self.store findComponentForId:@"vc3"]);
	XCTAssertNotNil([self.store findComponentForId:@"vc1"]);
	
}

- (void)testPopToRoot_removeAllTopVCsFromStore {
	self.nvc.willReturnVCs = @[self.vc2, self.vc3];
	[self.uut popToRoot:@"vc3"];
	
	XCTAssertNil([self.store findComponentForId:@"vc2"]);
	XCTAssertNil([self.store findComponentForId:@"vc3"]);
	XCTAssertNotNil([self.store findComponentForId:@"vc1"]);

}




@end
