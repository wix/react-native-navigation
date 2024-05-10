#import <UIKit/UIKit.h>

@interface UINavigationController (RNNOptions)

- (void)setRootBackgroundImage:(UIImage *)backgroundImage;

- (void)setNavigationBarTestId:(NSString *)testID;

- (void)setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)hideBarsOnScroll:(BOOL)hideOnScroll;

- (void)setBarStyle:(UIBarStyle)barStyle;

- (void)setNavigationBarBlur:(BOOL)blur;

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds;

- (void)setBackButtonTestID:(NSString *)testID;

@end
