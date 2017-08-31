#import <UIKit/UIKit.h>

// Undocumented animation curve used for the navigation controller's transition.
FOUNDATION_EXPORT UIViewAnimationOptions const RCCNavigationTransitionCurve;

@class RCCAnimator;

@protocol RCCAnimatorDelegate <NSObject>

@end

@interface RCCAnimator : NSObject <UIViewControllerAnimatedTransitioning>

@property (nonatomic, weak) id<RCCAnimatorDelegate> delegate;

@end
