#import "PanDirectionGestureRecognizer.h"

@implementation PanDirectionGestureRecognizer

- (instancetype)initWithAxis:(PanAxis)axis
                   direction:(PanDirection)direction
                      target:(id)target
                      action:(SEL)action {
    self = [super initWithTarget:target action:action];
    if (self) {
        _axis = axis;
        _direction = direction;
    }
    return self;
}

- (void)touchesMoved:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [super touchesMoved:touches withEvent:event];

    if (self.state == UIGestureRecognizerStateBegan) {
        CGPoint vel = [self velocityInView:self.view];
        switch (self.axis) {
        case PanAxisHorizontal:
            if (fabs(vel.y) > fabs(vel.x)) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        case PanAxisVertical:
            if (fabs(vel.x) > fabs(vel.y)) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        }

        BOOL isIncrement = (self.axis == PanAxisHorizontal) ? vel.x > 0 : vel.y > 0;

        switch (self.direction) {
        case PanDirectionLeft:
            if (isIncrement) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        case PanDirectionRight:
            if (!isIncrement) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        case PanDirectionUp:
            if (isIncrement) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        case PanDirectionDown:
            if (!isIncrement) {
                self.state = UIGestureRecognizerStateCancelled;
            }
            break;
        default:
            break;
        }
    }
}

@end
