#import "BottomTabsAttachModeFactory.h"
#import "RNNBottomTabsController.h"
#import "RNNComponentViewController+Utils.h"
#import "RNNLayoutManager.h"
#import "RNNTestRootViewCreator.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/BottomTabPresenterCreator.h>
#import <ReactNativeNavigation/RNNCommandsHandler.h>
#import <ReactNativeNavigation/RNNComponentViewController.h>
#import <ReactNativeNavigation/RNNErrorHandler.h>
#import <ReactNativeNavigation/RNNNavigationOptions.h>
#import <ReactNativeNavigation/RNNStackController.h>
#import <ReactNativeNavigation/UIViewController+RNNOptions.h>
#import <XCTest/XCTest.h>
#import <objc/runtime.h>

@interface MockUINavigationController : RNNStackController
@property(nonatomic, strong) NSArray *willReturnVCs;
@end

@implementation MockUINavigationController

- (NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController
                                            animated:(BOOL)animated {
    return self.willReturnVCs;
}

- (NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated {
    return self.willReturnVCs;
}

@end

@interface RNNCommandsHandlerTest : XCTestCase

@property(nonatomic, strong) RNNCommandsHandler *uut;
@property(nonatomic, strong) id modalManager;
@property(nonatomic, strong) RNNComponentViewController *vc1;
@property(nonatomic, strong) RNNComponentViewController *vc2;
@property(nonatomic, strong) RNNComponentViewController *vc3;
@property(nonatomic, strong) MockUINavigationController *nvc;
@property(nonatomic, strong) id mainWindow;
@property(nonatomic, strong) id sharedApplication;
@property(nonatomic, strong) id controllerFactory;
@property(nonatomic, strong) id layoutManager;
@property(nonatomic, strong) id overlayManager;
@property(nonatomic, strong) id eventEmmiter;
@property(nonatomic, strong) id setRootAnimator;
@property(nonatomic, strong) id creator;

@end

@implementation RNNCommandsHandlerTest

- (void)setUp {
    [super setUp];
    self.creator = [OCMockObject partialMockForObject:[RNNTestRootViewCreator new]];
    self.mainWindow = [OCMockObject partialMockForObject:[UIWindow new]];
    self.layoutManager = [OCMockObject partialMockForObject:[[RNNLayoutManager alloc] init]];
    self.eventEmmiter = [OCMockObject partialMockForObject:[RNNEventEmitter new]];
    self.overlayManager = [OCMockObject partialMockForObject:[RNNOverlayManager new]];
    self.modalManager = [OCMockObject partialMockForObject:[RNNModalManager new]];
    self.setRootAnimator = [OCMockObject partialMockForObject:[RNNSetRootAnimator new]];

    self.controllerFactory = [OCMockObject
        partialMockForObject:[[RNNControllerFactory alloc]
                                     initWithRootViewCreator:nil
                                                eventEmitter:self.eventEmmiter
                                                       store:nil
                                           componentRegistry:nil
                                                   andBridge:nil
                                 bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]]];
    self.uut = [[RNNCommandsHandler alloc] initWithControllerFactory:self.controllerFactory
                                                       layoutManager:self.layoutManager
                                                        eventEmitter:self.eventEmmiter
                                                        modalManager:self.modalManager
                                                      overlayManager:self.overlayManager
                                                     setRootAnimator:_setRootAnimator
                                                          mainWindow:_mainWindow];
    self.vc1 = [self generateComponentWithComponentId:@"1"];
    self.vc2 = [self generateComponentWithComponentId:@"2"];
    self.vc3 = [self generateComponentWithComponentId:@"3"];
    _nvc = [[MockUINavigationController alloc] init];
    [_nvc setViewControllers:@[ self.vc1, self.vc2, self.vc3 ]];

    UIApplication *sharedApplication = [OCMockObject niceMockForClass:[UIApplication class]];
    id mockedApplicationClass = OCMClassMock([UIApplication class]);
    OCMStub(ClassMethod([mockedApplicationClass sharedApplication])).andReturn(sharedApplication);
    OCMStub(sharedApplication.keyWindow).andReturn(self.mainWindow);
    OCMStub([sharedApplication windows]).andReturn(@[ self.mainWindow ]);
}

- (RNNComponentViewController *)generateComponentWithComponentId:(NSString *)componentId {
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = componentId;
    return [[RNNComponentViewController alloc]
        initWithLayoutInfo:layoutInfo
           rootViewCreator:_creator
              eventEmitter:nil
                 presenter:[RNNComponentPresenter new]
                   options:[[RNNNavigationOptions alloc] initWithDict:@{}]
            defaultOptions:nil];
}

- (void)testAssertReadyForEachMethodThrowsExceptions {
    NSArray *methods = [self getPublicMethodNamesForObject:self.uut];
    [self.uut setReadyToReceiveCommands:false];
    for (NSString *methodName in methods) {
        SEL s = NSSelectorFromString(methodName);
        IMP imp = [self.uut methodForSelector:s];
        void (*func)(id, SEL, id, id, id, id, id) = (void *)imp;
        XCTAssertThrowsSpecificNamed(func(self.uut, s, nil, nil, nil, nil, nil), NSException,
                                     @"BridgeNotLoadedError");
    }
}

- (NSArray *)getPublicMethodNamesForObject:(NSObject *)obj {
    NSMutableArray *skipMethods = [NSMutableArray new];

    [skipMethods addObject:@"initWithControllerFactory:layoutManager:eventEmitter:modalManager:"
                           @"overlayManager:setRootAnimator:mainWindow:"];
    [skipMethods addObject:@"assertReady"];
    [skipMethods addObject:@"setReadyToReceiveCommands:"];
    [skipMethods addObject:@"readyToReceiveCommands"];
    [skipMethods addObject:@".cxx_destruct"];
    [skipMethods addObject:@"dismissedModal:"];
    [skipMethods addObject:@"attemptedToDismissModal:"];
    [skipMethods addObject:@"dismissedMultipleModals:"];
    [skipMethods addObject:@"setDefaultOptions:completion:"];

    NSMutableArray *result = [NSMutableArray new];

    // count and names:
    int i = 0;
    unsigned int mc = 0;
    Method *mlist = class_copyMethodList(object_getClass(obj), &mc);

    for (i = 0; i < mc; i++) {
        NSString *methodName =
            [NSString stringWithUTF8String:sel_getName(method_getName(mlist[i]))];

        // filter skippedMethods
        if (methodName && ![skipMethods containsObject:methodName]) {
            [result addObject:methodName];
        }
    }

    return result;
}

- (void)testDynamicStylesMergeWithStaticStyles {
    RNNNavigationOptions *initialOptions = [[RNNNavigationOptions alloc] initWithDict:@{}];
    initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"the title"];
    RNNTestRootViewCreator *creator = [[RNNTestRootViewCreator alloc] init];
    RNNComponentViewController *vc =
        [RNNComponentViewController createWithComponentId:@"componentId"
                                           initialOptions:initialOptions];

    RNNStackController *nav =
        [[RNNStackController alloc] initWithLayoutInfo:nil
                                               creator:creator
                                               options:[RNNNavigationOptions emptyOptions]
                                        defaultOptions:nil
                                             presenter:[[RNNStackPresenter alloc] init]
                                          eventEmitter:nil
                                  childViewControllers:@[ vc ]];
    [self.mainWindow setRootViewController:nav];
    [vc viewWillAppear:false];
    XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);

    [self.uut setReadyToReceiveCommands:true];

    NSDictionary *dictFromJs = @{@"topBar" : @{@"background" : @{@"color" : @(0xFFFF0000)}}};
    UIColor *expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];

    [self.uut mergeOptions:@"componentId"
                   options:dictFromJs
                completion:^{

                }];

    XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);
    XCTAssertTrue([vc.navigationItem.standardAppearance.backgroundColor isEqual:expectedColor]);
}

