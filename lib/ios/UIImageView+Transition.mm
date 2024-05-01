#import "UIImageView+Transition.h"

@implementation UIImageView (Transition)

- (CGRect)resolveBoundsWithImageSize:(CGSize)imageSize {
    switch (self.contentMode) {
    case UIViewContentModeCenter:
        return CGRectMake(0, 0, imageSize.width, imageSize.height);
        break;
    case UIViewContentModeScaleAspectFill: {
        CGFloat widthRatio = imageSize.width / self.superview.bounds.size.width;
        CGFloat heightRatio = imageSize.height / self.superview.bounds.size.height;

        if (widthRatio > heightRatio) {
            imageSize = CGSizeMake(imageSize.width / heightRatio, imageSize.height / heightRatio);
        } else {
            imageSize = CGSizeMake(imageSize.width / widthRatio, imageSize.height / widthRatio);
        }

        return CGRectMake(0, 0, imageSize.width, imageSize.height);
    }
    case UIViewContentModeScaleAspectFit: {
        CGFloat widthRatio = imageSize.width / self.superview.bounds.size.width;
        CGFloat heightRatio = imageSize.height / self.superview.bounds.size.height;

        if (widthRatio > heightRatio) {
            imageSize = CGSizeMake(imageSize.width / widthRatio, imageSize.height / widthRatio);
        } else {
            imageSize = CGSizeMake(imageSize.width / heightRatio, imageSize.height / heightRatio);
        }

        return CGRectMake(0, 0, imageSize.width, imageSize.height);
    }
    default:
        break;
    }

    return self.bounds;
}

- (CGRect)resolveBounds {
    return [self resolveBoundsWithImageSize:CGSizeMake(self.image.size.width / self.image.scale,
                                                       self.image.size.height / self.image.scale)];
}

@end
