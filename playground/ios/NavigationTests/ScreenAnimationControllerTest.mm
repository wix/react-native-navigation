#import "ScreenAnimationController.h"
#import <OCMock/OCMock.h>
#import <XCTest/XCTest.h>

#ifdef RCT_NEW_ARCH_ENABLED

@interface RNNScreenAnimationSurfacePresenterSpy : NSObject

@property(nonatomic, assign) NSUInteger addObserverCount;
@property(nonatomic, assign) NSUInteger removeObserverCount;

@end

@implementation RNNScreenAnimationSurfacePresenterSpy

- (void)addObserver:(id<RCTSurfacePresenterObserver>)observer {
    self.addObserverCount++;
}

- (void)removeObserver:(id<RCTSurfacePresenterObserver>)observer {
    self.removeObserverCount++;
}

@end

@interface RNNScreenAnimationHostSpy : NSObject

@property(nonatomic, strong) RNNScreenAnimationSurfacePresenterSpy *surfacePresenter;

@end

@implementation RNNScreenAnimationHostSpy

@end

@interface ScreenAnimationControllerTest : XCTestCase

@end

@implementation ScreenAnimationControllerTest

- (void)testAnimationEndRemovesMountObserver {
    RNNScreenAnimationSurfacePresenterSpy *surfacePresenter =
        [RNNScreenAnimationSurfacePresenterSpy new];
    RNNScreenAnimationHostSpy *host = [RNNScreenAnimationHostSpy new];
    host.surfacePresenter = surfacePresenter;
    ScreenAnimationController *controller =
        [[ScreenAnimationController alloc] initWithContentTransition:nil
                                                  elementTransitions:nil
                                            sharedElementTransitions:nil
                                                            duration:0
                                                                host:(RCTHost *)host];

    UIViewController *fromViewController = [UIViewController new];
    UIViewController *toViewController = [UIViewController new];
    UINavigationController *navigationController =
        [[UINavigationController alloc] initWithRootViewController:fromViewController];
    XCTAssertNotNil(navigationController);

    id transitionContext = OCMProtocolMock(@protocol(UIViewControllerContextTransitioning));
    OCMStub([transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey])
        .andReturn(fromViewController);
    OCMStub([transitionContext viewControllerForKey:UITransitionContextToViewControllerKey])
        .andReturn(toViewController);
    OCMStub([transitionContext viewForKey:UITransitionContextFromViewKey]).andReturn(nil);
    OCMStub([transitionContext viewForKey:UITransitionContextToViewKey]).andReturn(nil);

    [controller animateTransition:transitionContext];
    [controller animateTransition:transitionContext];
    XCTAssertEqual(surfacePresenter.addObserverCount, 1);

    [controller animationEnded:NO];
    [controller animationEnded:NO];
    XCTAssertEqual(surfacePresenter.removeObserverCount, 1);
}

@end

#endif