- (void)testMergeOptions_shouldOverrideOptions {
    RNNNavigationOptions *initialOptions = [[RNNNavigationOptions alloc] initWithDict:@{}];
    initialOptions.topBar.title.text = [[Text alloc] initWithValue:@"the title"];

    RNNComponentViewController *vc =
        [RNNComponentViewController createWithComponentId:@"componentId"
                                           initialOptions:initialOptions];

    __unused RNNStackController *nav = [[RNNStackController alloc] initWithRootViewController:vc];
    [vc viewWillAppear:false];
    XCTAssertTrue([vc.navigationItem.title isEqual:@"the title"]);

    [self.uut setReadyToReceiveCommands:true];

    NSDictionary *dictFromJs = @{@"topBar" : @{@"title" : @{@"text" : @"new title"}}};

    [self.uut mergeOptions:@"componentId"
                   options:dictFromJs
                completion:^{
                  XCTAssertTrue([vc.navigationItem.title isEqual:@"new title"]);
                }];
}

- (void)testShowOverlay_createLayout {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
    NSDictionary *layout = @{};

    [[self.controllerFactory expect] createLayout:layout];
    [self.uut showOverlay:layout
                commandId:@""
               completion:^(NSString *componentId){
               }];
    [self.controllerFactory verify];
}

