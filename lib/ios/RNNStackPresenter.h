#import "RNNBasePresenter.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNStackPresenter : RNNBasePresenter

@property(nonatomic, strong) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry :(RNNNavigationOptions *)defaultOptions;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
