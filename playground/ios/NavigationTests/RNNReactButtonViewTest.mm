#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNReactButtonView.h>
#import <XCTest/XCTest.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#import <React/RCTFabricSurface.h>
#import <React/RCTSurfacePresenter.h>

@interface RNNSurfacePresenterSpy : NSObject
@property(nonatomic) NSInteger addCount;
@end

@implementation RNNSurfacePresenterSpy

- (void)addObserver:(__unused id<RCTSurfacePresenterObserver>)observer {
    self.addCount += 1;
}

- (void)removeObserver:(__unused id<RCTSurfacePresenterObserver>)observer {
}

@end
#endif

@interface RNNReactButtonViewTest : XCTestCase
@end

@implementation RNNReactButtonViewTest

#ifdef RCT_NEW_ARCH_ENABLED
- (void)testRegistersSurfacePresenterObserverOnce {
    id host = OCMClassMock([RCTHost class]);
    id surface = OCMClassMock([RCTFabricSurface class]);
    RNNSurfacePresenterSpy *surfacePresenter = [RNNSurfacePresenterSpy new];
    OCMStub([host createSurfaceWithModuleName:@"Button" initialProperties:@{}]).andReturn(surface);
    OCMStub([host surfacePresenter]).andReturn(surfacePresenter);

    RNNReactButtonView *view = [[RNNReactButtonView alloc]
        initWithHost:host
           moduleName:@"Button"
    initialProperties:@{}
         eventEmitter:nil
      sizeMeasureMode:RCTSurfaceSizeMeasureModeWidthUndefined |
                      RCTSurfaceSizeMeasureModeHeightUndefined
  reactViewReadyBlock:nil];

    XCTAssertNotNil(view);
    XCTAssertEqual(surfacePresenter.addCount, 1);
}

#endif

@end
