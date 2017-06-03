#import <XCTest/XCTest.h>
#import "RNNModalManager.h"

@interface RNNModalManagerTest : XCTestCase

@end

@implementation RNNModalManagerTest

- (void)testDismissAllModalsCleansPendingModalsToDismiss {
	RNNStore *store = [RNNStore new];
	[[store pendingModalIdsToDismiss] addObject:@"modal_id_1"];
	[[store pendingModalIdsToDismiss] addObject:@"modal_id_2"];
	[[store pendingModalIdsToDismiss] addObject:@"modal_id_3"];
	
	RNNModalManager *modalManager = [[RNNModalManager alloc] initWithStore:store];
	[modalManager dismissAllModals];
	
	XCTAssertTrue([[store pendingModalIdsToDismiss] count] == 0);
}

@end
