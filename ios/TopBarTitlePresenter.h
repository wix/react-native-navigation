#import "RNNNavigationOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import <Foundation/Foundation.h>

@interface TopBarTitlePresenter : RNNBasePresenter

- (void)applyOptionsOnInit:(RNNTopBarOptions *)options;

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)mergeOptions:(RNNTopBarOptions *)options
     resolvedOptions:(RNNTopBarOptions *)resolvedOptions;

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options
                             perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
