#import "RNNNavigationOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import <Foundation/Foundation.h>

@interface TopBarTitlePresenter : RNNBasePresenter

- (void)applyOptionsOnInit:(RNNTopBarOptions *)options;

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)mergeOptions:(RNNTopBarOptions *)options
     resolvedOptions:(RNNTopBarOptions *)resolvedOptions;

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options
                      navigationBar:(UINavigationBar *)navigationBar
                             perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (void)preCreateDeferredComponentTitleIfNeeded:(RNNTopBarOptions *)options API_AVAILABLE(ios(26.0));

- (void)attachDeferredComponentTitleIfNeeded:(RNNTopBarOptions *)options
                                navigationBar:(UINavigationBar *)navigationBar API_AVAILABLE(ios(26.0));

- (void)renderComponents:(RNNTopBarOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
