#import "UIImage+tint.h"

@implementation UIImage (tint)

- (UIImage *)withTintColor:(UIColor *)color {
	UIImage *newImage = [self imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 130000
	if (@available(iOS 13.0, *)) {
		return [newImage imageWithTintColor:color];
	}
#endif
	UIGraphicsBeginImageContextWithOptions(self.size, NO, newImage.scale);
	[color set];
	[newImage drawInRect:CGRectMake(0, 0, self.size.width, newImage.size.height)];
	newImage = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	return newImage;
}

@end
