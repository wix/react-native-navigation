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
    self.fromTransform = [self getTransform:fromElement];;
    self.toTransform = [self getTransform:toElement];
    self.toBounds = toElement.layer.bounds;
    self.fromBounds = fromElement.layer.bounds;
    
	return self;
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
    UIView* topMostView = [self topMostView:view];
    CGPoint center = [view.superview convertPoint:view.center toView:nil];
    CGFloat safeAreaTopOffset = [self safeAreaOffsetForView:view inView:topMostView];
    center.y += safeAreaTopOffset;
    CGRect frame = CGRectMake(center.x - view.bounds.size.width / 2, center.y - view.bounds.size.height / 2, view.bounds.size.width, view.bounds.size.height);
    return frame;
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

- (CGFloat)safeAreaOffsetForView:(UIView *)view inView:(UIView *)inView {
    CGFloat safeAreaOffset = inView.layoutMarginsGuide.layoutFrame.origin.y;
    
    if ([view isKindOfClass:RCTSafeAreaView.class] && [[view valueForKey:@"_currentSafeAreaInsets"] UIEdgeInsetsValue].top != safeAreaOffset) {
        return safeAreaOffset;
    } else if (view.superview) {
        return [self safeAreaOffsetForView:view.superview inView:inView];
    }
    
    return 0;
}

@end
