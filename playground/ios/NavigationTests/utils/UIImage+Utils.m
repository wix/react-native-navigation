#import "UIImage+Utils.h"

@implementation UIImage (Utils)

+ (UIImage *)emptyImage {
    UIGraphicsBeginImageContext(CGSizeMake(10, 10));
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

- (BOOL)isEqual:(UIImage *)image {
    NSData *data1 = UIImagePNGRepresentation(self);
    NSData *data2 = UIImagePNGRepresentation(image);

    return [super isEqual:image] || [data1 isEqual:data2];
}

@end
