#import "RNNBasePresenter.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"

@interface RNNStackPresenter : RNNBasePresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

- (BOOL)shouldPopItem:(UINavigationItem *)item options:(RNNNavigationOptions *)options;

@end
