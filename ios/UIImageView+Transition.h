#import <UIKit/UIKit.h>

@interface UIImageView (Transition)

/**
 Returns the bounds of the actual underlying image transformed by the contentMode with a custom
 imageSize.
 */
- (CGRect)resolveBoundsWithImageSize:(CGSize)imageSize;
/**
 Returns the bounds of the actual underlying image transformed by the contentMode, assuming the
 underlying image has already been loaded.
 */
- (CGRect)resolveBounds;

@end
