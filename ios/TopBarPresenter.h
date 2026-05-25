#import "RNNBasePresenter.h"
#import "RNNTopBarOptions.h"
#import <UIKit/UIKit.h>

@interface TopBarPresenter : RNNBasePresenter

- (UINavigationController *)navigationController;

- (void)applyOptions:(RNNTopBarOptions *)options;

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options;

- (void)applyBackgroundForTransitionToViewController:(UIViewController *)viewController
                                       topBarOptions:(RNNTopBarOptions *)options API_AVAILABLE(ios(26.0));

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
