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

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
//	[self.store setReadyToReceiveCommands:true];
	self.store = [[RNNStore alloc] init];
	self.uut = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:nil eventEmitter:nil];
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
	RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:vc];
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

@end
