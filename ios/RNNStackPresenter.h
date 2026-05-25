#import "RNNBasePresenter.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import <UIKit/UIKit.h>

@interface RNNStackPresenter : RNNBasePresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

- (void)applyTopBarBackgroundBeforeShowingViewController:(UIViewController *)viewController
    API_AVAILABLE(ios(26.0));

- (void)applyTopBarButtonsBeforeShowingViewController:(UIViewController *)viewController
    API_AVAILABLE(ios(26.0));

- (void)applyTopBarTitleBeforeShowingViewController:(UIViewController *)viewController
                                      navigationBar:(UINavigationBar *)navigationBar API_AVAILABLE(ios(26.0));

- (void)handleIOS26NavigationBarDidShowForViewController:(UIViewController *)viewController
    API_AVAILABLE(ios(26.0));

- (BOOL)shouldPopItem:(UINavigationItem *)item options:(RNNNavigationOptions *)options;

@end
