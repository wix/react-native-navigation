#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNModalManager.h"
#import "RNNComponentViewController.h"
#import "RNNStackController.h"

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
	RNNComponentViewController* _vc1;
	RNNComponentViewController* _vc2;
	RNNComponentViewController* _vc3;
	MockModalManager* _modalManager;
}

- (void)setUp {
	[super setUp];
	_vc1 = [RNNComponentViewController new];
	_vc2 = [RNNComponentViewController new];
	_vc3 = [RNNComponentViewController new];
	_vc1.layoutInfo = [RNNLayoutInfo new];
	_vc2.layoutInfo = [RNNLayoutInfo new];
	_vc3.layoutInfo = [RNNLayoutInfo new];
	_modalManager = [[MockModalManager alloc] init];
	_modalManager.topPresentedVC = [MockViewController new];
}

- (void)testDismissMultipleModalsInvokeDelegateWithCorrectParameters {
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	[_modalManager showModal:_vc2 animated:NO completion:nil];
	[_modalManager showModal:_vc3 animated:NO completion:nil];
	
	_modalManager.delegate = self;
	[_modalManager dismissAllModalsAnimated:NO completion:nil];
	
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
	[_modalManager dismissAllModalsAnimated:NO completion:nil];
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

- (void)testDismissModal_ShouldInvokeDelegateDismissedModal {
	id mockDelegate = [OCMockObject mockForProtocol:@protocol(RNNModalManagerDelegate)];
	_modalManager.delegate = mockDelegate;
	[_modalManager showModal:_vc1 animated:NO completion:nil];
	
	[[mockDelegate expect] dismissedModal:_vc1];
	[_modalManager dismissModal:_vc1 completion:nil];
	[mockDelegate verify];
}

- (void)testPresentationControllerDidDismiss_ShouldInvokeDelegateDismissedModal {
	id mockDelegate = [OCMockObject mockForProtocol:@protocol(RNNModalManagerDelegate)];
	_modalManager.delegate = mockDelegate;
	
	UIPresentationController* presentationController = [[UIPresentationController alloc] initWithPresentedViewController:_vc2 presentingViewController:_vc1];
	
	[[mockDelegate expect] dismissedModal:_vc2];
	[_modalManager presentationControllerDidDismiss:presentationController];
	[mockDelegate verify];
}

- (void)testPresentationControllerDidDismiss_ShouldInvokeDelegateDismissedModalWithPresentedChild {
	id mockDelegate = [OCMockObject mockForProtocol:@protocol(RNNModalManagerDelegate)];
	_modalManager.delegate = mockDelegate;
	RNNStackController* nav = [[RNNStackController alloc] initWithRootViewController:_vc2];
	
	UIPresentationController* presentationController = [[UIPresentationController alloc] initWithPresentedViewController:nav presentingViewController:_vc1];
	
	[[mockDelegate expect] dismissedModal:_vc2];
	[_modalManager presentationControllerDidDismiss:presentationController];
	[mockDelegate verify];
}

#pragma mark RNNModalManagerDelegate

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
	_modalDismissedCount = viewControllers.count;
}

- (void)dismissedModal:(UIViewController *)viewController {
	_modalDismissedCount = 1;
}

@end
