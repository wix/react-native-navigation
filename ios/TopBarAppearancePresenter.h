#import "TopBarPresenter.h"
#import <UIKit/UIKit.h>

API_AVAILABLE(ios(13.0))
@interface TopBarAppearancePresenter : TopBarPresenter

@property(nonatomic, strong) UIColor *borderColor;
@property(nonatomic) BOOL showBorder;
@property(nonatomic, strong) UIColor *scrollEdgeBorderColor;
@property(nonatomic) BOOL showScrollEdgeBorder;

+ (void)applyBackgroundToNavigationItem:(UINavigationItem *)item
                                  color:(UIColor *)color
                             transparent:(BOOL)transparent
                              translucent:(BOOL)translucent API_AVAILABLE(ios(13.0));

+ (void)applySharedBackgroundSettingsForNavigationItem:(UINavigationItem *)item
    API_AVAILABLE(ios(26.0));

+ (void)suppressSharedBackgroundForNavigationItem:(UINavigationItem *)item API_AVAILABLE(ios(26.0));

+ (void)flushDeferredBarButtonLayoutsForNavigationItem:(UINavigationItem *)item
    API_AVAILABLE(ios(26.0));

+ (void)relayoutBarContentForNavigationItem:(UINavigationItem *)item
                              navigationBar:(UINavigationBar *)navigationBar API_AVAILABLE(ios(26.0));

- (void)applyTransitionBackgroundToNavigationItem:(UINavigationItem *)item
                                      topBarOptions:(RNNTopBarOptions *)options API_AVAILABLE(ios(26.0));

- (void)finalizeNavigationBarAfterTransitionForViewController:(UIViewController *)viewController
    API_AVAILABLE(ios(26.0));

@end