- (void)testShowOverlay_saveToStore {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]);

    [[self.controllerFactory expect] createLayout:[OCMArg any]];
    [self.uut showOverlay:@{}
                commandId:@""
               completion:^(NSString *componentId){
               }];
    [self.overlayManager verify];
}

- (void)testShowOverlay_withCreatedLayout {
    [self.uut setReadyToReceiveCommands:true];
    UIViewController *layoutVC = [self generateComponentWithComponentId:nil];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(layoutVC);

    [[self.overlayManager expect] showOverlayWindow:[OCMArg any]];
    [self.uut showOverlay:@{}
                commandId:@""
               completion:^(NSString *componentId){
               }];
    [self.overlayManager verify];
}

- (void)testShowOverlay_invokeNavigationCommandEventWithLayout {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.overlayManager showOverlayWindow:[OCMArg any]]);
    id mockedVC = [OCMockObject partialMockForObject:self.vc1];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);

    [[self.eventEmmiter expect] sendOnNavigationCommandCompletion:@"showOverlay"
                                                        commandId:[OCMArg any]];
    [self.uut showOverlay:@{}
                commandId:@""
               completion:^(NSString *componentId){
               }];
    [self.eventEmmiter verify];
}

- (void)testDismissOverlay_findComponentFromLayoutManager {
    [self.uut setReadyToReceiveCommands:true];
    NSString *componentId = @"componentId";

    [[self.layoutManager expect] findComponentForId:componentId];
    [self.uut dismissOverlay:componentId
                   commandId:@""
                  completion:^{
                  }
                   rejection:^(NSString *code, NSString *message, NSError *error){
                   }];
    [self.layoutManager verify];
}

- (void)testDismissOverlay_dismissReturnedViewController {
    [self.uut setReadyToReceiveCommands:true];
    NSString *componentId = @"componentId";
    UIViewController *returnedView = [UIViewController new];

    OCMStub([self.layoutManager findComponentForId:componentId]).andReturn(returnedView);

    [[self.overlayManager expect] dismissOverlay:returnedView];
    [self.uut dismissOverlay:componentId
                   commandId:@""
                  completion:^{
                  }
                   rejection:^(NSString *code, NSString *message, NSError *error){
                   }];
    [self.overlayManager verify];
}

- (void)testDismissOverlay_handleErrorIfNoOverlayExists {
    [self.uut setReadyToReceiveCommands:true];
    NSString *componentId = @"componentId";
    id errorHandlerMockClass = [OCMockObject mockForClass:[RNNErrorHandler class]];

    [[errorHandlerMockClass expect] reject:[OCMArg any]
                             withErrorCode:1010
                          errorDescription:[OCMArg any]];
    [self.uut dismissOverlay:componentId
                   commandId:@""
                  completion:[OCMArg any]
                   rejection:[OCMArg any]];
    [errorHandlerMockClass verify];
}

