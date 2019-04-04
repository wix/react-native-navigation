#import <XCTest/XCTest.h>
#import <objc/runtime.h>
#import "RNNCommandsHandler.h"
#import "RNNNavigationOptions.h"
#import "RNNTestRootViewCreator.h"
#import "RNNRootViewController.h"
#import "RNNNavigationController.h"
#import "RNNErrorHandler.h"
#import <OCMock/OCMock.h>

@interface MockUIApplication : NSObject

-(UIWindow *)keyWindow;

@end

@implementation MockUIApplication

- (UIWindow *)keyWindow {
	return [UIWindow new];
}

@end

@interface MockUINavigationController : RNNNavigationController
@property (nonatomic, strong) NSArray* willReturnVCs;
@end

@implementation MockUINavigationController

-(NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController animated:(BOOL)animated {
	return self.willReturnVCs;
}

-(NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated {
	return self.willReturnVCs;
}

@end

@interface RNNCommandsHandlerTest : XCTestCase

@property (nonatomic, strong) id store;
@property (nonatomic, strong) RNNCommandsHandler* uut;
@property (nonatomic, strong) RNNRootViewController* vc1;
@property (nonatomic, strong) RNNRootViewController* vc2;
@property (nonatomic, strong) RNNRootViewController* vc3;
@property (nonatomic, strong) MockUINavigationController* nvc;
@property (nonatomic, strong) id mainWindow;
@property (nonatomic, strong) id sharedApplication;
@property (nonatomic, strong) id controllerFactory;
@property (nonatomic, strong) id overlayManager;
@property (nonatomic, strong) id eventEmmiter;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
	[super setUp];
	self.mainWindow = [OCMockObject partialMockForObject:[UIWindow new]];
	self.store = [OCMockObject partialMockForObject:[[RNNStore alloc] init]];
	self.eventEmmiter = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
	self.overlayManager = [OCMockObject partialMockForObject:[RNNOverlayManager new]];
	self.controllerFactory = [OCMockObject partialMockForObject:[[RNNControllerFactory alloc] initWithRootViewCreator:nil eventEmitter:self.eventEmmiter store:self.store componentRegistry:nil andBridge:nil]];
	self.uut = [[RNNCommandsHandler alloc] initWithStore:self.store controllerFactory:self.controllerFactory eventEmitter:self.eventEmmiter stackManager:[RNNNavigationStackManager new] modalManager:[RNNModalManager new] overlayManager:self.overlayManager mainWindow:_mainWindow];
	self.vc1 = [RNNRootViewController new];
	self.vc2 = [RNNRootViewController new];
	self.vc3 = [RNNRootViewController new];
	_nvc = [[MockUINavigationController alloc] init];
	[_nvc setViewControllers:@[self.vc1, self.vc2, self.vc3]];
	[self.store setComponent:self.vc1 componentId:@"vc1"];
	[self.store setComponent:self.vc2 componentId:@"vc2"];
	[self.store setComponent:self.vc3 componentId:@"vc3"];
	OCMStub([self.sharedApplication keyWindow]).andReturn(self.mainWindow);
}


- (void)testAssertReadyForEachMethodThrowsExceptoins {
	NSArray* methods = [self getPublicMethodNamesForObject:self.uut];
	[self.store setReadyToReceiveCommands:false];
	for (NSString* methodName in methods) {
		SEL s = NSSelectorFromString(methodName);
		IMP imp = [self.uut methodForSelector:s];
		void (*func)(id, SEL, id, id, id, id, id) = (void *)imp;
		
		XCTAssertThrowsSpecificNamed(func(self.uut,s, nil, nil, nil, nil, nil), NSException, @"BridgeNotLoadedError");
	}
}

-(NSArray*) getPublicMethodNamesForObject:(NSObject*)obj{
	NSMutableArray* skipMethods = [NSMutableArray new];
	
	[skipMethods addObject:@"initWithStore:controllerFactory:eventEmitter:stackManager:modalManager:overlayManager:mainWindow:"];
	[skipMethods addObject:@"assertReady"];
	[skipMethods addObject:@"removePopedViewControllers:"];
	[skipMethods addObject:@".cxx_destruct"];
	[skipMethods addObject:@"dismissedModal:"];
	[skipMethods addObject:@"dismissedMultipleModals:"];
	
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
	initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"the title"];
	RNNLayoutInfo* layoutInfo = [RNNLayoutInfo new];
	RNNTestRootViewCreator* creator = [[RNNTestRootViewCreator alloc] init];

	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithLayoutInfo:layoutInfo rootViewCreator:creator eventEmitter:nil presenter:presenter options:initialOptions defaultOptions:nil];

	RNNNavigationController* nav = [[RNNNavigationController alloc] initWithLayoutInfo:nil creator:creator options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:nil presenter:[[RNNNavigationControllerPresenter alloc] init] eventEmitter:nil];
	[nav setViewControllers:@[vc]];
	
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

