#import <UIKit/UIKit.h>

@interface UINavigationController (RNNOptions)

- (void)setInteractivePopGestureEnabled:(BOOL)enabled;

- (void)setRootBackgroundImage:(UIImage *)backgroundImage;

- (void)setNavigationBarTestId:(NSString *)testID;

- (void)setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)hideBarsOnScroll:(BOOL)hideOnScroll;

- (void)setNavigationBarNoBorder:(BOOL)noBorder;

- (void)setBarStyle:(UIBarStyle)barStyle;

- (void)setNavigationBarFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color;

- (void)setNavigationBarTranslucent:(BOOL)translucent;

- (void)setNavigationBarBlur:(BOOL)blur;

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds;

- (void)setNavigationBarLargeTitleVisible:(BOOL)visible;

- (void)setNavigationBarLargeTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color;

- (void)setBackButtonColor:(UIColor *)color;

@end
