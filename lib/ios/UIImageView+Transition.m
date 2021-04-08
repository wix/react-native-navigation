#import "UIImageView+Transition.h"

@implementation UIImageView (Transition)

- (CGRect)resolveBounds {
    switch (self.contentMode) {
    case UIViewContentModeCenter:
        return CGRectMake(0, 0, self.image.size.width, self.image.size.height);
        break;
    case UIViewContentModeScaleAspectFill: {
        CGSize imageSize = CGSizeMake(self.image.size.width, self.image.size.height);

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
        CGSize imageSize = CGSizeMake(self.image.size.width / self.image.scale,
                                      self.image.size.height / self.image.scale);

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

@end
