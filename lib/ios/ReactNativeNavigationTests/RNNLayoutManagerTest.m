
#import <XCTest/XCTest.h>
#import <OCMock/OCMock.h>
#import "RNNLayoutManager.h"
#import "UIViewController+LayoutProtocol.m"

@interface RNNLayoutManagerTest : XCTestCase

@property (nonatomic, strong) UIViewController* vc1;
@property (nonatomic, strong) UIViewController* vc2;
@property (nonatomic, strong) UIViewController* vc3;

@property (nonatomic, strong) UIWindow* firstWindow;
@property (nonatomic, strong) UIWindow* secondWindow;

@end

@implementation RNNLayoutManagerTest

- (void)setUp {
	_vc1 = [self createMockViewControllerWithComponentId:@"vc1"];
	_vc2 = [self createMockViewControllerWithComponentId:@"vc2"];
	_vc3 = [self createMockViewControllerWithComponentId:@"vc3"];
	
	_firstWindow = [[UIWindow alloc] init];
	_secondWindow = [[UIWindow alloc] init];
	NSArray* windows = @[_firstWindow, _secondWindow];
	
	UIApplication* sharedApplication = [OCMockObject mockForClass:[UIApplication class]];
	id mockedApplicationClass = OCMClassMock([UIApplication class]);
	OCMStub(ClassMethod([mockedApplicationClass sharedApplication])).andReturn(sharedApplication);
	OCMStub([sharedApplication windows]).andReturn(windows);
}

- (void)testFindComponentForIdShouldReturnVCInFirstWindowRoot {
	_firstWindow.rootViewController = _vc1;
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vc1"];
	XCTAssertEqual(resultVC, _vc1);
}

- (void)testFindComponentForIdShouldReturnVCFromSecondWindowRoot {
	_secondWindow.rootViewController = _vc1;
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vc1"];
	XCTAssertEqual(resultVC, _vc1);
}

- (void)testFindComponentShouldReturnModalFromFirstWindow {
	UIViewController* rootViewController = [OCMockObject partialMockForObject:[UIViewController new]];
	
	UIViewController* modal = _vc1;
	OCMStub([rootViewController presentedViewController]).andReturn(modal);
	_firstWindow.rootViewController = rootViewController;
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vc1"];
	XCTAssertEqual(resultVC, modal);
}

- (void)testFindComponentShouldReturnModalFromSecondWindow {
	UIViewController* rootViewController = [OCMockObject partialMockForObject:[UIViewController new]];
	UIViewController* modal = _vc1;
	OCMStub([rootViewController presentedViewController]).andReturn(modal);
	_secondWindow.rootViewController = rootViewController;
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vc1"];
	XCTAssertEqual(resultVC, modal);
}

- (void)testFindComponentShouldReturnNilForUndefindComponent {
	_firstWindow.rootViewController = _vc1;
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vvc"];
	XCTAssertNil(resultVC);
}

- (void)testFindComponentShouldFindRootViewControllerChildViewController {
	UINavigationController* nvc = [[UINavigationController alloc] init];
	[nvc setViewControllers:@[_vc1, _vc2, _vc3]];
	_secondWindow.rootViewController = nvc;
	
	UIViewController *resultVC = [RNNLayoutManager findComponentForId:@"vc3"];
	XCTAssertEqual(resultVC, _vc3);
}

- (UIViewController *)createMockViewControllerWithComponentId:(NSString *)componentId {
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	layoutInfo.componentId = componentId;
	
	UIViewController* vc = [UIViewController new];
	vc.layoutInfo = layoutInfo;
	
	return vc;
}

@end