- (void)testMergeOptions_shouldOverrideOptions {
	RNNNavigationOptions* initialOptions = [[RNNNavigationOptions alloc] initWithDict:@{}];
	initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"the title"];
	
	RNNViewControllerPresenter* presenter = [[RNNViewControllerPresenter alloc] init];
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithLayoutInfo:nil rootViewCreator:[[RNNTestRootViewCreator alloc] init] eventEmitter:nil presenter:presenter options:initialOptions defaultOptions:nil];
	
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:vc];
	[vc viewWillAppear:false];
	XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
	
	[self.store setReadyToReceiveCommands:true];
	[self.store setComponent:vc componentId:@"componentId"];
	
	NSDictionary* dictFromJs = @{@"topBar": @{@"title" : @{@"text" : @"new title"}}};
	
	[self.uut mergeOptions:@"componentId" options:dictFromJs completion:^{
		XCTAssertTrue([vc.navigationItem.title isEqual:@"new title"]);
	}];
}

- (void)testShowOverlay_createLayout {
	[self.store setReadyToReceiveCommands:true];
	OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
	NSDictionary* layout = @{};
	
	[[self.controllerFactory expect] createLayout:layout];
	[self.uut showOverlay:layout commandId:@"" completion:^{}];
	[self.controllerFactory verify];
}

- (void)testShowOverlay_saveToStore {
	[self.store setReadyToReceiveCommands:true];
	OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]);
	
	[[self.controllerFactory expect] createLayout:[OCMArg any]];
	[self.uut showOverlay:@{} commandId:@"" completion:^{}];
	[self.overlayManager verify];
}

- (void)testShowOverlay_withCreatedLayout {
	[self.store setReadyToReceiveCommands:true];
	UIViewController* layoutVC = [RNNRootViewController new];
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(layoutVC);
	
	[[self.overlayManager expect] showOverlayWindow:[OCMArg any]];
	[self.uut showOverlay:@{} commandId:@"" completion:^{}];
	[self.overlayManager verify];
}

- (void)testShowOverlay_invokeNavigationCommandEventWithLayout {
	[self.store setReadyToReceiveCommands:true];
	OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
	id mockedVC = [OCMockObject partialMockForObject:self.vc1];	
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);
	
	NSDictionary* layout = @{};
	
	[[self.eventEmmiter expect] sendOnNavigationCommandCompletion:@"showOverlay" commandId:[OCMArg any] params:[OCMArg any]];
	[self.uut showOverlay:layout commandId:@"" completion:^{}];
	[self.eventEmmiter verify];
}

- (void)testDismissOverlay_findComponentFromStore {
	[self.store setReadyToReceiveCommands:true];
	NSString* componentId = @"componentId";
	[[self.store expect] findComponentForId:componentId];
	[self.uut dismissOverlay:componentId commandId:@"" completion:^{} rejection:^(NSString *code, NSString *message, NSError *error) {}];
	[self.store verify];
}

