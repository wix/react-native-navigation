#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"
#import "RNNNavigationOptions.h"
#import "RNNTestRootViewCreator.h"
#import "RNNRootViewController.h"

@interface RNNCommandsHandlerTest : XCTestCase

@property (nonatomic, strong) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) NSString* pageName;
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) id emitter;
@property (nonatomic, strong) RNNStore* store;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNRootViewController* viewController;
@property (nonatomic, strong) RNNCommandsHandler* cmdHandler;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
	self.creator = [[RNNTestRootViewCreator alloc] init];
	self.pageName = @"somename";
	self.containerId = @"cntId";
	self.emitter = nil;
	self.options = [[RNNNavigationOptions alloc] initWithDict:@{@"title" : @"static title"}];
	self.store = [RNNStore new];
	self.viewController = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
	[self.store setReadyToReceiveCommands:true];
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.viewController];
	[self.store setContainer:self.viewController containerId:self.containerId];
	self.cmdHandler = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:nil];
}


- (void)testAssertReadyForEachMethodThrowsExceptoins {
	RNNStore *store = [RNNStore new];
	[store setReadyToReceiveCommands:NO];
	RNNCommandsHandler *uut = [[RNNCommandsHandler alloc] initWithStore:store controllerFactory:nil];
	
	NSArray* methods = [self getPublicMethodNamesForObject:uut];
	
	for (NSString* methodName in methods) {
		SEL s = NSSelectorFromString(methodName);
		XCTAssertThrowsSpecificNamed([uut performSelector:s withObject:nil], NSException, @"BridgeNotLoadedError");
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
		if (![skipMethods containsObject:methodName]) {
			[result addObject:methodName];
		}
	}
	
	return result;
}

-(void)testDynamicTopBarBackgroundColor_validColor {
	NSDictionary* dictFromJs = @{@"topBarBackgroundColor" :@(0xFFFF0000)};
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	[self.cmdHandler setOptions:self.containerId options:dictFromJs];
	XCTAssertTrue([self.viewController.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
}

-(void)testDynamicStylesMergeWithStaticStyles {
	NSDictionary* dictFromJs = @{@"topBarBackgroundColor" :@(0xFFFF0000)};
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	[self.cmdHandler setOptions:self.containerId options:dictFromJs];
	XCTAssertTrue([self.viewController.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
	XCTAssertTrue([self.viewController.navigationItem.title isEqual:@"static title"]);
}




@end