- (void)testDismissOverlay_invokeNavigationCommandEvent {
    [self.uut setReadyToReceiveCommands:true];
    NSString *componentId = @"componentId";

    OCMStub([self.layoutManager findComponentForId:componentId]).andReturn([UIViewController new]);

    [[self.eventEmmiter expect] sendOnNavigationCommandCompletion:@"dismissOverlay"
                                                        commandId:[OCMArg any]];
    [self.uut dismissOverlay:componentId
                   commandId:@""
                  completion:^{

                  }
                   rejection:^(NSString *code, NSString *message, NSError *error){
                   }];

    [self.eventEmmiter verify];
}

- (void)testSetRoot_setRootViewControllerOnMainWindow {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(self.vc1);

    [[self.mainWindow expect] setRootViewController:self.vc1];
    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    [self.mainWindow verify];
}

- (void)testSetStackRoot_resetStackWithSingleComponent {
    OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]]).andReturn(@[ self.vc2 ]);
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.layoutManager findComponentForId:@"vc1"]).andReturn(_nvc);
    self.vc2.options.animations.setStackRoot.enable = [[Bool alloc] initWithBOOL:NO];

    [self.uut setStackRoot:@"vc1"
                 commandId:@""
                  children:nil
                completion:^{

                }
                 rejection:^(NSString *code, NSString *message, NSError *error){

                 }];

    XCTAssertEqual(_nvc.viewControllers.firstObject, self.vc2);
    XCTAssertEqual(_nvc.viewControllers.count, 1);
}

- (void)testSetStackRoot_setMultipleChildren {
    NSArray *newViewControllers = @[ _vc1, _vc3 ];

    OCMStub([self.layoutManager findComponentForId:@"vc1"]).andReturn(_nvc);
    OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]])
        .andReturn(newViewControllers);
    [self.uut setReadyToReceiveCommands:true];

    _vc3.options.animations.setStackRoot.enable = [[Bool alloc] initWithBOOL:NO];
    [self.uut setStackRoot:@"vc1"
                 commandId:@""
                  children:nil
                completion:^{

                }
                 rejection:^(NSString *code, NSString *message, NSError *error){

                 }];
    XCTAssertTrue([_nvc.viewControllers isEqual:newViewControllers]);
}

- (void)testSetStackRoot_callRenderTreeAndWaitOnce {
    id vc1Mock = [OCMockObject partialMockForObject:_vc1];
    id vc2Mock = [OCMockObject partialMockForObject:_vc2];
    NSArray *newViewControllers = @[ vc1Mock, vc2Mock ];

    OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]])
        .andReturn(newViewControllers);
    [self.uut setReadyToReceiveCommands:true];
    [self.uut setStackRoot:@"vc1"
                 commandId:@""
                  children:nil
                completion:^{

                }
                 rejection:^(NSString *code, NSString *message, NSError *error){

                 }];

    [[vc1Mock expect] render];
    [[vc2Mock expect] render];
}

- (void)testSetStackRoot_waitForRender {
    _vc2.options.animations.setStackRoot.waitForRender = [[Bool alloc] initWithBOOL:YES];
    id vc1Mock = OCMPartialMock(_vc1);
    id vc2Mock = OCMPartialMock(_vc2);

    NSArray *newViewControllers = @[ vc1Mock, vc2Mock ];

    OCMStub([self.controllerFactory createChildrenLayout:[OCMArg any]])
        .andReturn(newViewControllers);

    [self.uut setReadyToReceiveCommands:true];
    [self.uut setStackRoot:@"vc1"
                 commandId:@""
                  children:nil
                completion:^{

                }
                 rejection:^(NSString *code, NSString *message, NSError *error){

                 }];

    [[vc1Mock expect] render];
    [[vc2Mock expect] render];
}

- (void)testSetRoot_waitForRenderTrue {
    [self.uut setReadyToReceiveCommands:true];
    self.vc1.options = [RNNNavigationOptions emptyOptions];
    self.vc1.options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:YES];

    id mockedVC = [OCMockObject partialMockForObject:self.vc1];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);

    [[mockedVC expect] render];
    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    [mockedVC verify];
}

