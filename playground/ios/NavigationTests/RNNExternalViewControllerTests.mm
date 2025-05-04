#import "RNNCustomViewController.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNExternalViewController.h>
#import <XCTest/XCTest.h>

@interface RNNExternalViewControllerTest : XCTestCase

@property(nonatomic, strong) RNNExternalViewController *uut;
@property(nonatomic, strong) RNNCustomViewController *customViewController;
@property(nonatomic, strong) id mockEventEmitter;
@property(nonatomic, strong) RNNLayoutInfo *layoutInfo;

@end

@implementation RNNExternalViewControllerTest

- (void)setUp {
    [super setUp];
    self.customViewController = [[RNNCustomViewController alloc] init];
    self.mockEventEmitter = [OCMockObject niceMockForClass:RNNEventEmitter.class];
    _layoutInfo = [[RNNLayoutInfo alloc] init];
    _layoutInfo.componentId = @"externalComponentId";
    _layoutInfo.name = @"externalComponentName";
    RNNComponentPresenter *presenter =
        [[RNNComponentPresenter alloc] initWithComponentRegistry:nil defaultOptions:nil];
    self.uut =
        [[RNNExternalViewController alloc] initWithLayoutInfo:_layoutInfo
                                                 eventEmitter:self.mockEventEmitter
                                                    presenter:presenter
                                                      options:[RNNNavigationOptions emptyOptions]
                                               defaultOptions:nil
                                               viewController:self.customViewController];
}

- (void)testLoadView_withMainScreenBounds {
    XCTAssertTrue(CGRectEqualToRect(self.uut.view.bounds, UIScreen.mainScreen.bounds));
}

- (void)testViewDidAppear_shouldEmitEvent {
    [[_mockEventEmitter expect] sendComponentDidAppear:_layoutInfo.componentId
                                         componentName:_layoutInfo.name
                                         componentType:ComponentTypeScreen];
    [self.uut viewDidAppear:NO];
    [_mockEventEmitter verify];
}

- (void)testViewDidDisappear_shouldEmitEvent {
    [[_mockEventEmitter expect] sendComponentDidDisappear:_layoutInfo.componentId
                                            componentName:_layoutInfo.name
                                            componentType:ComponentTypeScreen];
    [self.uut viewDidDisappear:NO];
    [_mockEventEmitter verify];
}

@end
