#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"
#import "RNNNavigationOptions.h"
#import "RNNTestRootViewCreator.h"
#import "RNNRootViewController.h"
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

@interface RNNCommandsHandlerTest : XCTestCase

@property (nonatomic, strong) RNNStore* store;
@property (nonatomic, strong) RNNCommandsHandler* uut;
@property (nonatomic, strong) RNNRootViewController* vc1;
@property (nonatomic, strong) RNNRootViewController* vc2;
@property (nonatomic, strong) RNNRootViewController* vc3;
@property (nonatomic, strong) MockUINavigationController* nvc;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
//	[self.store setReadyToReceiveCommands:true];
	self.store = [[RNNStore alloc] init];
	self.uut = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:nil eventEmitter:nil];
	self.vc1 = [RNNRootViewController new];
	self.vc2 = [RNNRootViewController new];
	self.vc3 = [RNNRootViewController new];
	_nvc = [[MockUINavigationController alloc] init];
	[_nvc setViewControllers:@[self.vc1, self.vc2, self.vc3]];
	[self.store setComponent:self.vc1 componentId:@"vc1"];
	[self.store setComponent:self.vc2 componentId:@"vc2"];
	[self.store setComponent:self.vc3 componentId:@"vc3"];
}


- (void)testAssertReadyForEachMethodThrowsExceptoins {
	NSArray* methods = [self getPublicMethodNamesForObject:self.uut];
	[self.store setReadyToReceiveCommands:false];
	for (NSString* methodName in methods) {
		SEL s = NSSelectorFromString(methodName);
		IMP imp = [self.uut methodForSelector:s];
		void (*func)(id, SEL, id, id, id) = (void *)imp;
		
		XCTAssertThrowsSpecificNamed(func(self.uut,s, nil, nil, nil), NSException, @"BridgeNotLoadedError");
	}
}

-(NSArray*) getPublicMethodNamesForObject:(NSObject*)obj{
	NSMutableArray* skipMethods = [NSMutableArray new];

	[skipMethods addObject:@"initWithStore:controllerFactory:eventEmitter:"];
	[skipMethods addObject:@"assertReady"];
	[skipMethods addObject:@"removePopedViewControllers:"];
	[skipMethods addObject:@".cxx_destruct"];

	NSMutableArray* result = [NSMutableArray new];

	// count and names:
	int i=0;
	unsigned int mc = 0;
	Method * mlist = class_copyMethodList(object_getClass(obj), &mc);

	for(i=0; i<mc; i++) {
		NSString *methodName = [NSString stringWithUTF8String:sel_getName(method_getName(mlist[i]))];

		// filter skippedMethods
		if (methodName && ![skipMethods containsObject:methodName]) {
			[result addObject:methodName];
		}
	}

	return result;
}

-(void)testDynamicStylesMergeWithStaticStyles {
	RNNNavigationOptions* initialOptions = [[RNNNavigationOptions alloc] initWithDict:@{}];
	initialOptions.topBar.title.text = @"the title";
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:@"name"
																withOptions:initialOptions
															withComponentId:@"componentId"
															rootViewCreator:[[RNNTestRootViewCreator alloc] init]
															   eventEmitter:nil
														  isExternalComponent:NO];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	[vc viewWillAppear:false];
	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);

	[self.store setReadyToReceiveCommands:true];
	[self.store setComponent:vc componentId:@"componentId"];
	
	NSDictionary* dictFromJs = @{@"topBar": @{@"background" : @{@"color" : @(0xFFFF0000)}}};
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];

	[self.uut mergeOptions:@"componentId" options:dictFromJs completion:^{
		XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
		XCTAssertTrue([nav.navigationBar.barTintColor isEqual:expectedColor]);
	}];
}

- (void)testPop_removeTopVCFromStore {
	[self.store setReadyToReceiveCommands:true];
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];

	[self.uut pop:@"vc3" options:nil completion:^{
		XCTAssertNil([self.store findComponentForId:@"vc3"]);
		XCTAssertNotNil([self.store findComponentForId:@"vc2"]);
		XCTAssertNotNil([self.store findComponentForId:@"vc1"]);
		[expectation fulfill];
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testPopToSpecificVC_removeAllPopedVCFromStore {
	[self.store setReadyToReceiveCommands:true];
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	_nvc.willReturnVCs = @[self.vc2, self.vc3];
	[self.uut popTo:@"vc1" completion:^{
		XCTAssertNil([self.store findComponentForId:@"vc2"]);
		XCTAssertNil([self.store findComponentForId:@"vc3"]);
		XCTAssertNotNil([self.store findComponentForId:@"vc1"]);
		[expectation fulfill];
	} rejection:nil];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

- (void)testPopToRoot_removeAllTopVCsFromStore {
	[self.store setReadyToReceiveCommands:true];
	_nvc.willReturnVCs = @[self.vc2, self.vc3];
	XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
	[self.uut popToRoot:@"vc3" completion:^{
		XCTAssertNil([self.store findComponentForId:@"vc2"]);
		XCTAssertNil([self.store findComponentForId:@"vc3"]);
		XCTAssertNotNil([self.store findComponentForId:@"vc1"]);
		[expectation fulfill];
	} rejection:nil];
	
	[self waitForExpectationsWithTimeout:1 handler:nil];
}

@end