- (void)testSetRoot_waitForRenderFalse {
    [self.uut setReadyToReceiveCommands:true];
    self.vc1.options = [RNNNavigationOptions emptyOptions];
    self.vc1.options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:NO];

    id mockedVC = [OCMockObject partialMockForObject:self.vc1];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);

    [[mockedVC expect] render];
    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    [mockedVC verify];
}

- (void)testSetRoot_withBottomTabsAttachModeTogether {
    [self.uut setReadyToReceiveCommands:true];
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.bottomTabs.tabsAttachMode = [[BottomTabsAttachMode alloc] initWithValue:@"together"];

    BottomTabsBaseAttacher *attacher =
        [[[BottomTabsAttachModeFactory alloc] initWithDefaultOptions:nil] fromOptions:options];
    RNNBottomTabsController *tabBarController =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:options
                                             defaultOptions:[RNNNavigationOptions emptyOptions]
                                                  presenter:[RNNBasePresenter new]
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:_eventEmmiter
                                       childViewControllers:@[ _vc1, _vc2 ]
                                         bottomTabsAttacher:attacher];

    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(tabBarController);

    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    XCTAssertTrue(_vc1.isViewLoaded);
    XCTAssertTrue(_vc2.isViewLoaded);
    XCTAssertEqual(_vc1.reactView.tag, 1);
    XCTAssertEqual(_vc2.reactView.tag, 2);
}

- (void)testSetRoot_withBottomTabsAttachModeOnSwitchToTab {
    [self.uut setReadyToReceiveCommands:true];
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.bottomTabs.tabsAttachMode =
        [[BottomTabsAttachMode alloc] initWithValue:@"onSwitchToTab"];
    options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:YES];

    BottomTabsBaseAttacher *attacher =
        [[[BottomTabsAttachModeFactory alloc] initWithDefaultOptions:nil] fromOptions:options];
    RNNBottomTabsController *tabBarController =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:options
                                             defaultOptions:[RNNNavigationOptions emptyOptions]
                                                  presenter:[RNNBasePresenter new]
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:_eventEmmiter
                                       childViewControllers:@[ _vc1, _vc2 ]
                                         bottomTabsAttacher:attacher];
    [tabBarController viewWillAppear:YES];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(tabBarController);

    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    XCTAssertTrue(_vc1.isViewLoaded);
    XCTAssertFalse(_vc2.isViewLoaded);
    [tabBarController setSelectedIndex:1];
    XCTAssertTrue(_vc2.isViewLoaded);
}

- (void)testSetRoot_withBottomTabsAttachModeAfterInitialTab {
    [self.uut setReadyToReceiveCommands:true];
    RNNNavigationOptions *options = [RNNNavigationOptions emptyOptions];
    options.bottomTabs.tabsAttachMode =
        [[BottomTabsAttachMode alloc] initWithValue:@"afterInitialTab"];
    options.animations.setRoot.waitForRender = [[Bool alloc] initWithBOOL:YES];

    BottomTabsBaseAttacher *attacher =
        [[[BottomTabsAttachModeFactory alloc] initWithDefaultOptions:nil] fromOptions:options];
    RNNBottomTabsController *tabBarController =
        [[RNNBottomTabsController alloc] initWithLayoutInfo:nil
                                                    creator:nil
                                                    options:options
                                             defaultOptions:[RNNNavigationOptions emptyOptions]
                                                  presenter:[RNNBasePresenter new]
                                         bottomTabPresenter:nil
                                      dotIndicatorPresenter:nil
                                               eventEmitter:_eventEmmiter
                                       childViewControllers:@[ _vc1, _vc2 ]
                                         bottomTabsAttacher:attacher];
    [tabBarController viewWillAppear:YES];

    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(tabBarController);

    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing Async Method"];
    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId) {
             XCTAssertFalse(self->_vc2.isViewLoaded);
             [expectation fulfill];
           }];

    [self waitForExpectationsWithTimeout:1 handler:nil];
    XCTAssertTrue(_vc1.isViewLoaded);
    XCTAssertTrue(_vc2.isViewLoaded);
}

