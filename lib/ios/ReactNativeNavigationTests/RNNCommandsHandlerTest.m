#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"

@interface RNNCommandsHandlerTest : XCTestCase

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
	
	
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



@end
