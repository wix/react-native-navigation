#import "UITabBar+utils.h"
#import "UITabBarController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

@interface UITabBarController_RNNOptionsTest : XCTestCase

@property(nonatomic, retain) UITabBarController *uut;

@end

@implementation UITabBarController_RNNOptionsTest

- (void)setUp {
    [super setUp];
    self.uut = [OCMockObject partialMockForObject:[UITabBarController new]];
    OCMStub([self.uut tabBar]).andReturn([OCMockObject partialMockForObject:[UITabBar new]]);
}

- (void)test_centerTabItems {
    [[(id)self.uut.tabBar expect] centerTabItems];
    [self.uut centerTabItems];
    [(id)self.uut.tabBar verify];
}

- (void)test_tabBarTranslucent_true {
    [self.uut setTabBarTranslucent:YES];
    XCTAssertTrue(self.uut.tabBar.translucent);
}

- (void)test_tabBarTranslucent_false {
    [self.uut setTabBarTranslucent:NO];
    XCTAssertFalse(self.uut.tabBar.translucent);
}

- (void)test_tabBarHideShadow_default {
    XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_true {
    [self.uut setTabBarHideShadow:YES];
    XCTAssertTrue(self.uut.tabBar.clipsToBounds);
}

- (void)test_tabBarHideShadow_false {
    [self.uut setTabBarHideShadow:NO];
    XCTAssertFalse(self.uut.tabBar.clipsToBounds);
}

- (void)test_hideTabBar {
    if (@available(iOS 18.0, *)) {
        [self.uut hideTabBar:NO];
        XCTAssertTrue(self.uut.tabBarHidden);
        XCTAssertTrue([self.uut rnn_isTabBarHidden]);
    }
}

- (void)test_showTabBar {
    if (@available(iOS 18.0, *)) {
        self.uut.tabBarHidden = YES;
        [self.uut showTabBar:NO];
        XCTAssertFalse(self.uut.tabBarHidden);
        XCTAssertFalse([self.uut rnn_isTabBarHidden]);
    }
}

- (void)test_rnnIsTabBarHidden_shouldUseAvailableVisibilityState {
    if (@available(iOS 18.0, *)) {
        self.uut.tabBarHidden = YES;
    } else {
        self.uut.tabBar.hidden = YES;
    }

    XCTAssertTrue([self.uut rnn_isTabBarHidden]);
}

@end
