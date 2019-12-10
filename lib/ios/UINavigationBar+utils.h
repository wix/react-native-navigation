#import <UIKit/UIKit.h>

@interface UINavigationBar (utils)

- (void)rnn_setBackgroundColor:(UIColor *)color;

- (void)rnn_setBackIndicatorImage:(UIImage *)image;

- (void)rnn_showBorder:(BOOL)showBorder;

- (void)rnn_setNavigationBarLargeTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color;

- (void)rnn_setNavigationBarTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color;

@end
