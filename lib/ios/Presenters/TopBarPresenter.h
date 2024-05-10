#import "RNNBasePresenter.h"
#import "RNNTopBarOptions.h"

@interface TopBarPresenter : RNNBasePresenter

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options;

- (void)mergeOptions:(RNNTopBarOptions *)options withDefault:(RNNTopBarOptions *)defaultOptions;

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController;

- (BOOL)transparent;
- (BOOL)scrollEdgeTransparent;

- (void)setBackButtonOptions:(RNNBackButtonOptions *)backButtonOptions;

- (void)showBorder:(BOOL)showBorder;

@property(nonatomic) BOOL translucent;
@property(nonatomic) BOOL scrollEdgeTranslucent;
@property(nonatomic, strong) UIColor *backgroundColor;
@property(nonatomic, strong) UIColor *scrollEdgeAppearanceColor;

@end
