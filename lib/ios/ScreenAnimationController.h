#import "RNNScreenTransition.h"
#import <Foundation/Foundation.h>
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerObserverCoordinator.h>
#import <React/RCTUIManagerUtils.h>

#ifdef RCT_NEW_ARCH_ENABLED

#ifdef USE_FRAMEWORKS_STATIC
#import <React_RuntimeApple/ReactCommon/RCTHost.h>
#else
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#endif

#import <React/RCTSurfacePresenterStub.h>
#endif


@interface ScreenAnimationController
	: NSObject <UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning,
#ifdef RCT_NEW_ARCH_ENABLED
RCTSurfacePresenterObserver,
#endif
RCTUIManagerObserver>

- (instancetype)initWithContentTransition:(RNNEnterExitAnimation *)contentTransition
					   elementTransitions:(NSArray<ElementTransitionOptions *> *)elementTransitions
				 sharedElementTransitions:
					 (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
								 duration:(CGFloat)duration
								   bridge:(RCTBridge *)bridge;

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithContentTransition:(RNNEnterExitAnimation *)contentTransition
					   elementTransitions:(NSArray<ElementTransitionOptions *> *)elementTransitions
				 sharedElementTransitions:
					 (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
								 duration:(CGFloat)duration
								   host:(RCTHost *)host;
#endif

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
								toVC:(UIViewController *)toVC
					   containerView:(UIView *)containerView;

@property(nonatomic, strong) RNNEnterExitAnimation *content;
@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<SharedElementTransitionOptions *> *sharedElementTransitions;

@end
