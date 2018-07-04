#import "UIImage+tint.h"

@implementation UIImage (tint)

- (UIImage *)withTintColor:(UIColor *)color {
	UIImage *newImage = [self imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
	UIGraphicsBeginImageContextWithOptions(self.size, NO, newImage.scale);
	[color set];
	[newImage drawInRect:CGRectMake(0, 0, self.size.width, newImage.size.height)];
	newImage = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	return newImage;
}

@end
