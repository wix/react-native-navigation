
#import "RNNExternalComponentStore.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface RNNExternalComponentStoreTest : XCTestCase

@property(nonatomic, strong) RNNExternalComponentStore *store;

@end

@implementation RNNExternalComponentStoreTest

- (void)setUp {
    [super setUp];

    self.store = [RNNExternalComponentStore new];
}

- (void)testGetExternalComponentShouldRetrunSavedComponent {
    UIViewController *testVC = [UIViewController new];
    RNNLayoutInfo *layoutInfo = [[RNNLayoutInfo alloc] init];
    layoutInfo.name = @"extId1";
#ifdef RCT_NEW_ARCH_ENABLED
    [self.store
        registerExternalHostComponent:layoutInfo.name
                             callback:^UIViewController *(NSDictionary *props, RCTHost *host) {
                               return testVC;
                             }];

    UIViewController *savedComponent = [self.store getExternalHostComponent:layoutInfo host:nil];
#else
    [self.store
        registerExternalComponent:layoutInfo.name
                         callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
                           return testVC;
                         }];

    UIViewController *savedComponent = [self.store getExternalComponent:layoutInfo bridge:nil];
#endif
    XCTAssertEqual(testVC, savedComponent);
}

@end
