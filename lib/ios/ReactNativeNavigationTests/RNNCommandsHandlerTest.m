#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"
#import "RNNNavigationOptions.h"
#import "RNNTestRootViewCreator.h"
#import "RNNRootViewController.h"
#import "RNNNavigationController.h"

@interface RNNCommandsHandlerTest : XCTestCase

@property (nonatomic, strong) RNNStore* store;
@property (nonatomic, strong) RNNCommandsHandler* uut;
@property (nonatomic, strong) RCTBridge* bridge;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
//	[self.store setReadyToReceiveCommands:true];
	self.store = [[RNNStore alloc] init];
	self.bridge = nil;
	self.uut = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:nil andBridge:self.bridge];
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

	[skipMethods addObject:@"initWithStore:controllerFactory:andBridge:"];
	[skipMethods addObject:@"assertReady"];
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
	RNNNavigationOptions* initialOptions = [[RNNNavigationOptions alloc] init];
	[initialOptions.topBar setTitle:@"the title"];
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:@"name"
																withOptions:initialOptions
															withContainerId:@"containerId"
															rootViewCreator:[[RNNTestRootViewCreator alloc] init]
															   eventEmitter:nil
																   animator:nil];
	RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:vc];
	[vc viewWillAppear:false];
	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);

	[self.store setReadyToReceiveCommands:true];
	[self.store setContainer:vc containerId:@"containerId"];
	
	NSDictionary* dictFromJs = @{@"topBar": @{@"backgroundColor" :@(0xFFFF0000)}};
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];

	[self.uut setOptions:@"containerId" options:dictFromJs];

	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
	XCTAssertTrue([nav.navigationBar.barTintColor isEqual:expectedColor]);
}

@end
