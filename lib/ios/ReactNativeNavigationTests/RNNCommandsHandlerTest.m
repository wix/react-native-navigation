#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"
#import "RNNNavigationOptions.h"
#import "RNNTestRootViewCreator.h"
#import "RNNRootViewController.h"

@interface RNNCommandsHandlerTest : XCTestCase

@property (nonatomic, strong) RNNStore* store;
@property (nonatomic, strong) RNNCommandsHandler* uut;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
	self.store = [[RNNStore alloc] init];
	self.uut = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:nil];
}


- (void)testAssertReadyForEachMethodThrowsExceptoins {
	NSArray* methods = [self getPublicMethodNamesForObject:self.uut];
	
	for (NSString* methodName in methods) {
		
		__strong id uut = self.uut;
		SEL s = NSSelectorFromString(methodName);
		IMP imp = [uut methodForSelector:s];
		void (*func)(id, SEL) = (void *)imp;
		
		XCTAssertThrowsSpecificNamed(func(uut,s), NSException, @"BridgeNotLoadedError");
	}
}

-(NSArray*) getPublicMethodNamesForObject:(NSObject*)obj{
	NSMutableArray* skipMethods = [NSMutableArray new];
	[skipMethods addObject:@"initWithStore:controllerFactory:"];
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
	[initialOptions setTitle:@"the title"];
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:@"name"
																withOptions:initialOptions
															withContainerId:@"containerId"
															rootViewCreator:[[RNNTestRootViewCreator alloc] init]
															   eventEmitter:nil];
	UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	[vc viewWillAppear:false];
	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
	
	[self.store setReadyToReceiveCommands:true];
	[self.store setContainer:vc containerId:@"containerId"];
	
	NSDictionary* dictFromJs = @{@"topBarBackgroundColor" :@(0xFFFF0000)};
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	
	[self.uut setOptions:@"containerId" options:dictFromJs];
	
	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
	XCTAssertTrue([nav.navigationBar.barTintColor isEqual:expectedColor]);
}

@end
