#import <UIKit/UIKit.h>

@interface UINavigationController (RNNOptions)

- (void)setRootBackgroundImage:(UIImage *)backgroundImage;

- (void)setNavigationBarTestId:(NSString *)testID;

- (void)setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)hideBarsOnScroll:(BOOL)hideOnScroll;
#if !TARGET_OS_TV
- (void)setBarStyle:(UIBarStyle)barStyle;
#endif
- (void)setNavigationBarBlur:(BOOL)blur;

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds;

@end
