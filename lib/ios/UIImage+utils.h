#import <UIKit/UIKit.h>

@interface UIImage (utils)

- (UIImage *)withTintColor:(UIColor *)color;

+ (UIImage *)imageWithSize:(CGSize)size color:(UIColor *)color;

- (UIImage *)imageWithInsets:(UIEdgeInsets)insets;

@end
