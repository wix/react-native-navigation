#import <XCTest/XCTest.h>
#import "RNNModalManager.h"

@interface MockViewController : UIViewController

@property CGFloat presentViewControllerCalls;

@end
@implementation MockViewController

- (void)presentViewController:(UIViewController *)viewControllerToPresent animated:(BOOL)flag completion:(void (^)(void))completion {
	_presentViewControllerCalls++;
	completion();
}

@end

@interface MockModalManager : RNNModalManager
@property (nonatomic, strong) MockViewController* topPresentedVC;
@end

@implementation MockModalManager
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
	_modalManager.topPresentedVC = [MockViewController new];
}

- (void)testDismissMultipleModalsInvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissAllModalsAnimated:NO];
	
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

- (void)testDismissAllModals_AfterDismissingPreviousModal_InvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissModal:_vc2 completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 1);
	[_modalManager dismissAllModalsAnimated:NO];
	XCTAssertTrue(_modalDismissedCount == 2);
}

- (void)testDismissModal_DismissNilModalDoesntCrash {
	_modalManager.delegate = self;
	[_modalManager dismissModal:nil completion:nil];
	
	XCTAssertTrue(_modalDismissedCount == 0);
}

- (void)testShowModal_NilModalThrows {
	XCTAssertThrows([_modalManager showModal:nil animated:NO completion:nil]);
}

- (void)testShowModal_CallPresentViewController {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	XCTAssertTrue(_modalManager.topPresentedVC.presentViewControllerCalls == 1);
}

#pragma mark RNNModalManagerDelegate

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	_modalDismissedCount = viewControllers.count;
}

- (void)dismissedModal:(UIViewController *)viewController {
	_modalDismissedCount = 1;
}

@end
