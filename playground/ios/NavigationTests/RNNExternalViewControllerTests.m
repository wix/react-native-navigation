#import "RNNCustomViewController.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNExternalViewController.h>
#import <XCTest/XCTest.h>

@interface RNNExternalViewControllerTest : XCTestCase

@property(nonatomic, strong) RNNExternalViewController *uut;
@property(nonatomic, strong) RNNCustomViewController *customViewController;

@end

@implementation RNNExternalViewControllerTest

- (void)setUp {
    [super setUp];
    self.customViewController = [[RNNCustomViewController alloc] init];
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.componentId = @"externalComponentId";
    RNNComponentPresenter *presenter =
        [[RNNComponentPresenter alloc] initWithComponentRegistry:nil defaultOptions:nil];
    self.uut = [[RNNExternalViewController alloc]
        initWithLayoutInfo:layoutInfo
              eventEmitter:nil
                 presenter:presenter
                   options:[[RNNNavigationOptions alloc] initEmptyOptions]
            defaultOptions:nil
            viewController:self.customViewController];
}

- (void)testLoadView_withMainScreenBounds {
    XCTAssertTrue(CGRectEqualToRect(self.uut.view.bounds, UIScreen.mainScreen.bounds));
}

@end
