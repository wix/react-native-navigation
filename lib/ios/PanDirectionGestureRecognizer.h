#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, PanAxis) { PanAxisVertical, PanAxisHorizontal };

typedef NS_ENUM(NSInteger, PanDirection) {
    PanDirectionLeft,
    PanDirectionRight,
    PanDirectionUp,
    PanDirectionDown,
    PanDirectionNormal
};

@interface PanDirectionGestureRecognizer : UIPanGestureRecognizer

@property(nonatomic, assign) PanAxis axis;
@property(nonatomic, assign) PanDirection direction;

- (instancetype)initWithAxis:(PanAxis)axis
                   direction:(PanDirection)direction
                      target:(id)target
                      action:(SEL)action;

@end
