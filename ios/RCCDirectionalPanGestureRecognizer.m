#import "RCCDirectionalPanGestureRecognizer.h"
#import <UIKit/UIGestureRecognizerSubclass.h>

@interface RCCDirectionalPanGestureRecognizer()
@property (nonatomic) BOOL dragging;
@end

@implementation RCCDirectionalPanGestureRecognizer

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    [super touchesMoved:touches withEvent:event];
    
    if (self.state == UIGestureRecognizerStateFailed) return;
    
    CGPoint velocity = [self velocityInView:self.view];
    
    // check direction only on the first move
    if (!self.dragging && !CGPointEqualToPoint(velocity, CGPointZero)) {
        NSDictionary *velocities = @{
                                     @(RCCPanDirectionRight) : @(velocity.x),
                                     @(RCCPanDirectionDown) : @(velocity.y),
                                     @(RCCPanDirectionLeft) : @(-velocity.x),
                                     @(RCCPanDirectionUp) : @(-velocity.y)
                                     };
        NSArray *keysSorted = [velocities keysSortedByValueUsingSelector:@selector(compare:)];
        
        // Fails the gesture if the highest velocity isn't in the same direction as `direction` property.
        if ([[keysSorted lastObject] integerValue] != self.direction) {
            self.state = UIGestureRecognizerStateFailed;
        }
        
        self.dragging = YES;
    }
}

- (void)reset
{
    [super reset];
    
    self.dragging = NO;
}

@end
