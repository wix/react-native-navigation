#import "UIImage+utils.h"

@implementation UIImage (utils)

- (UIImage *)withTintColor:(UIColor *)color {
    if (@available(iOS 13.0, *)) {
        return [self imageWithTintColor:color renderingMode:UIImageRenderingModeAlwaysTemplate];
    }

    UIImage *newImage = [self imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    UIGraphicsBeginImageContextWithOptions(self.size, NO, newImage.scale);
    [color set];
    [newImage drawInRect:CGRectMake(0, 0, self.size.width, newImage.size.height)];
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    return newImage;
}

+ (UIImage *)imageWithSize:(CGSize)size color:(UIColor *)color {
    UIGraphicsBeginImageContext(size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, color.CGColor);
    CGContextFillRect(context, CGRectMake(0, 0, size.width, size.height));
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

- (UIImage *)imageWithInsets:(UIEdgeInsets)insets {
    UIGraphicsBeginImageContextWithOptions(
        CGSizeMake(self.size.width + insets.left + insets.right,
                   self.size.height + insets.top + insets.bottom),
        false, self.scale);
    CGContextRef context = UIGraphicsGetCurrentContext();
    UIGraphicsPushContext(context);

    CGPoint origin = CGPointMake(insets.left, insets.top);
    [self drawAtPoint:origin];

    UIGraphicsPopContext();
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}

@end
