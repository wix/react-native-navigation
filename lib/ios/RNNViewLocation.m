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
    return self;
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
    if (view) {
        if (!CATransform3DEqualToTransform(view.layer.transform, CATransform3DIdentity)) {
            return view.layer.transform;
        } else {
            return [self getTransform:view.superview];
        }
    }

    return CATransform3DIdentity;
}

- (CGRect)convertViewFrame:(UIView *)view {
    return [view.superview convertRect:view.frame toView:nil];
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
