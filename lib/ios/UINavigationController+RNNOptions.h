#import <UIKit/UIKit.h>

@interface UINavigationController (RNNOptions)

- (void)setRootBackgroundImage:(UIImage *)backgroundImage;

- (void)setNavigationBarTestId:(NSString *)testID;

- (void)setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated;

#if !TARGET_OS_TV
- (void)setBarStyle:(UIBarStyle)barStyle;
- (void)hideBarsOnScroll:(BOOL)hideOnScroll;

#endif
- (void)setNavigationBarBlur:(BOOL)blur;

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds;

@end
