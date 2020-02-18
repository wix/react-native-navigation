#import "RNNViewLocation.h"
#import "RNNReactView.h"
#import <React/RCTSafeAreaView.h>

@implementation RNNViewLocation

- (instancetype)initWithFromElement:(UIView *)fromElement toElement:(UIView *)toElement {
	self = [super init];
    self.fromFrame = [self convertViewFrame:fromElement];
    self.toFrame = [self convertViewFrame:toElement];
	return self;
}

- (CGRect)convertViewFrame:(UIView *)view {
    UIView* topMostView = [self topMostView:view];
    CGRect frame = [view.superview convertRect:view.frame toView:nil];
    CGFloat safeAreaTopOffset = [self safeAreaOffsetForView:view inView:topMostView];
    frame.origin.y += safeAreaTopOffset;

    return frame;
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
