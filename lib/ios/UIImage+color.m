#import "UIImage+color.h"

@implementation UIImage (color)

+ (UIImage *)imageWithColor:(UIColor *)color withHeight:(double) height {
	CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, height);
	UIGraphicsBeginImageContext(rect.size);
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	CGContextSetFillColorWithColor(context, [color CGColor]);
	CGContextFillRect(context, rect);
	
	UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	return image;
}

@end
