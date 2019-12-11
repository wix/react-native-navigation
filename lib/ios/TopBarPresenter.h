#import "RNNBasePresenter.h"
#import "RNNTopBarOptions.h"

@interface TopBarPresenter : RNNBasePresenter

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options;

- (void)mergeOptions:(RNNTopBarOptions *)options defaultOptions:(RNNTopBarOptions *)defaultOptions;

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController;

@property (nonatomic) BOOL transparent;
@property (nonatomic) BOOL translucent;
@property (nonatomic, strong) UIColor* backgroundColor;

@end
