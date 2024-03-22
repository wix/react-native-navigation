#import <UIKit/UIKit.h>

@interface UIImage (Utils)

- (UIImage *)withTintColor:(UIColor *)color;

+ (UIImage *)imageWithSize:(CGSize)size color:(UIColor *)color;

- (UIImage *)imageWithInsets:(UIEdgeInsets)insets;

@end
