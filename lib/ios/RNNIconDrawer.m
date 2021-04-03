#import "RNNIconDrawer.h"

@implementation RNNIconDrawer

- (UIImage *)draw:(UIImage *)image
         imageColor:(UIColor *)imageColor
    backgroundColor:(UIColor *)color
               size:(CGSize)size
       cornerRadius:(CGFloat)cornerRadius {
    UIGraphicsBeginImageContextWithOptions(size, NO, image.scale);
    CGRect rect = CGRectMake(0, 0, size.width, size.height);
    CGRect imageRect =
        CGRectMake((size.width - image.size.width) / 2, (size.height - image.size.height) / 2,
                   image.size.width, image.size.height);
    UIBezierPath *path = [UIBezierPath bezierPathWithRoundedRect:rect cornerRadius:cornerRadius];
    [color setFill];

    [path fill];

    if (imageColor) {
        UIImage *templateImage = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
        [imageColor set];
        [templateImage drawInRect:imageRect];
    } else {
        UIImage *originalImage = [image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        [originalImage drawInRect:imageRect];
    }

    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();

    return [newImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
}

@end
