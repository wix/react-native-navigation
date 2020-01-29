#import "UIImage+insets.h"

@implementation UIImage (insets)

- (UIImage *)imageWithInsets:(UIEdgeInsets)insets {
	UIGraphicsBeginImageContextWithOptions(CGSizeMake(self.size.width + insets.left + insets.right,
													  self.size.height + insets.top + insets.bottom), false, self.scale);
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
