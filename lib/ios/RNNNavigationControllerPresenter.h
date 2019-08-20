#import "RNNBasePresenter.h"
#import "RNNRootViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property(nonatomic, strong) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry :(RNNNavigationOptions *)defaultOptions;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
