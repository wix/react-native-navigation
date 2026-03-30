#import "RNNViewLocation.h"
#import "RNNReactView.h"
#import <React/RCTSafeAreaView.h>

@implementation RNNViewLocation

- (instancetype)initWithFromElement:(UIView *)fromElement toElement:(UIView *)toElement {
    self = [super init];
    self.fromFrame = [self convertViewFrame:fromElement];
    self.toFrame = [self convertViewFrame:toElement];
    self.fromAngle = [self getViewAngle:fromElement];
    self.toAngle = [self getViewAngle:toElement];
    self.fromTransform = [self getTransform:fromElement];
    self.toTransform = [self getTransform:toElement];
    self.fromCornerRadius =
        fromElement.layer.cornerRadius ?: [self getClippedCornerRadius:fromElement];
    self.toCornerRadius = toElement.layer.cornerRadius ?: [self getClippedCornerRadius:toElement];
    self.index = [fromElement.superview.subviews indexOfObject:fromElement];

    self.fromBounds = [self convertViewBounds:fromElement];
    self.toBounds = [self convertViewBounds:toElement];
    self.fromCenter = [self convertViewCenter:fromElement];
    self.toCenter = [self convertViewCenter:toElement];
    self.fromPath = [self resolveViewPath:fromElement withSuperView:fromElement.superview];
    self.toPath = [self resolveViewPath:toElement withSuperView:toElement.superview];

    return self;
}

- (CGRect)resolveViewPath:(UIView *)view withSuperView:(UIView *)superView {
    const CGRect childFrame = [superView convertRect:view.bounds toView:superView.superview];
    if (superView) {
        const CGRect intersection = CGRectIntersection(superView.bounds, childFrame);
        if (!CGRectEqualToRect(intersection, view.frame) && superView.clipsToBounds) {
            return CGRectMake(-childFrame.origin.x, -childFrame.origin.y,
                              intersection.size.width + intersection.origin.x,
                              intersection.size.height + intersection.origin.y);
        } else {
            [self resolveViewPath:view withSuperView:superView.superview];
        }
    }

    return view.bounds;
}

- (CGFloat)getClippedCornerRadius:(UIView *)view {
    if (view.layer.cornerRadius > 0 && view.clipsToBounds) {
        return view.layer.cornerRadius;
    } else if (CGRectEqualToRect(view.frame, view.superview.bounds)) {
        return [self getClippedCornerRadius:view.superview];
    }

    return 0;
}

- (CATransform3D)getTransform:(UIView *)view {
    return view.layer.transform;
}

- (CGPoint)convertViewCenter:(UIView *)view {
    CGPoint center = [view.superview convertPoint:view.center toView:nil];
    return center;
}

- (CGRect)convertViewFrame:(UIView *)view {
    return [view.superview convertRect:view.frame toView:nil];
}

- (CGRect)convertViewBounds:(UIView *)view {
    CGRect convertedBounds = [view.superview convertRect:view.bounds toView:nil];
    return CGRectMake(0, 0, convertedBounds.size.width, convertedBounds.size.height);
}

- (CGFloat)getViewAngle:(UIView *)view {
    CGFloat radians = atan2f(view.transform.b, view.transform.a);
    return radians;
}

- (UIView *)topMostView:(UIView *)view {
    if ([view isKindOfClass:[RNNReactView class]]) {
        return view;
    } else {
        return [self topMostView:view.superview];
    }
}

@end
