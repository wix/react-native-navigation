#import "UIImage+tint.h"

@implementation UIImage (tint)

- (UIImage *)withTintColor:(UIColor *)color {
	UIGraphicsBeginImageContext(self.size);
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	CGContextTranslateCTM(context, 0, self.size.height);
	CGContextScaleCTM(context, 1.0, -1.0);
	
	CGRect rect = CGRectMake(0, 0, self.size.width, self.size.height);
	
	// draw alpha-mask
	CGContextSetBlendMode(context, kCGBlendModeNormal);
	CGContextDrawImage(context, rect, self.CGImage);
	
	// draw tint color, preserving alpha values of original image
	CGContextSetBlendMode(context, kCGBlendModeSourceIn);
	[color setFill];
	CGContextFillRect(context, rect);
	
	UIImage *coloredImage = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	return coloredImage;
}

@end
