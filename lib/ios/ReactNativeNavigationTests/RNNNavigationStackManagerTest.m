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
	self.vc1 = [UIViewController new];
	self.vc2 = [UIViewController new];
	self.vc3 = [UIViewController new];
	NSArray *vcArray = @[self.vc1, self.vc2, self.vc3];
	[self.nvc setViewControllers:vcArray];
	
	[self.store setContainer:self.vc1 containerId:@"vc1"];
	[self.store setContainer:self.vc2 containerId:@"vc2"];
	[self.store setContainer:self.vc3 containerId:@"vc3"];
	
	
}


- (void)testPop_removeTopVCFromStore {
	[self.uut pop:@"vc3"];
	
	XCTAssertNil([self.store findContainerForId:@"vc3"]);
	XCTAssertNotNil([self.store findContainerForId:@"vc2"]);
	XCTAssertNotNil([self.store findContainerForId:@"vc1"]);
}

- (void)testPopToSpecificVC_removeAllPopedVCFromStore {
	self.nvc.willReturnVCs = @[self.vc2, self.vc3];
	[self.uut popTo:@"vc1"];
	
	XCTAssertNil([self.store findContainerForId:@"vc2"]);
	XCTAssertNil([self.store findContainerForId:@"vc3"]);
	XCTAssertNotNil([self.store findContainerForId:@"vc1"]);
	
}

- (void)testPopToRoot_removeAllTopVCsFromStore {
	self.nvc.willReturnVCs = @[self.vc2, self.vc3];
	[self.uut popToRoot:@"vc3"];
	
	XCTAssertNil([self.store findContainerForId:@"vc2"]);
	XCTAssertNil([self.store findContainerForId:@"vc3"]);
	XCTAssertNotNil([self.store findContainerForId:@"vc1"]);

}




@end