- (void)testSetRoot_withAnimation {
    [self.uut setReadyToReceiveCommands:true];

    RNNNavigationOptions *stackOptions = [RNNNavigationOptions emptyOptions];
    stackOptions.animations.setRoot.alpha.duration = [TimeInterval withValue:500];

    RNNStackController *stack = [[RNNStackController alloc]
          initWithLayoutInfo:nil
                     creator:nil
                     options:stackOptions
              defaultOptions:nil
                   presenter:nil
                eventEmitter:nil
        childViewControllers:@[ [RNNComponentViewController createWithComponentId:@"first"
                                                                   initialOptions:nil] ]];

    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(stack);

    [(RNNSetRootAnimator *)[self.setRootAnimator expect] animate:_mainWindow
                                                        duration:0.5
                                                      completion:[OCMArg any]];
    [self.uut setRoot:@{}
            commandId:@""
           completion:^(NSString *componentId){
           }];
    [_setRootAnimator verify];
}

- (void)testMergeOptions_shouldMergeWithChildOnly {
    [self.uut setReadyToReceiveCommands:true];
    NSDictionary *mergeOptions = @{@"bottomTab" : @{@"badge" : @"Badge"}};

    RNNNavigationOptions *firstChildOptions = [RNNNavigationOptions emptyOptions];
    firstChildOptions.bottomTab.text = [Text withValue:@"First"];
    RNNNavigationOptions *secondChildOptions = [RNNNavigationOptions emptyOptions];
    secondChildOptions.bottomTab.text = [Text withValue:@"Second"];

    RNNComponentViewController *firstChild =
        [RNNComponentViewController createWithComponentId:@"first"
                                           initialOptions:firstChildOptions];
    RNNComponentViewController *secondChild =
        [RNNComponentViewController createWithComponentId:@"second"
                                           initialOptions:secondChildOptions];

    RNNBottomTabsController *tabBarController = [[RNNBottomTabsController alloc]
           initWithLayoutInfo:nil
                      creator:nil
                      options:[RNNNavigationOptions emptyOptions]
               defaultOptions:[RNNNavigationOptions emptyOptions]
                    presenter:[RNNBasePresenter new]
           bottomTabPresenter:[BottomTabPresenterCreator
                                  createWithDefaultOptions:[RNNNavigationOptions emptyOptions]]
        dotIndicatorPresenter:nil
                 eventEmitter:_eventEmmiter
         childViewControllers:@[ firstChild, secondChild ]
           bottomTabsAttacher:nil];
    [tabBarController viewWillAppear:YES];

    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(tabBarController);
    [self.mainWindow setRootViewController:tabBarController];
    [secondChild viewWillAppear:YES];

    [self.uut mergeOptions:secondChild.layoutInfo.componentId
                   options:mergeOptions
                completion:^{

                }];

    XCTAssertTrue([secondChild.tabBarItem.badgeValue isEqualToString:@"Badge"]);
    XCTAssertNil(firstChild.tabBarItem.badgeValue);
    XCTAssertTrue([firstChild.tabBarItem.title isEqualToString:@"First"]);
    XCTAssertTrue([secondChild.tabBarItem.title isEqualToString:@"Second"]);
}

