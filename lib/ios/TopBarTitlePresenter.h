#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"
#import "UIViewController+LayoutProtocol.h"

@interface TopBarTitlePresenter : RNNBasePresenter

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)mergeOptions:(RNNTopBarOptions *)options resolvedOptions:(RNNTopBarOptions *)resolvedOptions;

- (void)setCustomNavigationTitleView:(RNNTopBarOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
