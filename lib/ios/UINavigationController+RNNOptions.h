#import <UIKit/UIKit.h>

@interface UINavigationController (RNNOptions)

- (void)rnn_setInteractivePopGestureEnabled:(BOOL)enabled;

- (void)rnn_setRootBackgroundImage:(UIImage *)backgroundImage;

- (void)rnn_setNavigationBarTestID:(NSString *)testID;

- (void)rnn_setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)rnn_hideBarsOnScroll:(BOOL)hideOnScroll;

- (void)rnn_setNavigationBarNoBorder:(BOOL)noBorder;

- (void)rnn_setBarStyle:(UIBarStyle)barStyle;

- (void)rnn_setNavigationBarFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color;

- (void)rnn_setNavigationBarTranslucent:(BOOL)translucent;

- (void)rnn_setNavigationBarBlur:(BOOL)blur;

- (void)rnn_setNavigationBarClipsToBounds:(BOOL)clipsToBounds;

@end
