#import "RNNBasePresenter.h"
#import "RNNTopBarOptions.h"

@interface TopBarPresenter : RNNBasePresenter

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController;

- (void)showBorder:(BOOL)showBorder;

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color;

- (void)setBackButtonIcon:(UIImage *)icon withColor:(UIColor *)color title:(NSString *)title showTitle:(BOOL)showTitle;

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions;

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions;

@property (nonatomic) BOOL transparent;
@property (nonatomic) BOOL translucent;
@property (nonatomic, strong) UIColor* backgroundColor;

@end
