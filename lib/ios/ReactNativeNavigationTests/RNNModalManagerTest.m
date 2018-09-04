#import <XCTest/XCTest.h>
#import "RNNModalManager.h"

@interface MockViewController : UIViewController
@end
@implementation MockViewController

- (void)presentViewController:(UIViewController *)viewControllerToPresent animated:(BOOL)flag completion:(void (^)(void))completion {
	completion();
}

@end

@interface MockModalManager : RNNModalManager
@end
@implementation MockModalManager

-(UIViewController*)topPresentedVC {
	MockViewController* vc = [MockViewController new];
	return vc;
}

@end

@interface RNNModalManagerTest : XCTestCase <RNNModalManagerDelegate> {
	CGFloat _modalDismissedCount;
}

@end

@implementation RNNModalManagerTest {
	RNNRootViewController* _vc1;
	RNNRootViewController* _vc2;
	RNNRootViewController* _vc3;
	MockModalManager* _modalManager;
}

- (void)setUp {
	[super setUp];
	_vc1 = [RNNRootViewController new];
	_vc2 = [RNNRootViewController new];
	_vc3 = [RNNRootViewController new];
	_modalManager = [[MockModalManager alloc] init];
}
- (void)testDismissMultipleModalsInvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissAllModals];
	
	XCTAssertTrue(_modalDismissedCount == 3);
}

- (void)testDismissModal_InvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissModal:_vc3 completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 1);
}

- (void)testDismissPreviousModal_InvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissModal:_vc2 completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 1);
}

- (void)testDismissAllModalsAfterDismissingPreviousModal_InvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissModal:_vc2 completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 1);
	[_modalManager dismissAllModals];
	XCTAssertTrue(_modalDismissedCount == 2);
}

- (void)testDismissNilModal_doesntCrash {
	_modalManager.delegate = self;
	[_modalManager dismissModal:nil completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 0);
}


#pragma mark RNNModalManagerDelegate

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	_modalDismissedCount = viewControllers.count;
}

- (void)dismissedModal:(UIViewController *)viewController {
	_modalDismissedCount = 1;
}

@end