- (void)testMergeOptions_shouldResolveTreeOptions {
    [self.uut setReadyToReceiveCommands:true];
    NSDictionary *mergeOptions = @{@"bottomTab" : @{@"badge" : @"Badge"}};

    RNNNavigationOptions *firstChildOptions = [RNNNavigationOptions emptyOptions];

    RNNNavigationOptions *secondChildOptions = [RNNNavigationOptions emptyOptions];
    secondChildOptions.bottomTab.text = [Text withValue:@"Second"];
    RNNNavigationOptions *stackOptions = [RNNNavigationOptions emptyOptions];
    stackOptions.bottomTab.text = [Text withValue:@"First"];

    RNNComponentViewController *firstChild =
        [RNNComponentViewController createWithComponentId:@"first"
                                           initialOptions:firstChildOptions];
    RNNStackController *stack = [[RNNStackController alloc] initWithLayoutInfo:nil
                                                                       creator:nil
                                                                       options:stackOptions
                                                                defaultOptions:nil
                                                                     presenter:nil
                                                                  eventEmitter:nil
                                                          childViewControllers:@[ firstChild ]];
    RNNComponentViewController *secondChild =
        [RNNComponentViewController createWithComponentId:@"second"
                                           initialOptions:secondChildOptions];

    RNNBottomTabsController *tabBarController = [[RNNBottomTabsController alloc]
           initWithLayoutInfo:nil
                      creator:nil
                      options:[RNNNavigationOptions emptyOptions]
               defaultOptions:[RNNNavigationOptions emptyOptions]
                    presenter:[RNNBasePresenter new]
           bottomTabPresenter:[BottomTabPresenterCreator
                                  createWithDefaultOptions:[RNNNavigationOptions emptyOptions]]
        dotIndicatorPresenter:nil
                 eventEmitter:_eventEmmiter
         childViewControllers:@[ stack, secondChild ]
           bottomTabsAttacher:nil];
    [tabBarController viewWillAppear:YES];

    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(tabBarController);
    [self.mainWindow setRootViewController:tabBarController];
    [secondChild viewWillAppear:YES];

    [self.uut mergeOptions:firstChild.layoutInfo.componentId
                   options:mergeOptions
                completion:^{

                }];

    XCTAssertTrue([stack.tabBarItem.badgeValue isEqualToString:@"Badge"]);
    XCTAssertTrue([stack.tabBarItem.title isEqualToString:@"First"]);
    XCTAssertTrue([secondChild.tabBarItem.title isEqualToString:@"Second"]);
}

- (void)testShowModal_shouldShowAnimated {
    [self.uut setReadyToReceiveCommands:true];
    self.vc1.options = [RNNNavigationOptions emptyOptions];
    self.vc1.options.animations.showModal.enter.enable = [[Bool alloc] initWithBOOL:YES];

    id mockedVC = [OCMockObject partialMockForObject:self.vc1];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(mockedVC);

    [[self.modalManager expect] showModal:mockedVC animated:YES completion:[OCMArg any]];
    [self.uut showModal:@{}
              commandId:@"showModal"
             completion:^(NSString *componentId){

             }];
    [self.modalManager verify];
}

- (void)testPop_shouldRejectPromiseForInvalidComponentId {
    [self.uut setReadyToReceiveCommands:true];

    XCTestExpectation *expectation =
        [self expectationWithDescription:@"Should invoke reject block"];

    [self.uut pop:@"invalidComponentId"
        commandId:@"pop"
        mergeOptions:nil
        completion:^{
          [expectation fulfill];
        }
        rejection:^(NSString *code, NSString *message, NSError *error) {
          XCTAssert([code isEqualToString:@"1012"]);
          XCTAssert([message
              isEqualToString:
                  @"Popping component failed - componentId 'invalidComponentId' not found"]);
          [expectation fulfill];
        }];

    [self waitForExpectationsWithTimeout:5 handler:nil];
}

- (void)testSetDefaultOptions_shouldNotThrowWhenBridgeNotReady {
    [self.uut setReadyToReceiveCommands:false];
    [self.uut setDefaultOptions:@{}
                     completion:^{
                     }];
}

- (void)testDismissModal_shouldResolveTopMostComponentId {
    [self.uut setReadyToReceiveCommands:true];
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";

    RNNComponentViewController *child = [OCMockObject
        partialMockForObject:[RNNComponentViewController createWithComponentId:@"child"]];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    OCMStub([self.modalManager dismissModal:OCMArg.any animated:NO completion:OCMArg.invokeBlock]);
    OCMStub(child.isModal).andReturn(YES);
    OCMStub([self.layoutManager findComponentForId:@"child"]).andReturn(child);

    [self.uut dismissModal:@"child"
                 commandId:@"commandId"
              mergeOptions:nil
                completion:^(NSString *_Nonnull componentId) {
                  XCTAssertTrue([componentId isEqualToString:@"stack"]);
                }
                 rejection:^(NSString *_Nonnull code, NSString *_Nonnull message,
                             NSError *_Nullable error){
                 }];
}