- (void)testDismissOverlay_dismissReturnedViewController {
	[self.store setReadyToReceiveCommands:true];
	NSString* componentId = @"componentId";
	UIViewController* returnedView = [UIViewController new];
	OCMStub([self.store findComponentForId:componentId]).andReturn(returnedView);
	
	[[self.overlayManager expect] dismissOverlay:returnedView];
	[self.uut dismissOverlay:componentId commandId:@"" completion:^{} rejection:^(NSString *code, NSString *message, NSError *error) {}];
	[self.overlayManager verify];
}

- (void)testDismissOverlay_handleErrorIfNoOverlayExists {
	[self.store setReadyToReceiveCommands:true];
	NSString* componentId = @"componentId";
	id errorHandlerMockClass = [OCMockObject mockForClass:[RNNErrorHandler class]];
	
	[[errorHandlerMockClass expect] reject:[OCMArg any] withErrorCode:1010 errorDescription:[OCMArg any]];
	[self.uut dismissOverlay:componentId commandId:@"" completion:[OCMArg any] rejection:[OCMArg any]];
	[errorHandlerMockClass verify];
}

- (void)testDismissOverlay_invokeNavigationCommandEvent {
	[self.store setReadyToReceiveCommands:true];
	NSString* componentId = @"componentId";
	OCMStub([self.store findComponentForId:componentId]).andReturn([UIViewController new]);
	
	[[self.eventEmmiter expect] sendOnNavigationCommandCompletion:@"dismissOverlay" commandId:[OCMArg any] params:[OCMArg any]];
	[self.uut dismissOverlay:componentId commandId:@"" completion:^{
		
	} rejection:^(NSString *code, NSString *message, NSError *error) {}];
	
	[self.eventEmmiter verify];
}

- (void)testSetRoot_setRootViewControllerOnMainWindow {
	[self.store setReadyToReceiveCommands:true];
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(self.vc1);
	
	[[self.mainWindow expect] setRootViewController:self.vc1];
	[self.uut setRoot:@{} commandId:@"" completion:^{}];
	[self.mainWindow verify];
}

- (void)testSetStackRoot_resetStackWithSingleComponent {
	OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]]).andReturn(@[self.vc2]);
	[self.store setReadyToReceiveCommands:true];
	[self.uut setStackRoot:@"vc1" commandId:@"" children:nil completion:^{
		
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	XCTAssertEqual(_nvc.viewControllers.firstObject, self.vc2);
	XCTAssertEqual(_nvc.viewControllers.count, 1);
}

- (void)testSetStackRoot_setMultipleChildren {
	NSArray* newViewControllers = @[_vc1, _vc3];
	OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]]).andReturn(newViewControllers);
	[self.store setReadyToReceiveCommands:true];
	[self.uut setStackRoot:@"vc1" commandId:@"" children:nil completion:^{
		
	} rejection:^(NSString *code, NSString *message, NSError *error) {
		
	}];
	XCTAssertTrue([_nvc.viewControllers isEqual:newViewControllers]);
}

- (void)testSetRoot_waitForRenderTrue {
	[self.store setReadyToReceiveCommands:true];
	self.vc1.options = [[RNNNavigationOptions alloc] initEmptyOptions];
	self.vc1.options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:YES];
	
	id mockedVC = [OCMockObject partialMockForObject:self.vc1];
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);
	
	[[mockedVC expect] renderTreeAndWait:YES perform:[OCMArg any]];
	[self.uut setRoot:@{} commandId:@"" completion:^{}];
	[mockedVC verify];
}

- (void)testSetRoot_waitForRenderFalse {
	[self.store setReadyToReceiveCommands:true];
	self.vc1.options = [[RNNNavigationOptions alloc] initEmptyOptions];
	self.vc1.options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:NO];
	
	id mockedVC = [OCMockObject partialMockForObject:self.vc1];
	OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);
	
	[[mockedVC expect] renderTreeAndWait:NO perform:[OCMArg any]];
	[self.uut setRoot:@{} commandId:@"" completion:^{}];
	[mockedVC verify];
}

@end
