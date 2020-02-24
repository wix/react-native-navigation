#import "TransformRectTransition.h"

@implementation TransformRectTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toFrame = [RNNInterpolator fromRect:self.from toRect:self.to precent:p interpolation:self.interpolation];
    
    CGFloat scaleX = toFrame.size.width / self.from.size.width;
    CGFloat scaleY = toFrame.size.height / self.from.size.height;
    CGFloat offsetX = toFrame.origin.x - self.from.origin.x;
    CGFloat offsetY = toFrame.origin.y - self.from.origin.y;
    CGFloat translateX = (offsetX + (toFrame.size.width - self.from.size.width)/2);
    CGFloat translateY = (offsetY + (toFrame.size.height - self.from.size.height)/2);
    
    CATransform3D translate = CATransform3DMakeTranslation(translateX, translateY, 0);
    CATransform3D scale = CATransform3DScale(translate, scaleX, scaleY, 0);
    
    return scale;
}

@end