- (void)testDismissModal_shouldMergeOptions {
    [self.uut setReadyToReceiveCommands:true];
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";

    RNNComponentViewController *child = [OCMockObject
        partialMockForObject:[RNNComponentViewController createWithComponentId:@"child"]];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ child ]];

    OCMStub(child.isModal).andReturn(YES);
    OCMStub([self.layoutManager findComponentForId:@"child"]).andReturn(child);

    [[self.modalManager expect]
        dismissModal:[OCMArg checkWithBlock:^BOOL(UIViewController *modalToDismiss) {
          return modalToDismiss.options.animations.dismissModal.exit.enable.get == NO;
        }]
            animated:NO
          completion:OCMArg.any];

    [self.uut dismissModal:@"child"
                 commandId:@"commandId"
              mergeOptions:@{@"animations" : @{@"dismissModal" : @{@"exit" : @{@"enabled" : @(0)}}}}
                completion:^(NSString *_Nonnull componentId) {
                  XCTAssertTrue([componentId isEqualToString:@"stack"]);
                }
                 rejection:^(NSString *_Nonnull code, NSString *_Nonnull message,
                             NSError *_Nullable error){
                 }];

    [self.modalManager verify];
}

- (void)testShowModal_withPresentationStyle {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(_vc1);
    _vc1.options = [RNNNavigationOptions emptyOptions];
    _vc1.options.modalPresentationStyle = [Text withValue:@"overCurrentContext"];
    [self.uut showModal:@{} commandId:@"" completion:nil];
    XCTAssertEqual(_vc1.modalPresentationStyle, UIModalPresentationOverCurrentContext);
}

- (void)testApplyOptionsOnInit_shouldShowModalWithTransitionStyle {
    [self.uut setReadyToReceiveCommands:true];
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(_vc1);
    _vc1.options = [RNNNavigationOptions emptyOptions];
    _vc1.options.modalTransitionStyle = [Text withValue:@"crossDissolve"];
    [self.uut showModal:@{} commandId:@"" completion:nil];
    XCTAssertEqual(_vc1.modalTransitionStyle, UIModalTransitionStyleCrossDissolve);
}

- (void)testPush_shouldResolvePromiseAndSendCommandCompletionWithPushedComponentId {
    [self.uut setReadyToReceiveCommands:true];
    NSString *expectedComponentId = @"pushedComponent";
    RNNLayoutInfo *stackLayoutInfo = [RNNLayoutInfo new];
    stackLayoutInfo.componentId = @"stack";
    RNNComponentViewController *currentComponent =
        [RNNComponentViewController createWithComponentId:@"currentComponent"];
    RNNComponentViewController *pushedComponent =
        [RNNComponentViewController createWithComponentId:expectedComponentId];
    __unused RNNStackController *stack =
        [[RNNStackController alloc] initWithLayoutInfo:stackLayoutInfo
                                               creator:nil
                                               options:nil
                                        defaultOptions:nil
                                             presenter:nil
                                          eventEmitter:nil
                                  childViewControllers:@[ currentComponent ]];

    OCMStub([self.layoutManager findComponentForId:@"currentComponent"])
        .andReturn(currentComponent);
    OCMStub([self.controllerFactory createLayout:[OCMArg any]]).andReturn(pushedComponent);

    [[self.eventEmmiter expect] sendOnNavigationCommandCompletion:@"push"
                                                        commandId:@"pushCommandId"];

    XCTestExpectation *exp = [self expectationWithDescription:@"wait for animation to end"];
    [self.uut push:@"currentComponent"
         commandId:@"pushCommandId"
            layout:nil
        completion:^(NSString *_Nonnull componentId) {
          [self.eventEmmiter verify];
          XCTAssertTrue([componentId isEqualToString:expectedComponentId]);
          [exp fulfill];
        }
         rejection:^(NSString *code, NSString *message, NSError *error){

         }];

    [self waitForExpectationsWithTimeout:40 handler:nil];
}

@end
