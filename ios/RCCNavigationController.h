#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import "RCCAnimator.h"
#import "RCCDirectionalPanGestureRecognizer.h"

@interface RCCNavigationController : UINavigationController <UINavigationControllerDelegate>

@property (strong, nonatomic) RCCAnimator *animator;
@property (strong, nonatomic) UIPercentDrivenInteractiveTransition *interactionController;
/// A Boolean value that indicates whether the navigation controller is currently animating a push/pop operation.
@property (nonatomic) BOOL duringAnimation;
/// Gesture recognizer used to recognize swiping to the right.
@property (weak, readonly, nonatomic) UIPanGestureRecognizer *panRecognizer;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge;

@end
